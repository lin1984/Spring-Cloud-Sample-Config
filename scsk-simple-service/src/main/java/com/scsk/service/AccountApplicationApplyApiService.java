package com.scsk.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.DesignDocument.MapReduce;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.constants.ApplicationKeys;
import com.scsk.constants.Constants;
import com.scsk.constants.MessageKeys;
import com.scsk.exception.BusinessException;
import com.scsk.model.AccountAppAgreedOperationDateDoc;
import com.scsk.model.AccountAppDoc;
import com.scsk.model.AccountAppImageDoc;
import com.scsk.model.PushrecordDoc;
import com.scsk.model.UserInfoDoc;
import com.scsk.model.rev.GeologicalAppraisalIPDoc;
import com.scsk.model.rev.PhoneNumberDoc;
import com.scsk.model.rev.TeleNumberDoc;
import com.scsk.repository.RepositoryUtil;
import com.scsk.request.vo.AccountAppSeqReqVO;
import com.scsk.request.vo.AccountApplicationApplyApiReqVO;
import com.scsk.response.vo.AccountAppSeqResVO;
import com.scsk.response.vo.AccountApplicationApplyApiResVO;
import com.scsk.util.EncryptorUtil;
import com.scsk.util.GeologicalAppraisalIPUtli;
import com.scsk.util.PhoneNumberUtil;
import com.scsk.util.PushNotifications;
import com.scsk.util.TeleNumberUtil;

/**
 * 口座開設サービス
 * 
 * @author ylq
 */

@Service
public class AccountApplicationApplyApiService
extends
AbstractBLogic<AccountApplicationApplyApiReqVO, AccountApplicationApplyApiResVO> {

    @Autowired
    AccountAppSeqService accountAppSeqService;
    // 受付番号：　HJ-YYYYMM-########
    private final String ACCOUNTAPPSEQ = "0169A-%s-%s";
    // DOCタイプ
    private final String DOCTYPE = "ACCOUNTAPPLICATION";
    // DOCタイプ
    private final String DATEDICTYPE = "AGREEDOPERATIONDATE";
    // DOCタイプ
    private final String DOCTYPEIMAGE = "IMAGE";
    // 固定値 申込銀行
    private final String APPLICATIONBANK = "xxx";
    // 固定値 取引種類
    private final String TRANSACTIONTYPE = "3";
    // 固定値 ステータス
    private final String STATUS = "1";
    // 固定値 帳票出力回数
    private final int BILLOUTPUTCOUNT = 0;
    // 検索key
    private final String USERINFO = "userId:\"%s\"";
    // DOCタイプ ユーザー情報
    private final String USERDOCTYPE = "USERINFO";
    // 固定値Push未開封状況
    private final String PUSHSTATUSUNOPENED = "1";
    // 固定値Push配信失敗
    private final String PUSHSTATUSFAILURE = "3";
    // Push通知サクセス
    private final String SEND = "202";

    //
    private final List<String> APPLYSERVICE = Arrays.asList("1", "2");
    @Autowired
    private PushNotifications pushNotifications;
    @Autowired
    private RepositoryUtil repositoryUtil;
    @Autowired
    private EncryptorUtil encryptorUtil;

    @Override
    protected void preExecute(AccountApplicationApplyApiReqVO input)
            throws Exception {
    }

    @Override
    protected AccountApplicationApplyApiResVO doExecute(CloudantClient client,
            AccountApplicationApplyApiReqVO input) throws Exception {
        // データベースを取得
        Database db = client.database(Constants.DB_NAME, false);
        AccountApplicationApplyApiResVO output = new AccountApplicationApplyApiResVO();
        // システム日付　YYYYMMDD
        SimpleDateFormat applicationsdf = new SimpleDateFormat(Constants.DATE_FORMAT_YMD);
        String applicationDate = applicationsdf.format(new Date());
        // システム日付　yyyy/MM/dd
        SimpleDateFormat receiptsdf = new SimpleDateFormat(Constants.DATE_FORMAT_DB);
        String receiptDate = receiptsdf.format(new Date());
        // 配信日付 YYYYMMDDHHMISS
        SimpleDateFormat sdfPush = new SimpleDateFormat(Constants.DATE_FORMAT);
        String datePush = sdfPush.format(new Date());
        // 配信日付 yyyyMMddHHmmssSSS
        SimpleDateFormat sdfPushSSS = new SimpleDateFormat(Constants.DATE_FORMAT_ASS);
        String datePushSSS = sdfPushSSS.format(new Date());
        // 配信日付 yyyy年MM月dd日 HH:mm
        SimpleDateFormat sdfStatus = new SimpleDateFormat(Constants.DATE_SENDEMAIL_1);
        String statusDate = sdfStatus.format(new Date());
        // 検索日付
        SimpleDateFormat sdf2AppSeq = new SimpleDateFormat(Constants.DATE_FORMAT_APP);
        String dateAppSeq = sdf2AppSeq.format(new Date());
        // 1.受付番号採番
        // 1-1 受付番号のシーケンス取得
        AccountAppSeqReqVO input1 = new AccountAppSeqReqVO();
        input1.setUsetId(input.getUserId());

        AccountAppSeqResVO output1 = accountAppSeqService.execute(input1);
        // 1-2 受付番号生成
        String accountAppSeq = String.format(ACCOUNTAPPSEQ, dateAppSeq,
                output1.getAppSeq());
        // 2.口座申請情報をDBに保存する
        // 口座申請情報をDOC対象作成
        AccountAppDoc accountAppDoc = new AccountAppDoc();
        AccountAppImageDoc accountAppImageDoc = new AccountAppImageDoc();
        // 手動設定項目,暗号化
        accountAppDoc.setDocType(DOCTYPE);
        accountAppDoc.setUserId(encryptorUtil.encrypt(input.getUserId()));
        accountAppDoc.setUserType(input.getUserType());
        accountAppDoc.setDeviceTokenId(encryptorUtil.encrypt(input.getDeviceTokenId()));
        accountAppDoc.setLicenseId(encryptorUtil.encrypt(input.getLicenseId()));
        accountAppDoc.setLastName(encryptorUtil.encrypt(input.getLastName()));
        accountAppDoc.setFirstName(encryptorUtil.encrypt(input.getFirstName()));
        accountAppDoc.setKanaLastName(encryptorUtil.encrypt(input.getKanaLastName()));
        accountAppDoc.setKanaFirstName(encryptorUtil.encrypt(input.getKanaFirstName()));
        accountAppDoc.setAge(encryptorUtil.encrypt(input.getAge()));
        accountAppDoc.setBirthday(encryptorUtil.encrypt(input.getBirthday()));
        accountAppDoc.setSex(input.getSex());
        accountAppDoc.setPostCode(encryptorUtil.encrypt(input.getPostCode()));
        accountAppDoc.setAddress1(encryptorUtil.encrypt(input.getAddress1()));
        accountAppDoc.setAddress2(encryptorUtil.encrypt(input.getAddress2()));
        accountAppDoc.setAddress3(encryptorUtil.encrypt(input.getAddress3()));
        accountAppDoc.setAddress4(encryptorUtil.encrypt(input.getAddress4()));
        accountAppDoc.setKanaAddress(encryptorUtil.encrypt(input.getKanaAddress()));
        accountAppDoc.setTeleNumber(encryptorUtil.encrypt(input.getTeleNumber()));
        accountAppDoc.setWorkName(encryptorUtil.encrypt(input.getWorkName()));
        accountAppDoc.setWorkTeleNumber(encryptorUtil.encrypt(input.getWorkTeleNumber()));
        accountAppDoc.setPhoneNumber(encryptorUtil.encrypt(input.getPhoneNumber()));
        accountAppDoc.setApplicationBank(APPLICATIONBANK);
        accountAppDoc.setAccountAppSeq(accountAppSeq);
        accountAppDoc.setApplicationDate(applicationDate);
        accountAppDoc.setReceiptDate(receiptDate);
        accountAppDoc.setCardType(input.getCardType());
        accountAppDoc.setNoApplicationService(input.getNoApplicationService());
        // accountAppDoc.setGoodsAppointed(input.getGoodsAppointed());
        // accountAppDoc.setSpecificAccount(input.getSpecificAccount());
        // accountAppDoc.setCardLoans(input.getCardLoans());
        accountAppDoc.setTradingPurposes(input.getTradingPurposes());
        accountAppDoc.setOtherTradingPurposes(encryptorUtil.encrypt(input.getOtherTradingPurposes()));
        accountAppDoc.setOccupation(input.getOccupation());
        accountAppDoc.setOtherOccupations(encryptorUtil.encrypt(input.getOtherOccupations()));
        accountAppDoc.setSecurityPassword(encryptorUtil.encrypt(input.getSecurityPassword()));
        accountAppDoc.setTelRegisterPerTrans(input.getTelRegisterPerTrans());
        accountAppDoc.setTelRegisterPerDay(input.getTelRegisterPerDay());
        accountAppDoc.setTelOncePerTrans(input.getTelOncePerTrans());
        accountAppDoc.setTelOncePerDay(input.getTelOncePerDay());
        accountAppDoc.setInternetRegisterPerTrans(input.getInternetRegisterPerTrans());;
        accountAppDoc.setInternetRegisterPerDay(input.getInternetRegisterPerDay());
        accountAppDoc.setInternetOncePerTrans(input.getInternetOncePerTrans());
        accountAppDoc.setInternetOncePerDay(input.getInternetOncePerDay());
        accountAppDoc.setMobileRegisterPerTrans(0);
        accountAppDoc.setMobileRegisterPerDay(0);
        accountAppDoc.setMobileOncePerTrans(0);
        accountAppDoc.setMobileOncePerDay(0);
        accountAppDoc.setIdentificationType(input.getIdentificationType());
        String img_id = "";
        if ("A1".equals(input.getIdentificationType())) {
            try {
                accountAppImageDoc.setDocType(DOCTYPEIMAGE);
                accountAppImageDoc.setAccountAppSeq(accountAppSeq);
                accountAppImageDoc.setIdentificationImage(encryptorUtil.encrypt(input.getIdentificationImage()));
                accountAppImageDoc.setIdentificationImageBack(encryptorUtil.encrypt(input.getIdentificationImageBack()));
                accountAppImageDoc.setLivingConditionsImage(encryptorUtil.encrypt(input.getLivingConditionsImage()));
                img_id = repositoryUtil.saveToResultId(db, accountAppImageDoc);

            } catch (BusinessException e) {
                throw new BusinessException("");
            }
        } else {
            try {
                accountAppImageDoc.setDocType(DOCTYPEIMAGE);
                accountAppImageDoc.setAccountAppSeq(accountAppSeq);
                accountAppImageDoc.setIdentificationImage(encryptorUtil.encrypt(input.getIdentificationImage()));
                accountAppImageDoc.setLivingConditionsImage(encryptorUtil.encrypt(input.getLivingConditionsImage()));
                img_id = repositoryUtil.saveToResultId(db, accountAppImageDoc);
            } catch (BusinessException e) {
                throw new BusinessException("");
            }
        }
        accountAppDoc.setIdentificationImage(img_id);
        accountAppDoc.setLivingConditions(input.getLivingConditions());
        accountAppDoc.setLivingConditionsImage(img_id);
        accountAppDoc.setIpAddress(encryptorUtil.encrypt(input.getIpAddress()));
        accountAppDoc.setTransactionType(TRANSACTIONTYPE);
        accountAppDoc.setApplyService(APPLYSERVICE);
        accountAppDoc.setHoldAccount(input.getHoldAccount());
        accountAppDoc.setHoldAccountBank(encryptorUtil.encrypt(input.getHoldAccountBank()));
        accountAppDoc.setHoldAccountNumber(encryptorUtil.encrypt(input.getHoldAccountNumber()));
        accountAppDoc.setDirectServicesContract(input.getDirectServicesContract());
        accountAppDoc.setDirectServicesContractBank(encryptorUtil.encrypt(input.getDirectServicesContractBank()));
        accountAppDoc.setDirectServicesContractAccountNumber(encryptorUtil.encrypt(input.getDirectServicesContractAccountNumber()));
        accountAppDoc.setDirectServicesCardPassword(encryptorUtil.encrypt(input.getDirectServicesCardPassword()));
        accountAppDoc.setAccountAppMotive(input.getAccountAppMotive());
        accountAppDoc.setAccountAppOtherMotive(encryptorUtil.encrypt(input.getAccountAppOtherMotive()));
        accountAppDoc.setKnowProcess(input.getKnowProcess());
        accountAppDoc.setKnowOtherProcess(encryptorUtil.encrypt(input.getKnowOtherProcess()));
        accountAppDoc.setStatus(STATUS);
        accountAppDoc.setStoreCode(input.getStoreCode());
        accountAppDoc.setBillOutputCount(BILLOUTPUTCOUNT);
        // ＩＰアドレス鑑定
        try {

            GeologicalAppraisalIPDoc geologicalAppraisalIPDoc = GeologicalAppraisalIPUtli.GeologicalAppraisalIP(input.getIpAddress(), input.getPostCode());

            accountAppDoc.setIC_CountryCode(geologicalAppraisalIPDoc.getCountryCode());
            accountAppDoc.setIC_CountryName(geologicalAppraisalIPDoc.getCountryName());
            accountAppDoc.setIC_CountryThreat(geologicalAppraisalIPDoc.getCountryThreat());
            accountAppDoc.setIC_PSIP(geologicalAppraisalIPDoc.getPs_ip());
            accountAppDoc.setIC_Proxy(geologicalAppraisalIPDoc.getProxy());
            accountAppDoc.setIC_isMobile(geologicalAppraisalIPDoc.getIsMobile());
            accountAppDoc.setIC_Carrier(geologicalAppraisalIPDoc.getCarrier());
            accountAppDoc.setIC_CompanyName(geologicalAppraisalIPDoc.getCompanyName());
            accountAppDoc.setIC_CompanyDomain(geologicalAppraisalIPDoc.getCompanyDomain());
            accountAppDoc.setIC_CompanyCity(geologicalAppraisalIPDoc.getCompanyCity());
            accountAppDoc.setIC_Distance(geologicalAppraisalIPDoc.getDistance());
            accountAppDoc.setIC_IpThreat(geologicalAppraisalIPDoc.getThreat());
            accountAppDoc.setIC_SearchHistory(geologicalAppraisalIPDoc.getSearchHistory());

        } catch (Exception e) {
            // TODO: handle exception
        } // 完了

        // 自宅電話番号鑑定
        try {

            TeleNumberDoc teleNumberDoc = TeleNumberUtil.teleNumber(input.getTeleNumber());

            accountAppDoc.setTC_Access1(teleNumberDoc.getAccess1());
            accountAppDoc.setTC_Result1(teleNumberDoc.getResult1());
            accountAppDoc.setTC_Month1(teleNumberDoc.getMonth1());
            accountAppDoc.setTC_Movetel1(teleNumberDoc.getMovetel1());
            accountAppDoc.setTC_Carrier1(teleNumberDoc.getCarrier1());
            accountAppDoc.setTC_Count1(teleNumberDoc.getCount1());
            accountAppDoc.setTC_Attention1(teleNumberDoc.getAttention1());
            accountAppDoc.setTC_Tacsflag1(teleNumberDoc.getTacsflag1());
            accountAppDoc.setTC_Latestyear1(teleNumberDoc.getLatestyear1());
            accountAppDoc.setTC_Latestmonth1(teleNumberDoc.getLatestmonth1());
            accountAppDoc.setTC_F01_1(teleNumberDoc.getF01_1());
            accountAppDoc.setTC_F02_1(teleNumberDoc.getF02_1());
            accountAppDoc.setTC_F03_1(teleNumberDoc.getF03_1());
            accountAppDoc.setTC_F04_1(teleNumberDoc.getF04_1());
            accountAppDoc.setTC_F05_1(teleNumberDoc.getF05_1());
            accountAppDoc.setTC_F06_1(teleNumberDoc.getF06_1());
            accountAppDoc.setTC_F07_1(teleNumberDoc.getF07_1());
            accountAppDoc.setTC_F08_1(teleNumberDoc.getF08_1());
            accountAppDoc.setTC_F09_1(teleNumberDoc.getF09_1());
            accountAppDoc.setTC_F10_1(teleNumberDoc.getF10_1());
            accountAppDoc.setTC_F11_1(teleNumberDoc.getF11_1());
            accountAppDoc.setTC_F12_1(teleNumberDoc.getF12_1());
            accountAppDoc.setTC_F13_1(teleNumberDoc.getF13_1());
            accountAppDoc.setTC_F14_1(teleNumberDoc.getF14_1());
            accountAppDoc.setTC_F15_1(teleNumberDoc.getF15_1());
            accountAppDoc.setTC_F16_1(teleNumberDoc.getF16_1());
            accountAppDoc.setTC_F17_1(teleNumberDoc.getF17_1());
            accountAppDoc.setTC_F18_1(teleNumberDoc.getF18_1());
            accountAppDoc.setTC_F19_1(teleNumberDoc.getF19_1());
            accountAppDoc.setTC_F20_1(teleNumberDoc.getF20_1());
            accountAppDoc.setTC_F21_1(teleNumberDoc.getF21_1());
            accountAppDoc.setTC_F22_1(teleNumberDoc.getF22_1());
            accountAppDoc.setTC_F23_1(teleNumberDoc.getF23_1());
            accountAppDoc.setTC_F24_1(teleNumberDoc.getF24_1());

        } catch (Exception e) {
            // TODO: handle exception
        } // 完了

        // 自宅電話番号鑑定
        try {

            PhoneNumberDoc phoneNumberDoc = PhoneNumberUtil.phoneNumber(input.getPhoneNumber());

            accountAppDoc.setTC_Access2(phoneNumberDoc.getAccess2());
            accountAppDoc.setTC_Result2(phoneNumberDoc.getResult2());
            accountAppDoc.setTC_Month2(phoneNumberDoc.getMonth2());
            accountAppDoc.setTC_Movetel2(phoneNumberDoc.getMovetel2());
            accountAppDoc.setTC_Carrier2(phoneNumberDoc.getCarrier2());
            accountAppDoc.setTC_Count2(phoneNumberDoc.getCount2());
            accountAppDoc.setTC_Attention2(phoneNumberDoc.getAttention2());
            accountAppDoc.setTC_Tacsflag2(phoneNumberDoc.getTacsflag2());
            accountAppDoc.setTC_Latestyear2(phoneNumberDoc.getLatestyear2());
            accountAppDoc.setTC_Latestmonth2(phoneNumberDoc.getLatestmonth2());
            accountAppDoc.setTC_F01_2(phoneNumberDoc.getF01_2());
            accountAppDoc.setTC_F02_2(phoneNumberDoc.getF02_2());
            accountAppDoc.setTC_F03_2(phoneNumberDoc.getF03_2());
            accountAppDoc.setTC_F04_2(phoneNumberDoc.getF04_2());
            accountAppDoc.setTC_F05_2(phoneNumberDoc.getF05_2());
            accountAppDoc.setTC_F06_2(phoneNumberDoc.getF06_2());
            accountAppDoc.setTC_F07_2(phoneNumberDoc.getF07_2());
            accountAppDoc.setTC_F08_2(phoneNumberDoc.getF08_2());
            accountAppDoc.setTC_F09_2(phoneNumberDoc.getF09_2());
            accountAppDoc.setTC_F10_2(phoneNumberDoc.getF10_2());
            accountAppDoc.setTC_F11_2(phoneNumberDoc.getF11_2());
            accountAppDoc.setTC_F12_2(phoneNumberDoc.getF12_2());
            accountAppDoc.setTC_F13_2(phoneNumberDoc.getF13_2());
            accountAppDoc.setTC_F14_2(phoneNumberDoc.getF14_2());
            accountAppDoc.setTC_F15_2(phoneNumberDoc.getF15_2());
            accountAppDoc.setTC_F16_2(phoneNumberDoc.getF16_2());
            accountAppDoc.setTC_F17_2(phoneNumberDoc.getF17_2());
            accountAppDoc.setTC_F18_2(phoneNumberDoc.getF18_2());
            accountAppDoc.setTC_F19_2(phoneNumberDoc.getF19_2());
            accountAppDoc.setTC_F20_2(phoneNumberDoc.getF20_2());
            accountAppDoc.setTC_F21_2(phoneNumberDoc.getF21_2());
            accountAppDoc.setTC_F22_2(phoneNumberDoc.getF22_2());
            accountAppDoc.setTC_F23_2(phoneNumberDoc.getF23_2());
            accountAppDoc.setTC_F24_2(phoneNumberDoc.getF24_2());

        } catch (Exception e) {
            // TODO: handle exception
        } // 完了

        accountAppDoc.setAgreedOperationDate(input.getAgreedOperationDate());
        try {
            // 口座申請情報をDOC対象挿入
            repositoryUtil.save(db, accountAppDoc);

        } catch (BusinessException e) {
            // 失敗の場合：エラーとして処理終了
            throw new BusinessException("");
        }
        AccountAppAgreedOperationDateDoc accountAppAgreedOperationDateDoc = new AccountAppAgreedOperationDateDoc();
        accountAppAgreedOperationDateDoc.setDocType(DATEDICTYPE);
        accountAppAgreedOperationDateDoc.setUserId(encryptorUtil.encrypt(input.getUserId()));
        accountAppAgreedOperationDateDoc.setDeviceTokenId(encryptorUtil.encrypt(input.getDeviceTokenId()));
        accountAppAgreedOperationDateDoc.setUserType(input.getUserType());
        accountAppAgreedOperationDateDoc.setAgreedOperationDate(input.getAgreedOperationDate());
        try {
            repositoryUtil.save(db, accountAppAgreedOperationDateDoc);
        } catch (Exception e) {
            throw new BusinessException("");
        }
        // 3.正式ユーザ申請の場合、ユーザ情報を更新する。
        String ke = String.format(USERINFO,
                encryptorUtil.encrypt(input.getUserId()));
        // 3-1 ユーザIDでUSERINFO　Docへ検索
        List<UserInfoDoc> userInfoDocList = repositoryUtil.getIndex(db,ApplicationKeys.INSIGHTINDEX_ACCOUNTMNT_SEARCHBYUSERID_USERINFO,ke, UserInfoDoc.class);

		String applyInformation = Constants.RECEIPT_DATE + statusDate
				+ Constants.ACCOUNT_SEQ + accountAppSeq + Constants.APPLY_KIND;
		String push = Constants.PUSH_MESSAGE_STATUS_1 + applyInformation
				+ Constants.PUSH_MESSAGE_ABOUT;
        // 3-2 ユーザDOC対象更新
        // 検索結果が1件の場合
        if (userInfoDocList != null && userInfoDocList.size() == 1) {
            // 3-2-1 ユーザDOC対象編集,暗号化
            UserInfoDoc userInfoDoc = userInfoDocList.get(0);
            if (userInfoDoc.getUserType() == 1 || userInfoDoc.getUserType() == 2) {
                userInfoDoc.setDocType(USERDOCTYPE);
                userInfoDoc.setLastName(encryptorUtil.encrypt(input.getLastName()));
                userInfoDoc.setFirstName(encryptorUtil.encrypt(input.getFirstName()));
                userInfoDoc.setKanaLastName(encryptorUtil.encrypt(input.getKanaLastName()));
                userInfoDoc.setKanaFirstName(encryptorUtil.encrypt(input.getKanaFirstName()));
                userInfoDoc.setAge(encryptorUtil.encrypt(input.getAge()));
                List<String> occupation = input.getOccupation();
                List<String> occupationList = new ArrayList<String>();
                for (String string : occupation) {
                    occupationList.add(encryptorUtil.encrypt(string));
                }
                userInfoDoc.setOccupation(occupationList);
                userInfoDoc.setOtherOccupations(encryptorUtil.encrypt(input.getOtherOccupations()));
                userInfoDoc.setPhoneNumber(encryptorUtil.encrypt(input.getPhoneNumber()));
                userInfoDoc.setTeleNumber(encryptorUtil.encrypt(input.getTeleNumber()));
                userInfoDoc.setWorkTeleNumber(encryptorUtil.encrypt(input.getWorkTeleNumber()));
                userInfoDoc.setBirthday(encryptorUtil.encrypt(input.getBirthday()));
                userInfoDoc.setSex(input.getSex());
                userInfoDoc.setPostCode(encryptorUtil.encrypt(input.getPostCode()));
                userInfoDoc.setAddress1(encryptorUtil.encrypt(input.getAddress1()));
                userInfoDoc.setAddress2(encryptorUtil.encrypt(input.getAddress2()));
                userInfoDoc.setAddress3(encryptorUtil.encrypt(input.getAddress3()));
                userInfoDoc.setAddress4(encryptorUtil.encrypt(input.getAddress4()));
                userInfoDoc.setKanaAddress(encryptorUtil.encrypt(input.getKanaAddress()));
                userInfoDoc.setTradingPurposes(input.getTradingPurposes());

                try {
                    // 3-2-2 ユーザDOC対象更新
                    repositoryUtil.update(db, userInfoDoc);

                } catch (BusinessException e) {
                    // 失敗の場合：エラーとして処理終了
                    throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1012);
                }

                // 4-1 正式ユーザの場合を取得して配信を行う
                // 配信履歴Doc件数検索
                PushrecordDoc pushrecordDoc = new PushrecordDoc();

                List<PushrecordDoc> pushrecordDocSUM = new ArrayList<>();
                MapReduce viewSUM = new MapReduce();
                viewSUM.setMap("function (doc) {if(doc.docType && doc.docType === \"PUSHRECORD\" && doc.delFlg && doc.delFlg===\"0\" && doc.pushStatus ===\"1\" && doc.userId===\""
                        + accountAppDoc.getUserId()
                        + "\") {emit(doc._id, 1);}}");
                int sum = 1;

                pushrecordDocSUM = repositoryUtil.queryByDynamicView(db, viewSUM, PushrecordDoc.class);

                if (pushrecordDocSUM != null && pushrecordDocSUM.size() != 0) {
                    sum = pushrecordDocSUM.size() + 1;
                }

                List<String> list = userInfoDoc.getDeviceTokenIdList();
                List<String> list2 = new ArrayList<String>();
                for (String string : list) {
                    list2.add(encryptorUtil.decrypt(string));
                }

                // Push通知
                String send = pushNotifications.sendMessage(push, sum, list2);
                // 正式ユーザの場合、ログ‐アウトので、送信しない
                if (list.size() == 0 || list.isEmpty() || list.equals(null)) {
                    pushrecordDoc.setDocType(Constants.DOCTYPE_1);
                    pushrecordDoc.setAccountAppSeq(accountAppDoc.getAccountAppSeq());
                    pushrecordDoc.setUserId(accountAppDoc.getUserId());
                    pushrecordDoc.setDeviceTokenId(list);
                    pushrecordDoc.setPushDate("");
                    pushrecordDoc.setOpenDate("");
                    pushrecordDoc.setSaveDate(datePush);
                    pushrecordDoc.setPushContent("");
                    // 未配信
                    pushrecordDoc.setPushStatus("4");
                    pushrecordDoc.setStatus(STATUS);
                    repositoryUtil.save(db, pushrecordDoc);
                    // Push通知DOC
                } else {
                    // ４－１－１、配信
                    pushrecordDoc.setDocType(Constants.DOCTYPE_1);
                    pushrecordDoc.setAccountAppSeq(accountAppSeq);
                    pushrecordDoc.setUserId(accountAppDoc.getUserId());
                    pushrecordDoc.setDeviceTokenId(list);
                    pushrecordDoc.setPushDate(datePushSSS);
                    pushrecordDoc.setOpenDate("");
                    pushrecordDoc.setSaveDate(datePush);
                    pushrecordDoc.setPushContent(push);
                    pushrecordDoc.setStatus(STATUS);
                    // Push通知は成功を判断します
                    if (SEND.equals(send)) {
                        // 固定値Push未開封状況
                        pushrecordDoc.setPushStatus(PUSHSTATUSUNOPENED);
                    } else {
                        // 固定値Push配信失敗
                        pushrecordDoc.setPushStatus(PUSHSTATUSFAILURE);
                    }

                    // ４－１－２　配信履歴保存
                    repositoryUtil.save(db, pushrecordDoc);
                }
            }
        } else {
            // 4-2 仮ユーザの場合を取得して配信を行う
            List<String> list = new ArrayList<String>();

            list.add(encryptorUtil.decrypt(accountAppDoc.getDeviceTokenId()));
            List<PushrecordDoc> pushrecordDocSUM = new ArrayList<>();
            MapReduce viewSUM = new MapReduce();
            viewSUM.setMap("function (doc) {if(doc.docType && doc.docType === \"PUSHRECORD\" && doc.delFlg && doc.delFlg===\"0\" && doc.pushStatus ===\"1\"&& doc.userId===\""+ accountAppDoc.getUserId() + "\") {emit(doc._id, 1);}}");

            pushrecordDocSUM = repositoryUtil.queryByDynamicView(db, viewSUM, PushrecordDoc.class);

            int sum = 1;

            if (pushrecordDocSUM != null && pushrecordDocSUM.size() != 0) {
                sum = pushrecordDocSUM.size() + 1;
            }

            // Push通知
            String send = pushNotifications.sendMessage(push, sum, list);
            // Push通知DOC
            PushrecordDoc pushrecordDoc = new PushrecordDoc();
            // ４－２－１、配信
            pushrecordDoc.setDocType(Constants.DOCTYPE_1);
            pushrecordDoc.setAccountAppSeq(accountAppSeq);
            pushrecordDoc.setUserId(accountAppDoc.getUserId());
            List<String> tokenIdList=new ArrayList<>();
            tokenIdList.add(accountAppDoc.getDeviceTokenId());
            pushrecordDoc.setDeviceTokenId(tokenIdList);
            pushrecordDoc.setPushDate(datePushSSS);
            pushrecordDoc.setOpenDate("");
            pushrecordDoc.setSaveDate(datePush);
            pushrecordDoc.setPushContent(push);
            pushrecordDoc.setStatus(STATUS);
            // Push通知は成功を判断します
            if (SEND.equals(send)) {
                // 固定値Push未開封状況
                pushrecordDoc.setPushStatus(PUSHSTATUSUNOPENED);
            } else {
                // 固定値Push配信失敗
                pushrecordDoc.setPushStatus(PUSHSTATUSFAILURE);
            }

            // ４－2－２　配信履歴保存
            repositoryUtil.save(db, pushrecordDoc);
        }
        return output;
    }
}
