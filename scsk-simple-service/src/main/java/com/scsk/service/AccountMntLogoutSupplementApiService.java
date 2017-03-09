package com.scsk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.constants.ApplicationKeys;
import com.scsk.constants.Constants;
import com.scsk.exception.BusinessException;
import com.scsk.model.UserInfoDoc;
import com.scsk.request.vo.AccountMntLogoutSupplementApiReqVO;
import com.scsk.response.vo.AccountMntLogoutSupplementApiResVO;
import com.scsk.util.EncryptorUtil;
import com.scsk.util.Utils;

/**
 * ログアウト補充サービス
 * 
 * @author ylq
 *
 */
@Service
public class AccountMntLogoutSupplementApiService extends AbstractBLogic<AccountMntLogoutSupplementApiReqVO, AccountMntLogoutSupplementApiResVO>{

    //ユーザーIDの検索key
    private final String USERID = "userId:\"%s\"";
    
    @Autowired
    private EncryptorUtil encryptorUtil;
    
    @Override
    protected void preExecute(AccountMntLogoutSupplementApiReqVO input) throws Exception {
    }
    

    protected AccountMntLogoutSupplementApiResVO doExecute(CloudantClient client,
            AccountMntLogoutSupplementApiReqVO input) throws Exception {
        //dbを接続して。
        Database db = client.database(Constants.DB_NAME, false);
        
        //キーを設置します。
        String key = String.format(USERID, encryptorUtil.encrypt(input.getUserId()));
        
        //レスポンスをインスタンスします。
        AccountMntLogoutSupplementApiResVO output = new AccountMntLogoutSupplementApiResVO();
        
        //検索ユーザーデータ
        List<UserInfoDoc> userInfoDocList = repositoryUtil.getIndex(db, ApplicationKeys.INSIGHTINDEX_ACCOUNTMNT_SEARCHBYUSERID_USERINFO, key, UserInfoDoc.class);
        
        //結果が１件，検索結果が１件以外の場合、エラーとして処理終了
        if (userInfoDocList != null && userInfoDocList.size() == 1) {
          
            //インスタンス
            UserInfoDoc userInfoDoc = userInfoDocList.get(0);
          
            //検索ユーザーデータ
            List<String> deviceTokenIdList = userInfoDoc.getDeviceTokenIdList();
           
            if (Utils.isNotNullAndEmpty(input.getDeviceTokenId())){
                //暗号化
                deviceTokenIdList.remove(encryptorUtil.encrypt(input.getDeviceTokenId()));
            }
            
            try {
                
                //1－1で作成したユーザDOCをDBに更新する。
                repositoryUtil.update(db, userInfoDoc);
             
                // 更新成功の場合
                return output;
            } catch (BusinessException e) {
             
                // 更新失敗の場合、エラーとして処理終了
                throw new BusinessException("");
            }
        }else {
            
            //結果が0件以外の場合、エラーとして処理終了
            throw new BusinessException("");
        }
    }

}
