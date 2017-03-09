package com.scsk.request.vo;
/**
 * パスワード変更サービススリクエストデータ
 * @author 9003474
 *
 */
public class AccountMntUpdateUserPasswordApiReqVO {

    // ユーザーID
    String userId;

    // パスワード
    String password;

    // 新しいパスワード
    String newPassword;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
