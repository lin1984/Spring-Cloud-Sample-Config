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
import com.scsk.request.vo.AccountMntAccountRegistApiReqVO;
import com.scsk.request.vo.userMigrationApiReqVO;
import com.scsk.response.vo.AccountMntAccountRegistApiResVO;
import com.scsk.util.EncryptorUtil;
import com.scsk.util.LogInfoUtil;
import com.scsk.util.PasswordUtils;
import com.scsk.util.SendMail;
import com.scsk.util.Utils;

/**
 * メールアドレスの登録サービス
 * 
 * @author ws
 *
 */
@Service
public class AccountMntAccountRegistApiService extends
        AbstractBLogic<AccountMntAccountRegistApiReqVO, AccountMntAccountRegistApiResVO> {
    // DOCタイプ ユーザーキー
    private final String DOCTYPEFORUSERINFOKEY = "USERINFOKEY";
    // 検索key
    private final String USERINFO = "userId:\"%s\"";
    // ユーザID生成ルール
    private final String USERID = "M%s%s";
    // DOCタイプ ユーザー情報
    private final String DOCTYPEFORUSERINFO = "USERINFO";
    // ユーザータイプ
    private final int USERTYPE = 1;
    @Autowired
    private userMigrationApiService userMigrationApiService;
    
    @Autowired
    private EncryptorUtil encryptorUtil;
    @Autowired
    private SendMail sendMail;

    @Override
    protected void preExecute(AccountMntAccountRegistApiReqVO input) throws Exception {
    }

    @Override
    protected AccountMntAccountRegistApiResVO doExecute(CloudantClient client, AccountMntAccountRegistApiReqVO input) throws Exception {
        Database db = client.database(Constants.DB_NAME, false);
        AccountMntAccountRegistApiResVO output = new AccountMntAccountRegistApiResVO();
        /** １、登録用キー（メール）がUSERINFOKEYに登録 **/
        // USERINFOKEYDOC対象作成
        UserInfoKeyDoc userInfoKeyDoc = new UserInfoKeyDoc();
        // １－１、メールアドレスをUSERINFOKEYの _idに設定して DBへ挿入,暗号化
        userInfoKeyDoc.set_id(encryptorUtil.encrypt(input.getEmail()));
        userInfoKeyDoc.setDocType(DOCTYPEFORUSERINFOKEY);

        
        try {
            // DBへ挿入
            repositoryUtil.save(db, userInfoKeyDoc);
            //// 挿入成功の場合、処理追加対応：１－２
        } catch (BusinessException e) {
            //挿入失敗の場合、エラーとして処理終了
            throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1001);
        }

        //  １－２、リクエストパラメータ　userIdが空白以外の場合、 
        if (Utils.isNotNullAndEmpty(input.getUserId()) && input.getUserType() == 2) {

            String key = String.format(USERINFO, encryptorUtil.encrypt(input.getUserId()));
            // ユーザIDでUSERINFO　Docへ検索
            List<UserInfoDoc> userInfoDocList = repositoryUtil.getIndex(db,
                    ApplicationKeys.INSIGHTINDEX_ACCOUNTMNT_SEARCHBYUSERID_USERINFO, key, UserInfoDoc.class);
            // 検索結果が1件以外の場合
            if (userInfoDocList != null && userInfoDocList.size() != 1) {
                //検索結果が1件以外の場合、エラーとして処理終了。
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1002);
            }
            UserInfoDoc userInfoDoc = userInfoDocList.get(0);
            // メールアドレス、パスワード更新を行う,暗号化
            userInfoDoc.setEmail(encryptorUtil.encrypt(input.getEmail()));
            userInfoDoc.setPassword(PasswordUtils.passwordEncode(input.getPassword()));
            String name = encryptorUtil.decrypt(userInfoDoc.getLastName()) + encryptorUtil.decrypt(userInfoDoc.getFirstName());
            
            if (name != null) {
                name = encryptorUtil.decrypt(userInfoDoc.getEmail());
            }
            
            try {
                // DBへ挿入 更新
                repositoryUtil.update(db, userInfoDoc);
                sendMail.sendMailForRegist(encryptorUtil.decrypt(userInfoDoc.getEmail()), name);
                // ボーディ設定暗号解読
                output.setUserId(encryptorUtil.decrypt(userInfoDoc.getUserId()));
                // 更新成功の場合：正常として処理終了
                return output;
            } catch (BusinessException e) {
                // 更新失敗の場合、エラーとして処理終了。
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1002);
            }

        } else {
            //userId空白の場合、ユーザ作成
            /** ２、ユーザ作成 **/
            // システム日時を取得
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
            String date = sdf.format(new Date());
            // ２－１、ユーザID生成
            String userId = String.format(USERID, date, input.getEmail());
            
            // ２－２、ユーザDOC対象作成
            UserInfoDoc userInfoDoc = new UserInfoDoc();
            // 手動設定項目,暗号化
            userInfoDoc.setDocType(DOCTYPEFORUSERINFO);
            userInfoDoc.setUserId(encryptorUtil.encrypt(userId));
            userInfoDoc.setUserType(USERTYPE);
            if (Utils.isNotNullAndEmpty(input.getDeviceTokenId())) {
                userInfoDoc.getDeviceTokenIdList().add(encryptorUtil.encrypt(input.getDeviceTokenId()));
            }
            userInfoDoc.setEmail(encryptorUtil.encrypt(input.getEmail()));
            userInfoDoc.setLastName(encryptorUtil.encrypt(input.getLastName()));
            userInfoDoc.setFirstName(encryptorUtil.encrypt(input.getFirstName()));
            userInfoDoc.setKanaLastName(encryptorUtil.encrypt(input.getKanaLastName()));
            userInfoDoc.setKanaFirstName(encryptorUtil.encrypt(input.getKanaFirstName()));
            userInfoDoc.setAge(encryptorUtil.encrypt(input.getAge()));
            List<String> listocc = input.getOccupation();
            List<String> occupationList = new ArrayList<String>();
            for (String string : listocc) {
                occupationList.add(encryptorUtil.encrypt(string));
            }
            userInfoDoc.setOccupation(occupationList);
            userInfoDoc.setOtherOccupations(encryptorUtil.encrypt(input.getOtherOccupations()));
            userInfoDoc.setPassword(PasswordUtils.passwordEncode(input.getPassword()));
            userInfoDoc.setPhoneNumber(encryptorUtil.encrypt(input.getPhoneNumber()));
            userInfoDoc.setTeleNumber(encryptorUtil.encrypt(input.getTeleNumber()));
            userInfoDoc.setWorkName(input.getWorkName());
            userInfoDoc.setWorkTeleNumber(encryptorUtil.encrypt(input.getWorkTeleNumber()));
            userInfoDoc.setBirthday(encryptorUtil.encrypt(input.getBirthday()));
            userInfoDoc.setSex(input.getSex());
            userInfoDoc.setPostCode(encryptorUtil.encrypt(input.getPostCode()));
            userInfoDoc.setAddress1(encryptorUtil.encrypt(input.getAddress1()));
            userInfoDoc.setAddress2(encryptorUtil.encrypt(input.getAddress2()));
            userInfoDoc.setAddress3(encryptorUtil.encrypt(input.getAddress3()));
            userInfoDoc.setAddress4(encryptorUtil.encrypt(input.getAddress4()));
            userInfoDoc.setKanaAddress(encryptorUtil.encrypt(input.getKanaAddress()));
            //暗号化
            List<String> list = input.getCardNoList();
            List<String> cardNoList = new ArrayList<String>();
            for (String string : list) {
                cardNoList.add(encryptorUtil.encrypt(string));
            }
            userInfoDoc.setCardNoList(cardNoList);
            userInfoDoc.setThemeList(input.getThemeList());

            String name = encryptorUtil.decrypt(userInfoDoc.getLastName()) + "" + encryptorUtil.decrypt(userInfoDoc.getFirstName());
            
            if ("".equals(name)) {
                name = encryptorUtil.decrypt(userInfoDoc.getEmail());
            }
            try {
                // ２－３、ユーザDOC対象挿入
                repositoryUtil.save(db, userInfoDoc);
                sendMail.sendMailForRegist(encryptorUtil.decrypt(userInfoDoc.getEmail()), name);
                
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
                // ボーディ設定
                output.setUserId(userId);
                // 成功の場合：正常として処理終了
                return output;
            } catch (BusinessException e) {
                // 失敗の場合：エラーとして処理終了
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1002);
            }
        }
    }
}