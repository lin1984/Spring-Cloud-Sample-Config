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
import com.scsk.request.vo.AccountMntUpdateUserPasswordApiReqVO;
import com.scsk.response.vo.AccountMntUpdateUserPasswordApiResVO;
import com.scsk.util.EncryptorUtil;
import com.scsk.util.PasswordUtils;

/**
 * パスワード変更サービス
 * 
 * @author ws
 *
 */
@Service
public class AccountMntUpdateUserPasswordApiService extends
        AbstractBLogic<AccountMntUpdateUserPasswordApiReqVO, AccountMntUpdateUserPasswordApiResVO> {

    // ユーザーIDの検索key
    private final String USERID = "userId:\"%s\"";
    // 固定值
    private final String FLG = "0";
    
    @Autowired
    private EncryptorUtil encryptorUtil;

    //パスワードデコード
    //private 

    @Override
    protected void preExecute(AccountMntUpdateUserPasswordApiReqVO input) throws Exception {
    }

    protected AccountMntUpdateUserPasswordApiResVO doExecute(CloudantClient client, AccountMntUpdateUserPasswordApiReqVO input) throws Exception {
        
        //dbを接続して。
        Database db = client.database(Constants.DB_NAME, false);
        
        //キーを設置します。
        String key = String.format(USERID, encryptorUtil.encrypt(input.getUserId()));
        
        //レスポンスをインスタンスします。
        AccountMntUpdateUserPasswordApiResVO output = new AccountMntUpdateUserPasswordApiResVO();
        
        //検索ユーザーデータ
        List<UserInfoDoc> userInfoDocList = repositoryUtil.getIndex(db, ApplicationKeys.INSIGHTINDEX_ACCOUNTMNT_SEARCHBYUSERID_USERINFO, key, UserInfoDoc.class);
        
        //検索のユーザー存在判断
        if (userInfoDocList != null && userInfoDocList.size() == 1) {
            
            //ユーザーDOCデータを取ります
            UserInfoDoc userInfoDoc = userInfoDocList.get(0);
            
            //DOC.password とリクエストパラメータは比較する
            if (PasswordUtils.passwordMatch(input.getPassword(), userInfoDoc.getPassword())) {
                
                    userInfoDoc.setChangPasswordFlg(FLG);
                  //検索の結果に新パスワードを設置します。
                    userInfoDoc.setPassword(PasswordUtils.passwordEncode(input.getNewPassword()));
                    try {
                        
                        //ユーザDOC対象更新
                        repositoryUtil.update(db, userInfoDoc);
                        
                        // 更新成功の場合
                        return output;
                    } catch (BusinessException e) {
                        // 更新失敗のG合、エラーとして処理終了。
                        throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1005);
                    }
                
            }else {
                //比較NGの場合、エラーとして処理終了。
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1004);
            }
            
        //検索結果が0件の場合、エラーとして処理終了。
        } else {
            throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1004);
        }
    }
}