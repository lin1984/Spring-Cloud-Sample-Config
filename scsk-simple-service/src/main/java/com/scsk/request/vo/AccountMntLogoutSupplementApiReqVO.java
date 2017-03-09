package com.scsk.request.vo;

/**
 * ログアウト補充リクエストデータ
 * 
 * @author ylq
 *
 */
public class AccountMntLogoutSupplementApiReqVO {

    //ユーザーID
    private String userId;
    //端末ID
    private String deviceTokenId;
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getDeviceTokenId() {
        return deviceTokenId;
    }
    public void setDeviceTokenId(String deviceTokenId) {
        this.deviceTokenId = deviceTokenId;
    }
    
}
