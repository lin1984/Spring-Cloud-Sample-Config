package com.scsk.request.vo;
/**
 * 最新のプッシュを取得リクエストデータ
 * @author ylq
 *
 */
public class AssistantGetTheLatestPushApiReqVO {

    // ユーザーID
    private String userId;
    // 配信日付
    private String pushDate;
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getPushDate() {
        return pushDate;
    }
    public void setPushDate(String pushDate) {
        this.pushDate = pushDate;
    }
}
