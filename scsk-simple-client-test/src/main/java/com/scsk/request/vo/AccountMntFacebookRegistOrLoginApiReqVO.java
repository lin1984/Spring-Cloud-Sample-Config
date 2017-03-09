package com.scsk.request.vo;

import java.util.List;
/**
 * facebook登録＆ログインリクエストデータ
 * @author ylq
 *
 */
public class AccountMntFacebookRegistOrLoginApiReqVO {
    // ユーザーID
    private String userId;
    // 端末ID配列
    private String deviceTokenId;
    // E-mail
    private String email;
    // 姓
    private String lastName;
    // 名
    private String firstName;
    //姓カナ
    private String kanaLastName;
    //名カナ
    private String kanaFirstName;
    // 満年齢
    private String age;
    // 職業
    private List<String> occupation;
    // その他職業
    private String otherOccupations;
    // 性別
    private int sex;
    // 電話番号
    private String phoneNumber;
    //自宅
    private String teleNumber;
    // 勤務先名
    private String workName;
    // 勤務先電話番号
    private String workTeleNumber;
    // 誕生日
    private String birthday;
    // 郵便番号
    private String postCode;
    // 都
    private String address1;
    // 道府
    private String address2;
    // 県
    private String address3;
    // 市区町村以下
    private String address4;
    //市区町村以下（カナ）
    private String kanaAddress;
    // 口座情報
    private List<String> cardNoList;
    // 興味あるテーマ
    private List<String> themeList;
    // facebookID
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
    
    public String getWorkName() {
        return workName;
    }
    public void setWorkName(String workName) {
        this.workName = workName;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWorkTeleNumber() {
        return workTeleNumber;
    }

    public void setWorkTeleNumber(String workTeleNumber) {
        this.workTeleNumber = workTeleNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<String> getOccupation() {
        return occupation;
    }

    public void setOccupation(List<String> occupation) {
        this.occupation = occupation;
    }

    public String getOtherOccupations() {
        return otherOccupations;
    }

    public void setOtherOccupations(String otherOccupations) {
        this.otherOccupations = otherOccupations;
    }

    public String getKanaLastName() {
        return kanaLastName;
    }

    public void setKanaLastName(String kanaLastName) {
        this.kanaLastName = kanaLastName;
    }

    public String getKanaFirstName() {
        return kanaFirstName;
    }

    public void setKanaFirstName(String kanaFirstName) {
        this.kanaFirstName = kanaFirstName;
    }

    public String getTeleNumber() {
        return teleNumber;
    }

    public void setTeleNumber(String teleNumber) {
        this.teleNumber = teleNumber;
    }

    public String getKanaAddress() {
        return kanaAddress;
    }

    public void setKanaAddress(String kanaAddress) {
        this.kanaAddress = kanaAddress;
    }

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public List<String> getCardNoList() {
        return cardNoList;
    }

    public void setCardNoList(List<String> cardNoList) {
        this.cardNoList = cardNoList;
    }

    public List<String> getThemeList() {
        return themeList;
    }

    public void setThemeList(List<String> themeList) {
        this.themeList = themeList;
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
