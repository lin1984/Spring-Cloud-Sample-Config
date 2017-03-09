package com.scsk.service;

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
import com.scsk.request.vo.AccountMntFacebookTogetherApiReqVO;
import com.scsk.response.vo.AccountMntFacebookTogetherApiResVO;
import com.scsk.util.EncryptorUtil;

/**
 * Facebook連携サービス
 * 
 * @author ylq
 *
 */
@Service
public class AccountMntFacebookTogetherApiService extends
        AbstractBLogic<AccountMntFacebookTogetherApiReqVO, AccountMntFacebookTogetherApiResVO> {

    // メールの検索key
    private final String USERID = "userId:\"%s\"";
    
    @Autowired
    private EncryptorUtil encryptorUtil;

    @Override
    protected void preExecute(AccountMntFacebookTogetherApiReqVO input) throws Exception {
    }

    @Override
    protected AccountMntFacebookTogetherApiResVO doExecute(CloudantClient client,
            AccountMntFacebookTogetherApiReqVO input) throws Exception {

        // dbを接続して。
        Database db = client.database(Constants.DB_NAME, false);

        // キーを設置します。
        String key = String.format(USERID, encryptorUtil.encrypt(input.getUserId()));

        // レスポンスをインスタンスします。
        AccountMntFacebookTogetherApiResVO output = new AccountMntFacebookTogetherApiResVO();

        // 検索ユーザーデータ
        List<UserInfoDoc> userInfoDocList = repositoryUtil.getIndex(db, ApplicationKeys.INSIGHTINDEX_ACCOUNTMNT_SEARCHBYUSERID_USERINFO, key, UserInfoDoc.class);

        // 結果が１件
        if (userInfoDocList != null && userInfoDocList.size() == 1) {

            // ユーザDOC対象作成
            UserInfoDoc userInfoDoc = userInfoDocList.get(0);
            //暗号化
            userInfoDoc.setFacebookId(encryptorUtil.encrypt(input.getFacebookId()));
            userInfoDoc.setFacebookName(encryptorUtil.encrypt(input.getFacebookName()));
            userInfoDoc.setFacebookEmail(encryptorUtil.encrypt(input.getFacebookEmail()));
            userInfoDoc.setFacebookSex(input.getFacebookSex());
            userInfoDoc.setFacebookBirthday(encryptorUtil.encrypt(input.getFacebookBirthday()));
            userInfoDoc.setFacebookPhoneNumber(encryptorUtil.encrypt(input.getFacebookPhoneNumber()));
            userInfoDoc.setFacebookAddress(encryptorUtil.encrypt(input.getFacebookAddress()));

            try {
                // ユーザDOC対象更新
                repositoryUtil.update(db, userInfoDoc);

                // 更新成功の場合、正常終了d
                return output;
            } catch (BusinessException e) {
                // エラーとして処理終了
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1010);
            }

        } else {
            // 結果が0件
            throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1010);
        }

    }

}
