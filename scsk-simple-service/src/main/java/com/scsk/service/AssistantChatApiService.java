package com.scsk.service;

/**
 * Agent転送
 * 
 * @param input
 *            リクエストデータ
 * @return Response レスポンスデータ
 */

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.constants.ApplicationKeys;
import com.scsk.constants.Constants;
import com.scsk.exception.BusinessException;
import com.scsk.model.AssistantChatApiDoc;
import com.scsk.request.vo.AssistantChatApiReqVO;
import com.scsk.response.vo.AssistantChatApiResVO;
import com.scsk.util.AssistantChat;
import com.scsk.util.EncryptorUtil;
import com.scsk.util.Utils;

@Service
public class AssistantChatApiService extends AbstractBLogic<AssistantChatApiReqVO, AssistantChatApiResVO> {
    // 検索key
    private final String key = "userId:\"%s\"";
    // DOCタイプ
    private final String DOCTYPE = "ASSISTANTCHAT";
    // 会話サクセス
    private final String SEND = "200";
    // IDデータを変更します
    private final String CHANGEIDDATA = "";

    @Autowired
    private EncryptorUtil encryptorUtil;
    @Autowired
    private AssistantChat assistantChat;

    @Override
    protected void preExecute(AssistantChatApiReqVO input) throws Exception {
    }

    @Override
    protected AssistantChatApiResVO doExecute(CloudantClient client, AssistantChatApiReqVO input) throws Exception {
        // データベースを取得
        Database db = client.database(Constants.DB_NAME, false);
        AssistantChatApiResVO output = new AssistantChatApiResVO();
        // 会話結果を時間
        String askDate = Utils.currentTime(Constants.DATE_FORMAT_ASS);
        // 会話結果を取得する
        String[] send = assistantChat.sendMessage(input.getAsk());
        // 会話結果を取得する時間
        String answerTimeDate = Utils.currentTime(Constants.DATE_FORMAT_ASS);
        String answer = "";
        if (!SEND.equals(send[0])) {
            answer = "{\"status\":\"" + send[0] + "\",\"status_message\":\"error\"}";
        } else {
            answer = send[1];
        }
        List<AssistantChatApiDoc> assistantChatApiDocList = null;
        try {
            assistantChatApiDocList = repositoryUtil.getIndex(db,
                    ApplicationKeys.INSIGHTINDEX_ASSISTANT_SEARCHBYUSERID_ASSISTANTCHAT,
                    String.format(key, encryptorUtil.encrypt(input.getUserId())), AssistantChatApiDoc.class);
        } catch (BusinessException e) {
            // 何もしない
        }
        AssistantChatApiDoc assistantChatApiDoc = null;
        Response response = null;
        if (assistantChatApiDocList != null && assistantChatApiDocList.size() > 0) {
            assistantChatApiDoc = assistantChatApiDocList.get(0);
            assistantChatApiDoc.setAsk(encryptorUtil.encrypt(input.getAsk()));
            assistantChatApiDoc.setAskTime(askDate);
            assistantChatApiDoc.setAnswer(encryptorUtil.encrypt(answer));
            assistantChatApiDoc.setAnswerTime(answerTimeDate);
            assistantChatApiDoc.setChangeIdData(CHANGEIDDATA);
            try {
                response = repositoryUtil.updateToResultRes(db, assistantChatApiDoc);
            } catch (BusinessException e) {
                // TODO
                throw new BusinessException("");

            }
        } else {
            assistantChatApiDoc = new AssistantChatApiDoc();
            assistantChatApiDoc.setDocType(DOCTYPE);
            assistantChatApiDoc.setUserId(encryptorUtil.encrypt(input.getUserId()));
            assistantChatApiDoc.setAsk(encryptorUtil.encrypt(input.getAsk()));
            assistantChatApiDoc.setAskTime(askDate);
            assistantChatApiDoc.setAnswer(encryptorUtil.encrypt(answer));
            assistantChatApiDoc.setAnswerTime(answerTimeDate);
            assistantChatApiDoc.setChangeIdData(CHANGEIDDATA);
            try {
                response = repositoryUtil.saveToResultRes(db, assistantChatApiDoc);
            } catch (BusinessException e) {
                // TODO
                throw new BusinessException("");
            }
        }

            output.setUserId(encryptorUtil.decrypt(assistantChatApiDoc.getUserId()));
            output.setAsk(encryptorUtil.decrypt(assistantChatApiDoc.getAsk()));
            output.setAskTime(assistantChatApiDoc.getAskTime());
            output.setAnswer(encryptorUtil.decrypt(assistantChatApiDoc.getAnswer()));
            output.setAnswerTime(assistantChatApiDoc.getAnswerTime());
            output.setStartRev(response.getRev());
            return output;
        
    }
}
