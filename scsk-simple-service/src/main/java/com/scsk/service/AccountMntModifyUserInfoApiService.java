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
import com.scsk.request.vo.AccountMntModifyUserInfoApiReqVO;
import com.scsk.response.vo.AccountMntModifyUserInfoApiResVO;
import com.scsk.util.EncryptorUtil;

/**
 * お問い合わせサービス
 * 
 * @author ylq
 *
 */
@Service
public class AccountMntModifyUserInfoApiService extends
        AbstractBLogic<AccountMntModifyUserInfoApiReqVO, AccountMntModifyUserInfoApiResVO> {

    // ユーザーIDの検索key
    private final String USERID = "userId:\"%s\"";
    
    @Autowired
    private EncryptorUtil encryptorUtil;

    @Override
    protected void preExecute(AccountMntModifyUserInfoApiReqVO input) throws Exception {
    }

    protected AccountMntModifyUserInfoApiResVO doExecute(CloudantClient client, AccountMntModifyUserInfoApiReqVO input) throws Exception {

        // データベースを取得
        Database db = client.database(Constants.DB_NAME, false);

        // ユーザーIDkey
        String key = String.format(USERID, encryptorUtil.encrypt(input.getUserId()));

        // レスポンスをインスタンスします。
        AccountMntModifyUserInfoApiResVO output = new AccountMntModifyUserInfoApiResVO();

        // 検索ユーザーデータ
        List<UserInfoDoc> userInfoDocList = repositoryUtil.getIndex(db, ApplicationKeys.INSIGHTINDEX_ACCOUNTMNT_SEARCHBYUSERID_USERINFO, key, UserInfoDoc.class);

        // 検索結果が１件の場合
        if (userInfoDocList != null && userInfoDocList.size() == 1) {
            // ユーザDOC対象作成
            UserInfoDoc userInfoDoc = userInfoDocList.get(0);
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

            // 自動設定項目
            try {
                repositoryUtil.update(db, userInfoDoc);

                // 更新成功の場合、正常終了d
                return output;

            } catch (BusinessException e) {
                // 更新失敗の場合、エラーコード設定
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1011);
            }
        } else {
            // 検索0件の場合、エラーコード設定
            throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1009);
        }
    }
}