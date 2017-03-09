package com.scsk.request.vo;

import java.util.List;
/**
 * 口座開設リクエストデータ
 * 
 * @author ylq
 */
public class AccountApplicationApplyApiReqVO {

    //ユーザーID
    private String userId;
    //ユーザータイプ
    private int userType;
    //端末ID
    private String deviceTokenId;
    //免許証号
    private String licenseId;
    // 姓
    private String lastName;
    //名
    private String firstName;
    //姓名カナ
    private String kanaLastName;
    //名カナ
    private String kanaFirstName;
    // 満年齢
    private String age;
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
    //自宅電話番号
    private String teleNumber;
    //携帯電話番号
    private String phoneNumber;
    //
    private String workName;
    // 勤務先電話番号
    private String workTeleNumber;
    //発行するカード種類
    private String cardType;
    //利用しないサービス
    private List<String> noApplicationService;
    //口座開設と同時にお申込みいただける商品指定
    private String goodsAppointed;
    //特定口座について
    private String specificAccount;
    //カードローン
    private String cardLoans;
    //取引目的
    private List<String> tradingPurposes;
    //その他取引目的
    private String otherTradingPurposes;
    //職業
    private List<String> occupation;
    //その他職業
    private String otherOccupations;
    //暗証番号
    private String securityPassword;
 // テレホンバンキングサービス_事前登録振込（１回あたり）
    private int telRegisterPerTrans = 0;
    // テレホンバンキングサービス_事前登録振込（１日あたり）
    private int telRegisterPerDay = 0;
    // テレホンバンキングサービス_都度登録振込（１回あたり）
    private int telOncePerTrans = 0;
    // テレホンバンキングサービス_都度登録振込（１日あたり）
    private int telOncePerDay = 0;
    // インターネットバンキングサービス_事前登録振込（１回あたり）
    private int internetRegisterPerTrans = 0;
    // インターネットバンキングサービス_事前登録振込（１日あたり）
    private int internetRegisterPerDay = 0;
    // インターネットバンキングサービス_都度登録振込（１回あたり）
    private int internetOncePerTrans = 0;
    // インターネットバンキングサービス_都度登録振込（１日あたり）
    private int internetOncePerDay = 0;
    // モバイルバンキングサービス_事前登録振込（１回あたり）
    private int mobileRegisterPerTrans = 0;
    // モバイルバンキングサービス_事前登録振込（１日あたり）
    private int mobileRegisterPerDay = 0;
    // モバイルバンキングサービス_都度登録振込（１回あたり）
    private int mobileOncePerTrans = 0;
    // モバイルバンキングサービス_都度登録振込（１日あたり）
    private int mobileOncePerDay = 0;
    //本人確認書類    
    private String identificationType;
    //本人確認書類画像
    private String identificationImage;
    //本人確認書類画像裏面
    private String identificationImageBack;  
    //生活状況確認書類
    private String livingConditions;
    //生活状況確認書類画像
    private String livingConditionsImage;
    // IPアドレス
    private String ipAddress;
    // 既に口座をお持ちの方
    private String holdAccount;
    // 既に口座をお持ちの方：店名
    private String holdAccountBank;
    // 既に口座をお持ちの方：口座番号
    private String holdAccountNumber;
    // ダイレクトバンキングサービスのご契約
    private String directServicesContract;
    // ダイレクトバンキングサービスのご契約：店名
    private String directServicesContractBank;
    // ダイレクトバンキングサービスのご契約：口座番号
    private String directServicesContractAccountNumber;
    // ダイレクトバンキングカード暗証番号
    private String directServicesCardPassword;
    // ひろぎんネット支店の口座開設の動機
    private String accountAppMotive;
    // ひろぎんネット支店の口座開設のその他動機
    private String accountAppOtherMotive;
    // ひろぎんネット支店を知った経緯
    private String knowProcess;
    // ひろぎんネット支店を知ったその他経緯
    private String knowOtherProcess;
    // ストアコード
    private String storeCode = "";
    //
    private String agreedOperationDate;
    
    public String getStoreCode() {
        return storeCode;
    }
    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }
    public String getWorkName() {
        return workName;
    }
    public void setWorkName(String workName) {
        this.workName = workName;
    }
    public void setAgreedOperationDate(String agreedOperationDate) {
        this.agreedOperationDate = agreedOperationDate;
    }
    public String getAgreedOperationDate() {
        return agreedOperationDate;
    }
    
    public String getHoldAccount() {
        return holdAccount;
    }
    public void setHoldAccount(String holdAccount) {
        this.holdAccount = holdAccount;
    }
    public String getHoldAccountBank() {
        return holdAccountBank;
    }
    public void setHoldAccountBank(String holdAccountBank) {
        this.holdAccountBank = holdAccountBank;
    }
    public String getHoldAccountNumber() {
        return holdAccountNumber;
    }
    public void setHoldAccountNumber(String holdAccountNumber) {
        this.holdAccountNumber = holdAccountNumber;
    }
    public String getDirectServicesContract() {
        return directServicesContract;
    }
    public void setDirectServicesContract(String directServicesContract) {
        this.directServicesContract = directServicesContract;
    }
    public String getDirectServicesContractBank() {
        return directServicesContractBank;
    }
    public void setDirectServicesContractBank(String directServicesContractBank) {
        this.directServicesContractBank = directServicesContractBank;
    }
    public String getDirectServicesContractAccountNumber() {
        return directServicesContractAccountNumber;
    }
    public void setDirectServicesContractAccountNumber(String directServicesContractAccountNumber) {
        this.directServicesContractAccountNumber = directServicesContractAccountNumber;
    }
    public String getDirectServicesCardPassword() {
        return directServicesCardPassword;
    }
    public void setDirectServicesCardPassword(String directServicesCardPassword) {
        this.directServicesCardPassword = directServicesCardPassword;
    }
    public String getAccountAppMotive() {
        return accountAppMotive;
    }
    public void setAccountAppMotive(String accountAppMotive) {
        this.accountAppMotive = accountAppMotive;
    }
    public String getAccountAppOtherMotive() {
        return accountAppOtherMotive;
    }
    public void setAccountAppOtherMotive(String accountAppOtherMotive) {
        this.accountAppOtherMotive = accountAppOtherMotive;
    }
    public String getKnowProcess() {
        return knowProcess;
    }
    public void setKnowProcess(String knowProcess) {
        this.knowProcess = knowProcess;
    }
    public String getKnowOtherProcess() {
        return knowOtherProcess;
    }
    public void setKnowOtherProcess(String knowOtherProcess) {
        this.knowOtherProcess = knowOtherProcess;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getIdentificationImageBack() {
        return identificationImageBack;
    }
    public void setIdentificationImageBack(String identificationImageBack) {
        this.identificationImageBack = identificationImageBack;
    }
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
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
    public String getLicenseId() {
        return licenseId;
    }
    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
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
    public String getKanaAddress() {
        return kanaAddress;
    }
    public void setKanaAddress(String kanaAddress) {
        this.kanaAddress = kanaAddress;
    }
    public String getTeleNumber() {
        return teleNumber;
    }
    public void setTeleNumber(String teleNumber) {
        this.teleNumber = teleNumber;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getCardType() {
        return cardType;
    }
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
    public List<String> getNoApplicationService() {
        return noApplicationService;
    }
    public void setNoApplicationService(List<String> noApplicationService) {
        this.noApplicationService = noApplicationService;
    }
    public String getGoodsAppointed() {
        return goodsAppointed;
    }
    public void setGoodsAppointed(String goodsAppointed) {
        this.goodsAppointed = goodsAppointed;
    }
    public String getSpecificAccount() {
        return specificAccount;
    }
    public void setSpecificAccount(String specificAccount) {
        this.specificAccount = specificAccount;
    }
    public String getCardLoans() {
        return cardLoans;
    }
    public void setCardLoans(String cardLoans) {
        this.cardLoans = cardLoans;
    }
    public List<String> getTradingPurposes() {
        return tradingPurposes;
    }
    public void setTradingPurposes(List<String> tradingPurposes) {
        this.tradingPurposes = tradingPurposes;
    }
    public String getOtherTradingPurposes() {
        return otherTradingPurposes;
    }
    public void setOtherTradingPurposes(String otherTradingPurposes) {
        this.otherTradingPurposes = otherTradingPurposes;
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
    public String getSecurityPassword() {
        return securityPassword;
    }
    public void setSecurityPassword(String securityPassword) {
        this.securityPassword = securityPassword;
    }

    public int getTelRegisterPerTrans() {
        return telRegisterPerTrans;
    }
    public void setTelRegisterPerTrans(int telRegisterPerTrans) {
        this.telRegisterPerTrans = telRegisterPerTrans;
    }
    public int getTelRegisterPerDay() {
        return telRegisterPerDay;
    }
    public void setTelRegisterPerDay(int telRegisterPerDay) {
        this.telRegisterPerDay = telRegisterPerDay;
    }
    public int getTelOncePerTrans() {
        return telOncePerTrans;
    }
    public void setTelOncePerTrans(int telOncePerTrans) {
        this.telOncePerTrans = telOncePerTrans;
    }
    public int getTelOncePerDay() {
        return telOncePerDay;
    }
    public void setTelOncePerDay(int telOncePerDay) {
        this.telOncePerDay = telOncePerDay;
    }
    public int getInternetRegisterPerTrans() {
        return internetRegisterPerTrans;
    }
    public void setInternetRegisterPerTrans(int internetRegisterPerTrans) {
        this.internetRegisterPerTrans = internetRegisterPerTrans;
    }
    public int getInternetRegisterPerDay() {
        return internetRegisterPerDay;
    }
    public void setInternetRegisterPerDay(int internetRegisterPerDay) {
        this.internetRegisterPerDay = internetRegisterPerDay;
    }
    public int getInternetOncePerTrans() {
        return internetOncePerTrans;
    }
    public void setInternetOncePerTrans(int internetOncePerTrans) {
        this.internetOncePerTrans = internetOncePerTrans;
    }
    public int getInternetOncePerDay() {
        return internetOncePerDay;
    }
    public void setInternetOncePerDay(int internetOncePerDay) {
        this.internetOncePerDay = internetOncePerDay;
    }
    public int getMobileRegisterPerTrans() {
        return mobileRegisterPerTrans;
    }
    public void setMobileRegisterPerTrans(int mobileRegisterPerTrans) {
        this.mobileRegisterPerTrans = mobileRegisterPerTrans;
    }
    public int getMobileRegisterPerDay() {
        return mobileRegisterPerDay;
    }
    public void setMobileRegisterPerDay(int mobileRegisterPerDay) {
        this.mobileRegisterPerDay = mobileRegisterPerDay;
    }
    public int getMobileOncePerTrans() {
        return mobileOncePerTrans;
    }
    public void setMobileOncePerTrans(int mobileOncePerTrans) {
        this.mobileOncePerTrans = mobileOncePerTrans;
    }
    public int getMobileOncePerDay() {
        return mobileOncePerDay;
    }
    public void setMobileOncePerDay(int mobileOncePerDay) {
        this.mobileOncePerDay = mobileOncePerDay;
    }
    public String getIdentificationType() {
        return identificationType;
    }
    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }
    public String getIdentificationImage() {
        return identificationImage;
    }
    public void setIdentificationImage(String identificationImage) {
        this.identificationImage = identificationImage;
    }
    public String getLivingConditions() {
        return livingConditions;
    }
    public void setLivingConditions(String livingConditions) {
        this.livingConditions = livingConditions;
    }
    public String getLivingConditionsImage() {
        return livingConditionsImage;
    }
    public void setLivingConditionsImage(String livingConditionsImage) {
        this.livingConditionsImage = livingConditionsImage;
    }
    public String getWorkTeleNumber() {
        return workTeleNumber;
    }
    public void setWorkTeleNumber(String workTeleNumber) {
        this.workTeleNumber = workTeleNumber;
    }
}
