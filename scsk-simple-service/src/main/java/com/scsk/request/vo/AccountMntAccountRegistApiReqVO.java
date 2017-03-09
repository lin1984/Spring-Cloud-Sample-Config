package com.scsk.request.vo;

import java.util.List;

/**
 * メールアドレスの登録リクエストデータ
 * 
 * @author ws
 *
 */
public class AccountMntAccountRegistApiReqVO {
    
    //ユーザID
    private String userId;
    // ユーザータイプ
    private int userType;
    // 端末ID
    private String deviceTokenId;
    // E-mail
    private String email;
    // 姓
    private String lastName;
    // 名
    private String firstName;
    //姓名カナ
    private String kanaLastName;
    //名カナ
    private String kanaFirstName;
    // 満年齢
    private String age;
    // 職業
    private List<String> occupation;
    // その他職業
    private String otherOccupations;
    // 暗証番号
    private String password;
    // 電話番号
    private String phoneNumber;
    //自宅
    private String teleNumber;
    // 勤務先名
    private String workName;
    // 勤務先電話番号
    private String workTeleNumber;
    // 生年月日
    private String birthday;
    // 性别
    private int sex;
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
    
    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKanaFirstName() {
        return kanaFirstName;
    }

    public void setKanaFirstName(String kanaFirstName) {
        this.kanaFirstName = kanaFirstName;
    }

    public String getKanaLastName() {
        return kanaLastName;
    }

    public void setKanaLastName(String kanaLastName) {
        this.kanaLastName = kanaLastName;
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
    
    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getWorkTeleNumber() {
        return workTeleNumber;
    }

    public void setWorkTeleNumber(String workTeleNumber) {
        this.workTeleNumber = workTeleNumber;
    }
}