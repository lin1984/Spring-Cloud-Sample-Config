package com.scsk.request.vo;
/**
 * 自動応答フリーテキストスリクエストデータ
 * @author ylq
 *
 */
public class AssistantChatApiReqVO {

    //  ユーザーID
    private String userId;
    //  agent回答的问题
    private String ask;

    public String getAsk() {
        return ask;
    }
    public void setAsk(String ask) {
        this.ask = ask;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
