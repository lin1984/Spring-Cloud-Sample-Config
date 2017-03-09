package com.scsk.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.constants.ApplicationKeys;
import com.scsk.constants.Constants;
import com.scsk.constants.MessageKeys;
import com.scsk.exception.BusinessException;
import com.scsk.model.UserInfoDoc;
import com.scsk.model.UserInfoKeyDoc;
import com.scsk.request.vo.AccountMntFacebookRegistOrLoginApiReqVO;
import com.scsk.request.vo.userMigrationApiReqVO;
import com.scsk.response.vo.AccountMntFacebookRegistOrLoginApiResVO;
import com.scsk.util.EncryptorUtil;
import com.scsk.util.LogInfoUtil;
import com.scsk.util.Utils;

/**
 * facebook登録＆ログインサービス
 * 
 * @author ylq
 *
 */
@Service
public class AccountMntFacebookRegistOrLoginApiService
        extends AbstractBLogic<AccountMntFacebookRegistOrLoginApiReqVO, AccountMntFacebookRegistOrLoginApiResVO> {
    // 検索key
    private final String FACEBOOKID = "facebookId:\"%s\" AND userType:2";
    // ユーザID生成ルール
    private final String USERID = "F%s%s";
    // DOCタイプ ユーザー情報
    private final String DOCTYPEFORUSERINFO = "USERINFO";
    // DOCタイプ ユーザーキー
    private final String DOCTYPEFORUSERINFOKEY = "USERINFOKEY";
    // userType
    private final int USERTYPE = 2;
    
    @Autowired
    private userMigrationApiService userMigrationApiService;
    
    @Autowired
    private EncryptorUtil encryptorUtil;

    @Override
    protected void preExecute(AccountMntFacebookRegistOrLoginApiReqVO input) throws Exception {
    }

    @Override
    protected AccountMntFacebookRegistOrLoginApiResVO doExecute(CloudantClient client,
            AccountMntFacebookRegistOrLoginApiReqVO input) throws Exception {
        // データベースを取得
        Database db = client.database(Constants.DB_NAME, false);

        AccountMntFacebookRegistOrLoginApiResVO output = new AccountMntFacebookRegistOrLoginApiResVO();

        /** １、存在チェック **/
        // １－１、facebookIdでUSERINFO Docへ検索
        List<UserInfoDoc> userInfoDocList = repositoryUtil.getIndex(db,
                ApplicationKeys.INSIGHTINDEX_ACCOUNTMNT_SEARCHBYFACEBOOKID_USERINFO,
                String.format(FACEBOOKID, encryptorUtil.encrypt(input.getFacebookId())), UserInfoDoc.class);

        if (userInfoDocList != null && userInfoDocList.size() == 1) {
            // 結果が１件の場合、処理２へ
            /** ２、facebookログイン **/
            // 処理１で取得USERINFOを編集
            UserInfoDoc userInfoDoc = userInfoDocList.get(0);
            // 手動設定項目
            List<String> deviceTokenIdList = userInfoDoc.getDeviceTokenIdList();
            // deviceTokenIdListにdeviceTokenIdを含めて挿入しない
            if (Utils.isNotNullAndEmpty(input.getDeviceTokenId())
                    && !deviceTokenIdList.contains(input.getDeviceTokenId())) {
                // リクエストパラメータ.deviceTokenIdを端末ID配列に挿入,暗号化
                deviceTokenIdList.add(encryptorUtil.encrypt(input.getDeviceTokenId()));

            }
            //1
            userInfoDoc.setFacebookId(encryptorUtil.encrypt(input.getFacebookId()));
            userInfoDoc.setFacebookName(encryptorUtil.encrypt(input.getFacebookName()));
            userInfoDoc.setFacebookEmail(encryptorUtil.encrypt(input.getFacebookEmail()));
            userInfoDoc.setFacebookSex(input.getFacebookSex());
            userInfoDoc.setFacebookBirthday(encryptorUtil.encrypt(input.getFacebookBirthday()));
            userInfoDoc.setFacebookPhoneNumber(encryptorUtil.encrypt(input.getFacebookPhoneNumber()));
            userInfoDoc.setFacebookAddress(encryptorUtil.encrypt(input.getFacebookAddress()));

            /** 2－2、ユーザDOC対象更新 **/
            try {
                // 2－1で編集したユーザDOCをDBに更新する。
                repositoryUtil.update(db, userInfoDoc);
                // 処理１で取得USERINFOを編集,暗号解読
                output.setUserId(encryptorUtil.decrypt(userInfoDoc.getUserId()));
                output.setUserType(userInfoDoc.getUserType());
                output.setEmail(encryptorUtil.decrypt(userInfoDoc.getEmail()));
                output.setLastName(encryptorUtil.decrypt(userInfoDoc.getLastName()));
                output.setFirstName(encryptorUtil.decrypt(userInfoDoc.getFirstName()));
                output.setKanaLastName(encryptorUtil.decrypt(userInfoDoc.getKanaLastName()));
                output.setKanaFirstName(encryptorUtil.decrypt(userInfoDoc.getKanaFirstName()));
                output.setAge(encryptorUtil.decrypt(userInfoDoc.getAge()));
                List<String> occupation = userInfoDoc.getOccupation();
                List<String> occupationList = new ArrayList<String>();
                for (String string : occupation) {
                    occupationList.add(encryptorUtil.decrypt(string));
                }
                output.setOccupation(occupationList);
                output.setOtherOccupations(encryptorUtil.decrypt(userInfoDoc.getOtherOccupations()));
                output.setPhoneNumber(encryptorUtil.decrypt(userInfoDoc.getPhoneNumber()));
                output.setTeleNumber(encryptorUtil.decrypt(userInfoDoc.getTeleNumber()));
                output.setWorkName(encryptorUtil.decrypt(userInfoDoc.getWorkName()));
                output.setWorkTeleNumber(encryptorUtil.decrypt(userInfoDoc.getWorkTeleNumber()));
                output.setBirthday(encryptorUtil.decrypt(userInfoDoc.getBirthday()));
                output.setSex(userInfoDoc.getSex());
                output.setPostCode(encryptorUtil.decrypt(userInfoDoc.getPostCode()));
                output.setAddress1(encryptorUtil.decrypt(userInfoDoc.getAddress1()));
                output.setAddress2(encryptorUtil.decrypt(userInfoDoc.getAddress2()));
                output.setAddress3(encryptorUtil.decrypt(userInfoDoc.getAddress3()));
                output.setAddress4(encryptorUtil.decrypt(userInfoDoc.getAddress4()));
                output.setKanaAddress(encryptorUtil.decrypt(userInfoDoc.getKanaAddress()));
                //暗号解読List
                List<String> list = userInfoDoc.getCardNoList();
                List<String> cardNoList = new ArrayList<String>();
                for (String string : list) {
                    cardNoList.add(encryptorUtil.decrypt(string));
                }
                output.setCardNoList(cardNoList);
                output.setThemeList(userInfoDoc.getThemeList());
                // 成功の場合：正常として処理終了
                // すでにあるの場合、ログイン処理を行う
                output.setReslutCode(MessageKeys.I_ACCOUNTMNT_1006);
                return output;
            } catch (BusinessException e) {
                // 失敗の場合：エラーとして処理終了
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1010);
            }

        } else if (userInfoDocList != null && userInfoDocList.size() == 0) {

            // 検索結果が０件の場合、処理３へ
            /** 3、ユーザ新規 **/
            // USERINFOKEYDOC対象作成
            UserInfoKeyDoc userInfoKeyDoc = new UserInfoKeyDoc();
            //2
            userInfoKeyDoc.set_id(encryptorUtil.encrypt(input.getFacebookId()));
            userInfoKeyDoc.setDocType(DOCTYPEFORUSERINFOKEY);

            // ３－１、登録用キー（facebookId）がUSERINFOKEYに登録
            try {
                // DBへ挿入
                repositoryUtil.save(db, userInfoKeyDoc);
            } catch (BusinessException e) {
                // 挿入失敗の場合、エラーとして処理終了。
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1001);
            }

            // 挿入成功の場合、処理3-2へ
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
            String date = sdf.format(new Date());
            // 3－2、ユーザID生成
            String userId = String.format(USERID, date, input.getFacebookId());
            String userID = encryptorUtil.encrypt(userId);
            
            // USERINFO DOCを新規作成
            UserInfoDoc userInfoDoc = new UserInfoDoc();
            // 手動設定項目
            userInfoDoc.setDocType(DOCTYPEFORUSERINFO);
            userInfoDoc.setUserId(userID);
            userInfoDoc.setUserType(USERTYPE);
            if (Utils.isNotNullAndEmpty(input.getDeviceTokenId())) {
                List<String> deviceTokenIdList = new ArrayList<String>();
                deviceTokenIdList.add(encryptorUtil.encrypt(input.getDeviceTokenId()));
                userInfoDoc.setDeviceTokenIdList(deviceTokenIdList);
            }
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
            userInfoDoc.setWorkName(encryptorUtil.encrypt(input.getWorkName()));
            userInfoDoc.setWorkTeleNumber(encryptorUtil.encrypt(input.getWorkTeleNumber()));
            userInfoDoc.setBirthday(encryptorUtil.encrypt(input.getBirthday()));
            userInfoDoc.setSex(input.getSex());
            userInfoDoc.setPostCode(encryptorUtil.encrypt(input.getPostCode()));
            userInfoDoc.setAddress1(encryptorUtil.encrypt(input.getAddress1()));
            userInfoDoc.setAddress2(encryptorUtil.encrypt(input.getAddress2()));
            userInfoDoc.setAddress3(encryptorUtil.encrypt(input.getAddress3()));
            userInfoDoc.setAddress4(encryptorUtil.encrypt(input.getAddress4()));
            userInfoDoc.setKanaAddress(encryptorUtil.encrypt(input.getKanaAddress()));
            List<String> list = input.getCardNoList();
            List<String> cardNoList = new ArrayList<String>();
            for (String string : list) {
                cardNoList.add(encryptorUtil.encrypt(string));
            }
            userInfoDoc.setCardNoList(cardNoList);
            userInfoDoc.setThemeList(input.getThemeList());
            //3
            userInfoDoc.setFacebookId(encryptorUtil.encrypt(input.getFacebookId()));
            userInfoDoc.setFacebookName(encryptorUtil.encrypt(input.getFacebookName()));
            userInfoDoc.setFacebookEmail(encryptorUtil.encrypt(input.getFacebookEmail()));
            userInfoDoc.setFacebookSex(input.getFacebookSex());
            userInfoDoc.setFacebookBirthday(encryptorUtil.encrypt(input.getFacebookBirthday()));
            userInfoDoc.setFacebookPhoneNumber(encryptorUtil.encrypt(input.getFacebookPhoneNumber()));
            userInfoDoc.setFacebookAddress(encryptorUtil.encrypt(input.getFacebookAddress()));
            /** 3－3、ユーザDOC対象挿入 **/
            try {
                // 3－2で作成したユーザDOCをDBに更新する。
                repositoryUtil.save(db, userInfoDoc);
                String userid = encryptorUtil.decrypt(userID);
                try {
                    // ユーザ履歴データ転換　仮ユーザ　正式ユーザに変換します。
                    userMigrationApiReqVO input1 = new userMigrationApiReqVO();
                    input1.setOldUser(encryptorUtil.encrypt(input.getUserId()));
                    input1.setNewUser(encryptorUtil.encrypt(userId));
                    userMigrationApiService.execute(input1);
                } catch (Exception e) {
                    // ログ出力
                    LogInfoUtil.LogError("ユーザー履歴データ転換失敗しました。旧ユーザID："+ input.getUserId() + ", 新たなユーザID："+ userId, e);
                }
                output.setUserId(userid);
                output.setUserType(USERTYPE);
                // 成功の場合：正常として処理終了
                // FacebooIdがないの場合、登録処理を行う
                
                output.setReslutCode(MessageKeys.I_ACCOUNTMNT_1001);
                return output;
            } catch (BusinessException e) {
                // 失敗の場合：エラーとして処理終了
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1010);
            }
        } else {
            // 検索結果が１件以上の場合、エラーとして処理終了
            throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1010);
        }
    }
}
