package com.scsk.service;

/**
 * Assistant担当
 * 
 * @author ylq
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloudant.client.api.CloudantClient;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.request.vo.AssistantChatCohesionApiReqVO;
import com.scsk.response.vo.AssistantChatCohesionApiResVO;
import com.scsk.util.AssistantChat;


@Service
public class AssistantChatCohesionApiService extends AbstractBLogic<AssistantChatCohesionApiReqVO, AssistantChatCohesionApiResVO> {

    // 担当会話对比
    private final String SEND = "この度はお問い合わせ誠にありがとうございます。";
    @Autowired
    private AssistantChat assistantChat;
    
    
    @Override
    protected void preExecute(AssistantChatCohesionApiReqVO input) throws Exception {
    }

    @Override
    protected AssistantChatCohesionApiResVO doExecute(CloudantClient client, AssistantChatCohesionApiReqVO input)
            throws Exception {
        
        AssistantChatCohesionApiResVO output = new AssistantChatCohesionApiResVO();
        // 会話結果を取得する
        String[] send = assistantChat.sendMessage(input.getAsk());
        // 会話結果パラメーターを取得する
        String content = assistantChat.sendAsk(send[1]);
        
        if (content != null && content.startsWith(SEND)) {
            output.setAnswer(send[1]);
            return output;
        }else {
            output.setAnswer("");
            return output;
        }
    }

}
