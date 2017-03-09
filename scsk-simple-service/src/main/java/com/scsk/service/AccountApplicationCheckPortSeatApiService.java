package com.scsk.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.constants.ApplicationKeys;
import com.scsk.constants.Constants;
import com.scsk.model.AccountAppDoc;
import com.scsk.model.UserInfoDoc;
import com.scsk.request.vo.AccountApplicationCheckPortSeatApiReqVO;
import com.scsk.response.vo.AccountApplicationCheckPortSeatApiResVO;
import com.scsk.util.EncryptorUtil;
@Service
public class AccountApplicationCheckPortSeatApiService extends AbstractBLogic<AccountApplicationCheckPortSeatApiReqVO, AccountApplicationCheckPortSeatApiResVO>{

    // 検索key
    private final String SEARCHBYUSERID = "userId:\"%s\"";
    // 検索key
    private final String SEARCHBYACCOUNT = "userId:\"%s\"";
    
    @Autowired
    private EncryptorUtil encryptorUtil;


    @Override
    protected void preExecute(AccountApplicationCheckPortSeatApiReqVO input) throws Exception {
    }

    @Override
    protected AccountApplicationCheckPortSeatApiResVO doExecute(CloudantClient client,
            AccountApplicationCheckPortSeatApiReqVO input) throws Exception {
        Database db = client.database(Constants.DB_NAME, false);

        AccountApplicationCheckPortSeatApiResVO output = new AccountApplicationCheckPortSeatApiResVO();
        // １－１、ユーザIDでUSERINFO　Docへ検索
        List<UserInfoDoc> userInfoDocList = repositoryUtil.getIndex(db,
                ApplicationKeys.INSIGHTINDEX_ACCOUNTMNT_SEARCHBYUSERID_USERINFO,
                String.format(SEARCHBYUSERID, encryptorUtil.encrypt(input.getUserId())), UserInfoDoc.class);
        
        List<AccountAppDoc> accountappDocList = repositoryUtil.getIndex(db, 
                ApplicationKeys.INSIGHTINDEX_ACCOUNTAPP_SEARCHBYUSERID_ACCOUNTAPPLICATION,
                String.format(SEARCHBYACCOUNT, encryptorUtil.encrypt(input.getUserId())), AccountAppDoc.class);

        
        int userInfoDocSize = 0;
        int accountappDocSize = accountappDocList.size();
        if (accountappDocSize > 0) {

            output.setFlag(1);
            return output;
        } else if (accountappDocSize == 0) {
            if (userInfoDocList != null && userInfoDocList.size() == 1) {
                UserInfoDoc userInfoDoc = userInfoDocList.get(0);
                userInfoDocSize = userInfoDoc.getAccountAppCount();
                int checkPortSeatSize = accountappDocSize + userInfoDocSize;
                if (checkPortSeatSize == 0) {
                    // FLAG　無
                    output.setFlag(0);
                    return output;

                }else {
                    // FLAG 有り
                    output.setFlag(1);
                    return output;
                }
            }else {
                // FLAG 無
                output.setFlag(0);
                return output;
            }
        }

        return output;
    }
}
