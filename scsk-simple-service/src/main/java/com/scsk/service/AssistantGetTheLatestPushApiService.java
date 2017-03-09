package com.scsk.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.DesignDocument.MapReduce;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.constants.Constants;
import com.scsk.exception.BusinessException;
import com.scsk.model.PushrecordDoc;
import com.scsk.request.vo.AssistantGetTheLatestPushApiReqVO;
import com.scsk.response.vo.AssistantGetTheLatestPushApiResVO;
import com.scsk.util.EncryptorUtil;
import com.scsk.util.LogInfoUtil;
import com.scsk.vo.AskAnswerVO;
@Service
public class AssistantGetTheLatestPushApiService extends AbstractBLogic<AssistantGetTheLatestPushApiReqVO, AssistantGetTheLatestPushApiResVO>{

    // Push開封状況:未開封
    private final String PUSHSTATUS1 = "1";
    // Push開封状況:開封
    private final String PUSHSTATUS2 = "2";
    // PUSHのタイプ
    private final String MSGTYPEPUSH = "1";
    @Autowired
    private EncryptorUtil encryptorUtil;

    @Override
    protected void preExecute(AssistantGetTheLatestPushApiReqVO input) throws Exception {
    }

    @Override
    protected AssistantGetTheLatestPushApiResVO doExecute(CloudantClient client,
            AssistantGetTheLatestPushApiReqVO input) throws Exception {
        Database db = client.database(Constants.DB_NAME, false);
        AssistantGetTheLatestPushApiResVO output = new AssistantGetTheLatestPushApiResVO();

        
        // プッシュ配信データを取得
        List<PushrecordDoc> pushrecordDocList = getPushrecordDocList(db, encryptorUtil.encrypt(input.getUserId()));
        List<AskAnswerVO> askAnsweVOList = new ArrayList<AskAnswerVO>();
        
        int unreadNum = 0;
        for (int i = 0; i < pushrecordDocList.size(); i++) {
            PushrecordDoc pushrecordDoc = pushrecordDocList.get(i);
            if (compareDate(input.getPushDate(), pushrecordDoc.getPushDate())) {
                AskAnswerVO askAnsweVO = new AskAnswerVO();
                askAnsweVO.setPush(pushrecordDoc.getPushContent());
                askAnsweVO.setPushTime(pushrecordDoc.getPushDate());
                askAnsweVO.setMsgType(MSGTYPEPUSH);
                askAnsweVOList.add(askAnsweVO);

                if (PUSHSTATUS1.equals(pushrecordDoc.getPushStatus())){
                    unreadNum++;
                    pushrecordDoc.setPushStatus(PUSHSTATUS2);
                    try {
                        repositoryUtil.update(db, pushrecordDoc);
                    } catch (BusinessException e) {
                        // 何もしない
                    }
                }
            }
        }
        askAnsweVOList.sort(new Comparator<AskAnswerVO>() {
            public int compare(AskAnswerVO nextRow, AskAnswerVO currentRow) {
                String currentRowTime = currentRow.getPushTime();
                String nextRowTime = nextRow.getPushTime();
                return currentRowTime.compareTo(nextRowTime);
            }
        });
        output.setUnreadNum(unreadNum);
        output.setPushList(askAnsweVOList);
        
        return output;
    }

    private boolean compareDate(String restrictionDate, String rowDate) {

        int i = restrictionDate.compareTo(rowDate);

        return i < 0 ? true : false;
    }

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
