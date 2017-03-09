package com.scsk.request.vo;
/**
 * Facebook連携リクエストデータ
 * @author ylq
 *
 */
public class AccountMntFacebookTogetherApiReqVO {

    // ユーザーID
    private String userId;

    // facebook氏名
    private String facebookId;

    // facebook氏名
    private String facebookName;

    // facebookメールアドレス
    private String facebookEmail;

    // facebook性别
    private int facebookSex;

    // facebook誕生日
    private String facebookBirthday;

    // facebook電話
    private String facebookPhoneNumber;

    // facebook住所
    private String facebookAddress;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFacebookName() {
        return facebookName;
    }

    public void setFacebookName(String facebookName) {
        this.facebookName = facebookName;
    }

    public String getFacebookEmail() {
        return facebookEmail;
    }

    public void setFacebookEmail(String facebookEmail) {
        this.facebookEmail = facebookEmail;
    }

    public int getFacebookSex() {
        return facebookSex;
    }

    public void setFacebookSex(int facebookSex) {
        this.facebookSex = facebookSex;
    }

    public String getFacebookBirthday() {
        return facebookBirthday;
    }

    public void setFacebookBirthday(String facebookBirthday) {
        this.facebookBirthday = facebookBirthday;
    }

    public String getFacebookPhoneNumber() {
        return facebookPhoneNumber;
    }

    public void setFacebookPhoneNumber(String facebookPhoneNumber) {
        this.facebookPhoneNumber = facebookPhoneNumber;
    }

    public String getFacebookAddress() {
        return facebookAddress;
    }

    public void setFacebookAddress(String facebookAddress) {
        this.facebookAddress = facebookAddress;
    }

}
