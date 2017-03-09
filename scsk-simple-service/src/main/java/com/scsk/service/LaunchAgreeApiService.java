package com.scsk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.constants.Constants;
import com.scsk.exception.BusinessException;
import com.scsk.model.LaunchAgreeApiDoc;
import com.scsk.request.vo.LaunchAgreeApiReqVO;
import com.scsk.response.vo.LaunchAgreeApiResVO;
import com.scsk.util.EncryptorUtil;

/**
 * 同意
 * 
 * @author ylq
 *
 */
@Service
public class LaunchAgreeApiService extends AbstractBLogic<LaunchAgreeApiReqVO, LaunchAgreeApiResVO>{

    // DOCタイプ 
    private final String LANUCHAGREE = "LANUCHAGREE";
    
    @Autowired
    private EncryptorUtil encryptorUtil;
    
    @Override
    protected void preExecute(LaunchAgreeApiReqVO input) throws Exception {
    }

    @Override
    protected LaunchAgreeApiResVO doExecute(CloudantClient client, LaunchAgreeApiReqVO input) throws Exception {
        // データベースを取得
        Database db = client.database(Constants.DB_NAME, false);
        LaunchAgreeApiDoc launchAgreeApiDoc = new LaunchAgreeApiDoc();
        launchAgreeApiDoc.setDocType(LANUCHAGREE);
        launchAgreeApiDoc.setUserId(encryptorUtil.encrypt(input.getUserId()));
        launchAgreeApiDoc.setDeviceTokenId(encryptorUtil.encrypt(input.getDeviceTokenId()));
        try {
            repositoryUtil.save(db, launchAgreeApiDoc);
        } catch (BusinessException e) {
            //挿入失敗の場合、エラーとして処理終了
            throw new BusinessException("");
        }
        return null;
    }

}
