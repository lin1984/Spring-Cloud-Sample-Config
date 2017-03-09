package com.scsk.request.vo;
/**
 * メールアドレスのログインリクエストデータ
 * 
 * @author ylq
 */
public class AccountMntAccountLoginApiReqVO {

    // 端末ID
    private String deviceTokenId;

    // メールアドレス
    private String email;

    // パスワード
    private String password;

    public String getDeviceTokenId() {
        return deviceTokenId;
    }

    public void setDeviceTokenId(String deviceTokenId) {
        this.deviceTokenId = deviceTokenId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
