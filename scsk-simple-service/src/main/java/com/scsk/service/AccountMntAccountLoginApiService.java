package com.scsk.service;

import java.util.ArrayList;
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
import com.scsk.request.vo.AccountMntAccountLoginApiReqVO;
import com.scsk.response.vo.AccountMntAccountLoginApiResVO;
import com.scsk.util.EncryptorUtil;
import com.scsk.util.PasswordUtils;
import com.scsk.util.Utils;

/**
 * メールアドレスのログインサービス
 * 
 * @author ylq
 *
 */
@Service
public class AccountMntAccountLoginApiService extends
        AbstractBLogic<AccountMntAccountLoginApiReqVO, AccountMntAccountLoginApiResVO> {

    // ユーザーIDの検索key
    private final String EMAIL = "email:\"%s\"";
    
    @Autowired
    private EncryptorUtil encryptorUtil;
    
    @Override
    protected void preExecute(AccountMntAccountLoginApiReqVO input) throws Exception {
    }

    protected AccountMntAccountLoginApiResVO doExecute(CloudantClient client, AccountMntAccountLoginApiReqVO input) throws Exception {

        // データベースを取得
        Database db = client.database(Constants.DB_NAME, false);

        // ユーザーIDkey
        String key = String.format(EMAIL, encryptorUtil.encrypt(input.getEmail()));

        // レスポンスをインスタンスします。
        AccountMntAccountLoginApiResVO output = new AccountMntAccountLoginApiResVO();

        // 検索ユーザーデータ
        List<UserInfoDoc> userInfoDocList = repositoryUtil.getIndex(db, ApplicationKeys.INSIGHTINDEX_ACCOUNTMNT_SERACHBYEMAIL_USERINFO, key, UserInfoDoc.class);

        // 検索結果が1件の場合
        if (userInfoDocList != null && userInfoDocList.size() == 1) {
            UserInfoDoc userInfoDoc = userInfoDocList.get(0);
            // DOC.password とリクエストパラメータは比較する
            if (PasswordUtils.passwordMatch(input.getPassword(), userInfoDoc.getPassword())) {
                // DOC編集
                List<String> list = userInfoDoc.getDeviceTokenIdList();
                
                // deviceTokenIdListにdeviceTokenIdを含めて挿入しない
                if (Utils.isNotNullAndEmpty(input.getDeviceTokenId()) && !list.contains(encryptorUtil.encrypt(input.getDeviceTokenId()))) {
                    // リクエストパラメータ.deviceTokenIdを端末ID配列に挿入,暗号化
                    list.add(encryptorUtil.encrypt(input.getDeviceTokenId()));
                    userInfoDoc.setDeviceTokenIdList(list);
                }

                try {
                    // 更新データ
                    repositoryUtil.update(db, userInfoDoc);
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
                    //
                    List<String> cardNo = userInfoDoc.getCardNoList();
                    List<String> cardNoList = new ArrayList<String>();
                    for (String string : cardNo) {
                        cardNoList.add(encryptorUtil.decrypt(string));
                    }
                    output.setCardNoList(cardNoList);
                    output.setThemeList(userInfoDoc.getThemeList());
                    output.setChangPasswordFlg(userInfoDoc.getChangPasswordFlg());
                    output.setFacebookId(userInfoDoc.getFacebookId());
                    
                    // 更新成功の場合、正常終了d
                    return output;
                } catch (BusinessException e) {
                    // 更新失敗の場合、エラーとして処理終了。
                    throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1002);
                }
            } else {
                // 比較NGの場合、エラーとして処理終了。
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_9001);
            }
        } else {
            // 検索0件の場合、エラーコード設定
            throw new BusinessException(MessageKeys.E_ACCOUNTMNT_9001);
        }
    }
}