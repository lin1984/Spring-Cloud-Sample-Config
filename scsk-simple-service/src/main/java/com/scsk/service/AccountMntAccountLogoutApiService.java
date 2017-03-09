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
import com.scsk.request.vo.AccountMntAccountLogoutApiReqVO;
import com.scsk.response.vo.AccountMntAccountLogoutApiResVO;
import com.scsk.util.EncryptorUtil;

/**
 * ログアウトサービス
 * 
 * @author ylq
 *
 */
@Service
public class AccountMntAccountLogoutApiService extends
        AbstractBLogic<AccountMntAccountLogoutApiReqVO, AccountMntAccountLogoutApiResVO> {

    // ユーザーIDの検索key
    private final String USERID = "userId:\"%s\"";
    
    //docType = USERINFO
    private final String USERINFO = "USERINFO";
    @Autowired
    private EncryptorUtil encryptorUtil;
    
    @Override
    protected void preExecute(AccountMntAccountLogoutApiReqVO input) throws Exception {
    }

    protected AccountMntAccountLogoutApiResVO doExecute(CloudantClient client, AccountMntAccountLogoutApiReqVO input) throws Exception {

        //dbを接続して。
        Database db = client.database(Constants.DB_NAME, false);
        
        //キーを設置します。
        String key = String.format(USERID, encryptorUtil.encrypt(input.getUserId()));
        
        //レスポンスをインスタンスします。
        AccountMntAccountLogoutApiResVO output = new AccountMntAccountLogoutApiResVO();
        
        //検索ユーザーデータ
        List<UserInfoDoc> userInfoDocList = repositoryUtil.getIndex(db, ApplicationKeys.INSIGHTINDEX_ACCOUNTMNT_SEARCHBYUSERID_USERINFO, key, UserInfoDoc.class);
        
        //検索結果が１件の場合、処理２へ、検索結果が１件以外の場合、エラーとして処理終了。
        if (userInfoDocList != null && userInfoDocList.size() == 1) {
            //インスタンス
            UserInfoDoc userInfoDoc = userInfoDocList.get(0);
            
            //ユーザーデータを設置します。
            userInfoDoc.setDocType(USERINFO);
            userInfoDoc.setUserId(encryptorUtil.encrypt(input.getUserId()));
            List<String> deviceTokenIdList = userInfoDoc.getDeviceTokenIdList();
            if (input.getDeviceTokenId() != null && !input.getDeviceTokenId().isEmpty()) {
                deviceTokenIdList.remove(encryptorUtil.encrypt(input.getDeviceTokenId()));
            }
            userInfoDoc.setDeviceTokenIdList(deviceTokenIdList);
            userInfoDoc.setFirstName(encryptorUtil.encrypt(input.getFirstName()));
            userInfoDoc.setLastName(encryptorUtil.encrypt(input.getLastName()));
            userInfoDoc.setKanaLastName(encryptorUtil.encrypt(input.getKanaLastName()));
            userInfoDoc.setKanaFirstName(encryptorUtil.encrypt(input.getKanaFirstName()));
            userInfoDoc.setAge(encryptorUtil.encrypt(input.getAge()));
            List<String> occupation = input.getOccupation();
            List<String> occupationList = new ArrayList<String>();
            for (String string : occupation) {
                occupationList.add(encryptorUtil.encrypt(string));
            }
            userInfoDoc.setOtherOccupations(encryptorUtil.encrypt(input.getOtherOccupations()));
            userInfoDoc.setOccupation(occupationList);
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
            try {
                //ユーザDOC対象更新
                repositoryUtil.update(db, userInfoDoc);
                
                // 更新成功の場合
                return output;
            } catch (BusinessException e) {
                // 更新失敗の場合、エラーとして処理終了。
                throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1012);
            }
        
        //検索結果が１件以外の場合、エラーとして処理終了
        } else {
            throw new BusinessException(MessageKeys.E_ACCOUNTMNT_1012);
        }
    }
}