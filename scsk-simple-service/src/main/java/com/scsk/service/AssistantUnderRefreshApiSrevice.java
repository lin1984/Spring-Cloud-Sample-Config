package com.scsk.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.DesignDocument.MapReduce;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.constants.ApplicationKeys;
import com.scsk.constants.Constants;
import com.scsk.exception.BusinessException;
import com.scsk.model.AssistantChatApiDoc;
import com.scsk.model.PushrecordDoc;
import com.scsk.model.rev.RevUtilDoc;
import com.scsk.model.rev.RevsInfo;
import com.scsk.request.vo.AssistantUnderRefreshApiReqVO;
import com.scsk.response.vo.AssistantUnderRefreshApiResVO;
import com.scsk.util.EncryptorUtil;
import com.scsk.util.LogInfoUtil;
import com.scsk.util.Utils;
import com.scsk.vo.AskAnswerVO;

/**
 * Agentドロップダウンリフレッシュ
 * 
 * @author ws
 */
@Service
public class AssistantUnderRefreshApiSrevice
        extends AbstractBLogic<AssistantUnderRefreshApiReqVO, AssistantUnderRefreshApiResVO> {

    private final String key = "userId:\"%s\"";
    // 最大行数
    private final int MAXROW = 10;
    // 日数を制限
    private final int UPPERLIMITDAYS = 2;
    // 対話のタイプ
    private final String MSGTYPECHAT = "0";
    // PUSHのタイプ
    private final String MSGTYPEPUSH = "1";
    // データなしフラグ
    private final String DATAFLGTYPE1 = "1";
    // データあるフラグ
    private final String DATAFLGTYPE0 = "0";
    // Push開封状況:未開封
    private final String PUSHSTATUS1 = "1";
    // Push開封状況:開封
    private final String PUSHSTATUS2 = "2";
    // 無効なバージョン
    private final String VER_INVALID = "missing";

    private final String SPACE = "";

    private final String IS_UPDATEIDDATA = "1";

    @Autowired
    private EncryptorUtil encryptorUtil;

    @Override
    protected void preExecute(AssistantUnderRefreshApiReqVO input) throws Exception {
    }

    @Override
    protected AssistantUnderRefreshApiResVO doExecute(CloudantClient client, AssistantUnderRefreshApiReqVO input)
            throws Exception {
        Database db = client.database(Constants.DB_NAME, false);
        AssistantUnderRefreshApiResVO output = new AssistantUnderRefreshApiResVO();

        String startRev = input.getStartRev() == null ? SPACE : input.getStartRev();
        String _noDataFlg = DATAFLGTYPE0;
        String _startRev = startRev;
        String startTime = null;
        String endTime = null;
        String _id = null;

        // バージョン履歴情報
        RevUtilDoc revDoc = null;
        List<AssistantChatApiDoc> assistantChatApiDocUserId = repositoryUtil.getIndex(db,
                ApplicationKeys.INSIGHTINDEX_ASSISTANT_SEARCHBYUSERID_ASSISTANTCHAT,
                String.format(key, encryptorUtil.encrypt(input.getUserId())), AssistantChatApiDoc.class);
        if (assistantChatApiDocUserId != null && assistantChatApiDocUserId.size() > 0) {
            _id = assistantChatApiDocUserId.get(0).get_id();
            try {
                // バージョン履歴情報を取得
                revDoc = repositoryUtil.find(db, _id);
            } catch (BusinessException e) {
                // 何もしない
            }
        }

        // 問答データリスト
        List<AssistantChatApiDoc> assistantChatApiDocList = new ArrayList<>();
        if (revDoc != null) {
            RevsInfo revsInfo[] = revDoc.get_revs_info();
            // Revisions revisions = revDoc.get_revisions();
            int count = 0;
            String restrictionDate = getRestrictionDate();
            Long revNum = SPACE.equals(startRev) ? 0 : Long.valueOf(startRev.split("-")[0]);

            int sIndex = SPACE.equals(startRev) ? 0
                    : revsInfo.length - Integer.valueOf(startRev.split("-")[0]).intValue() + 1;

            for (int i = sIndex; i < revsInfo.length && count < MAXROW; i++) {
                String cRev = revsInfo[i].getRev();
                String cRevNum = cRev.split("-")[0];
                if ((SPACE.equals(startRev) || revNum >= Long.valueOf(cRevNum))
                        && !VER_INVALID.equals(revsInfo[i].getStatus())) {
                    try {
                        AssistantChatApiDoc assistantChatApiDoc = repositoryUtil.find(db, _id, cRev,
                                AssistantChatApiDoc.class);
                        if (IS_UPDATEIDDATA.equals(assistantChatApiDoc.getUpdatedDate()))
                            continue;
                        if (compareDate(restrictionDate, assistantChatApiDoc.getAskTime())) {
                            _startRev = cRev;
                            count++;
                            startTime = assistantChatApiDoc.getAskTime();
                            assistantChatApiDocList.add(assistantChatApiDoc);
                        } else {
                            _noDataFlg = DATAFLGTYPE1;
                            break;
                        }
                    } catch (BusinessException e) {
                        // 何もしない
                    }
                }
            }
        } else {
            _noDataFlg = DATAFLGTYPE1;
        }

        // プッシュ配信データを取得
        List<PushrecordDoc> pushrecordDocList = getPushrecordDocList(db, encryptorUtil.encrypt(input.getUserId()));

        if (DATAFLGTYPE1.equals(_noDataFlg) || assistantChatApiDocList.size() == 0) {
            _noDataFlg = DATAFLGTYPE1;
            if (SPACE.equals(startRev)) {
                startTime = Utils.currentTime(Constants.DATE_FORMAT_ASS);
            } else {
                if (startTime == null) {
                    try {
                        // 20161222 _idはnull対応、問答データ存在しない場合、_idはnull.
                        if (_id != null) {
                            AssistantChatApiDoc assistantChatApiDoc = repositoryUtil.find(db, _id, startRev,
                                    AssistantChatApiDoc.class);
                            startTime = String.valueOf(Long.valueOf(assistantChatApiDoc.getAskTime()) + 1);
                        } else {
                            startTime = "0";
                        }
                    } catch (BusinessException e) {
                        startTime = "0";
                    }
                }
            }
        } else {
            if (SPACE.equals(startRev)) {
                startTime = Utils.currentTime(Constants.DATE_FORMAT_ASS);
            } else {
                startTime = assistantChatApiDocList.get(0).getAskTime();
            }
            endTime = assistantChatApiDocList.get(assistantChatApiDocList.size() - 1).getAskTime();
        }

        List<AskAnswerVO> askAnsweVOList = new ArrayList<AskAnswerVO>();
        for (int i = 0; i < assistantChatApiDocList.size(); i++) {
            AssistantChatApiDoc assistantChatApiDoc = assistantChatApiDocList.get(i);
            if ("1".equals(assistantChatApiDoc.getChangeIdData())) {
                continue;
            }
            AskAnswerVO askAnsweVO = new AskAnswerVO();
            askAnsweVO.setMsgType(MSGTYPECHAT);
            askAnsweVO.setAsk(encryptorUtil.decrypt(assistantChatApiDoc.getAsk()));
            askAnsweVO.setAskTime(assistantChatApiDoc.getAskTime());
            askAnsweVO.setAnswer(encryptorUtil.decrypt(assistantChatApiDoc.getAnswer()));
            askAnsweVO.setAnswerTime(assistantChatApiDoc.getAnswerTime());
            askAnsweVOList.add(askAnsweVO);
        }

        int unreadCount = 0;
        for (int i = 0; i < pushrecordDocList.size(); i++) {
            PushrecordDoc pushrecordDoc = pushrecordDocList.get(i);
            if (!"4".equals(pushrecordDoc.getPushStatus())) {
                if (PUSHSTATUS1.equals(pushrecordDoc.getPushStatus()))
                    unreadCount++;
                if (validDataCheck(startTime, endTime, _noDataFlg, pushrecordDoc.getPushDate())) {
                    if (PUSHSTATUS1.equals(pushrecordDoc.getPushStatus())) {
                        unreadCount--;
                        pushrecordDoc.setPushStatus(PUSHSTATUS2);
                        try {
                            repositoryUtil.update(db, pushrecordDoc);
                        } catch (BusinessException e) {
                            // 何もしない
                        }
                    }
                    AskAnswerVO askAnsweVO = new AskAnswerVO();
                    askAnsweVO.setMsgType(MSGTYPEPUSH);
                    askAnsweVO.setPush(pushrecordDoc.getPushContent());
                    askAnsweVO.setPushTime(pushrecordDoc.getPushDate());
                    askAnsweVOList.add(askAnsweVO);
                }
            }
        }

        askAnsweVOList.sort(new Comparator<AskAnswerVO>() {
            public int compare(AskAnswerVO nextRow, AskAnswerVO currentRow) {
                String currentRowTime = MSGTYPECHAT.equals(currentRow.getMsgType()) ? currentRow.getAnswerTime()
                        : currentRow.getPushTime();
                String nextRowTime = MSGTYPECHAT.equals(nextRow.getMsgType()) ? nextRow.getAnswerTime()
                        : nextRow.getPushTime();
                return currentRowTime.compareTo(nextRowTime);
            }
        });

        output.setUserId(input.getUserId());
        output.setUnreadCount(unreadCount);
        output.setStartRev(_startRev);
        output.setNoDataFlg(_noDataFlg);
        output.setAskAnswerVOList(askAnsweVOList);
        return output;
    }

    /**
     * 日付制限を取得
     */
    private String getRestrictionDate() {
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_APP_DATE);
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, UPPERLIMITDAYS);
        d = ca.getTime();
        return format.format(d);
    }

    /**
     * 
     * @param restrictionDate
     * @param rowDate
     * @return
     */
    private boolean compareDate(String restrictionDate, String rowDate) {

        int i = restrictionDate.compareTo(rowDate.substring(0, 8));

        return i >= 0 ? true : false;
    }

    /**
     * 
     * @param startTime
     * @param endTime
     * @param noDataFlg
     * @param rowDate
     * @return
     */
    private boolean validDataCheck(String startTime, String endTime, String noDataFlg, String rowDate) {
        String rt = rowDate + "000";
        if (DATAFLGTYPE1.equals(noDataFlg)) {
            int s = startTime.compareTo(rt);
            if (s >= 0) {
                return true;
            }
        } else {
            int s = startTime.compareTo(rt);
            int e = endTime.compareTo(rt);
            if (s >= 0 && e <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param db
     * @param userId
     * @return
     */
    private List<PushrecordDoc> getPushrecordDocList(Database db, String userId) {
        List<PushrecordDoc> pushrecordDocList = new ArrayList<>();
        try {
            MapReduce view = new MapReduce();
            view.setMap("function (doc) {\r\n  if(doc.docType && doc.docType === \"PUSHRECORD\" && doc.userId === \""
                    + userId
                    + "\" && doc.delFlg && doc.delFlg==\"0\" ){\r\n    if(doc.pushDate !== \"\") {\r\n      emit(doc.accountAppSeq, 1);\r\n    }\r\n  }\r\n}");
            pushrecordDocList = repositoryUtil.queryByDynamicView(db, view, true, PushrecordDoc.class);
        } catch (BusinessException e) {
            // TODO
            LogInfoUtil.logWarn("", e);
        }
        return pushrecordDocList;
    }
}
