package com.scsk.request.vo;

/**
 * メールアドレス変更リクエストデータ
 * 
 * @author ws
 *
 */
public class AccountMntChangeAccountEmailApiReqVO {
    // ユーザーID
    private String userId;
    // 新たしいE-mail
    private String newEmail;
    // 暗証番号
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}