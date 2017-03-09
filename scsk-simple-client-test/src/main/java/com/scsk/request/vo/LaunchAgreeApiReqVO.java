package com.scsk.request.vo;
/**
 * 同意リクエストデータ
 * 
 * @author ylq
 *
 */
public class LaunchAgreeApiReqVO {

    // ドキュメントタイプ
    private String docType = "";
    // 端末ID
    private String deviceTokenId = "";
    // ユーザーID
    private String userId = "";
    
    public String getDocType() {
        return docType;
    }
    public void setDocType(String docType) {
        this.docType = docType;
    }
    public String getDeviceTokenId() {
        return deviceTokenId;
    }
    public void setDeviceTokenId(String deviceTokenId) {
        this.deviceTokenId = deviceTokenId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
