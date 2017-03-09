package com.scsk.service;
/**
 * パスワード忘れ
 */
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
import com.scsk.request.vo.AccountMntaccountResetPasswordByEmailReqVO;
import com.scsk.response.vo.AccountMntaccountResetPasswordByEmailResVO;
import com.scsk.util.EncryptorUtil;
import com.scsk.util.PasswordUtils;
import com.scsk.util.RandomUtil;
import com.scsk.util.SendMail;

@Service
public class AccountMntaccountResetPasswordByEmaillApiService extends
        AbstractBLogic<AccountMntaccountResetPasswordByEmailReqVO, AccountMntaccountResetPasswordByEmailResVO> {

    // ユーザーIDの検索key
    private final String EMAIL = "email:\"%s\"";
    // 固定值
    private final String FIG = "1";

    @Autowired
    private EncryptorUtil encryptorUtil;
    @Autowired
    private SendMail sendMail;

    @Override
    protected void preExecute(AccountMntaccountResetPasswordByEmailReqVO input) throws Exception {
    }

    @Override
    protected AccountMntaccountResetPasswordByEmailResVO doExecute(CloudantClient client, AccountMntaccountResetPasswordByEmailReqVO input)
            throws Exception {
        // データベースを取得
        Database db = client.database(Constants.DB_NAME, false);

        // ユーザーIDkey
        String key = String.format(EMAIL, encryptorUtil.encrypt(input.getEmail()));

        // レスポンスをインスタンスします。
        AccountMntaccountResetPasswordByEmailResVO output = new AccountMntaccountResetPasswordByEmailResVO();

        // 検索ユーザーデータ
        List<UserInfoDoc> userInfoDocList = repositoryUtil.getIndex(db, ApplicationKeys.INSIGHTINDEX_ACCOUNTMNT_SERACHBYEMAIL_USERINFO, key, UserInfoDoc.class);

        // 検索結果が1件の場合
        if (userInfoDocList != null && userInfoDocList.size() == 1) {
            UserInfoDoc userInfoDoc = userInfoDocList.get(0);
            // ランダムに生成されたパスワード
            String password = RandomUtil.getStringRandom();
            String name = encryptorUtil.decrypt(userInfoDoc.getLastName()) + "" + encryptorUtil.decrypt(userInfoDoc.getFirstName());
            
            if ("".equals(name)) {
                name = encryptorUtil.decrypt(userInfoDoc.getEmail());
            }
            try {
                sendMail.sendMailForResetPassword(input.getEmail(), password, name);
            } catch (BusinessException e) {
                //メール送信失敗の場合、異常終了
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1030);
            }
            // 固定値を変更します
            userInfoDoc.setChangPasswordFlg(FIG);
            // ランダムに生成されたパスワード插入DB
            userInfoDoc.setPassword(PasswordUtils.passwordEncode(password));
            try {
                // リセットのパスワードをDBに更新します
                repositoryUtil.update(db, userInfoDoc);
                return output;
            } catch (BusinessException e) {
                // 更新失敗の場合、エラーとして処理終了。
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1030);
            }
        }else {
            // 検索結果が0件の場合、エラーとして処理終了。
            // 悪意検索を防ぐために、ここで正常終了とする。
            throw new BusinessException(MessageKeys.E_ACCOUNTMNT_9002);
        }
    }
}
