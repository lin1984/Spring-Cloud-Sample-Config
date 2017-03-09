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
import com.scsk.model.UserInfoKeyDoc;
import com.scsk.request.vo.AccountMntChangeAccountEmailApiReqVO;
import com.scsk.response.vo.AccountMntChangeAccountEmailApiResVO;
import com.scsk.util.EncryptorUtil;
import com.scsk.util.PasswordUtils;
import com.scsk.util.SendMail;

/**
 * メールアドレス変更サービス
 * 
 * @author ws
 *
 */
@Service
public class AccountMntChangeAccountEmailApiService extends
        AbstractBLogic<AccountMntChangeAccountEmailApiReqVO, AccountMntChangeAccountEmailApiResVO> {
    // 検索key
    private final String SEARCHBYUSERID = "userId:\"%s\"";

    // DOCタイプ ユーザーキー
    private final String DOCTYPEFORUSERINFOKEY = "USERINFOKEY";
    
    @Autowired
    private EncryptorUtil encryptorUtil;
    @Autowired
    private SendMail sendMail;
    
    @Override
    protected void preExecute(AccountMntChangeAccountEmailApiReqVO input) throws Exception {
        
    }

    @Override
    protected AccountMntChangeAccountEmailApiResVO doExecute(CloudantClient client,
            AccountMntChangeAccountEmailApiReqVO input) throws Exception {
        Database db = client.database(Constants.DB_NAME, false);

        AccountMntChangeAccountEmailApiResVO output = new AccountMntChangeAccountEmailApiResVO();
        
        /** １、ユーザー存在チェック **/
        // １－１、ユーザIDでUSERINFO　Docへ検索
        List<UserInfoDoc> UserInfoDocList = repositoryUtil.getIndex(db,
                ApplicationKeys.INSIGHTINDEX_ACCOUNTMNT_SEARCHBYUSERID_USERINFO,
                String.format(SEARCHBYUSERID, encryptorUtil.encrypt(input.getUserId())), UserInfoDoc.class);

        // 検索結果が1件以外の場合、エラーとして処理終了。
        if (UserInfoDocList != null && UserInfoDocList.size() != 1) {
            throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1007);
        }

        // ユーザー情報取得
        UserInfoDoc userInfoDoc = UserInfoDocList.get(0);
        // 古いメール取得
        String oldEmail = userInfoDoc.getEmail();

        // 検索結果が1件の場合、処理１－2へ
        // １－2、パスワードが一致するかどうかをチェック
        if (!PasswordUtils.passwordMatch(input.getPassword(), userInfoDoc.getPassword())) {
            throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1008);
        }

        // 検証結果OKの場合、正常として処理２へ。
        /** ２、登録用キー（メール）がUSERINFOKEYに登録 **/
        // USERINFOKEYDOC対象作成
        UserInfoKeyDoc userInfoKeyDoc = new UserInfoKeyDoc();
        // １－１、メールアドレスをUSERINFOKEYの _idに設定して
        userInfoKeyDoc.set_id(encryptorUtil.encrypt(input.getNewEmail()));
        userInfoKeyDoc.setDocType(DOCTYPEFORUSERINFOKEY);

        try {
            // DBへ挿入
            repositoryUtil.save(db, userInfoKeyDoc); // 挿入成功の場合、処理２－２へ
        } catch (BusinessException e) {
            // 挿入失敗の場合、エラーとして処理終了。
            throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1001);
        }

        /** ２－２、メールアドレス変更 **/
        // １－１で取得したUSERINFOを編集
        //暗号化
        userInfoDoc.setEmail(encryptorUtil.encrypt(input.getNewEmail()));
        String name = encryptorUtil.decrypt(userInfoDoc.getLastName()) + "" + encryptorUtil.decrypt(userInfoDoc.getFirstName());
        
        if ("".equals(name)) {
            name = encryptorUtil.decrypt(userInfoDoc.getEmail());
        }
        sendMail.sendMailForChangeMail(input.getNewEmail(), name);

        try {
            repositoryUtil.update(db, userInfoDoc);
        } catch (BusinessException e) {
            // 更新失敗の場合、エラーとして処理終了。
            throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1007);
        }

        /** ２－３、更新前のemailをUSERINFOKEYから削除 **/
        repositoryUtil.removeByDocId(db, oldEmail);

        return output;
    }
}