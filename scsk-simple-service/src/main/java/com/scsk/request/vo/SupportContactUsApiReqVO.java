package com.scsk.request.vo;

public class SupportContactUsApiReqVO {
    
    // ユーザーID
    private String userId;
    
    // 端末ID
    private String deviceTokenId;
    
    // お問い合わせ
    private String contactUs;

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

    public String getContactUs() {
        return contactUs;
    }

    public void setContactUs(String contactUs) {
        this.contactUs = contactUs;
    }

}
