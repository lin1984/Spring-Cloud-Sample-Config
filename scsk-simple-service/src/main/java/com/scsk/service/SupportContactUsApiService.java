package com.scsk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.constants.Constants;
import com.scsk.constants.MessageKeys;
import com.scsk.exception.BusinessException;
import com.scsk.model.ContactUsDoc;
import com.scsk.request.vo.SupportContactUsApiReqVO;
import com.scsk.response.vo.SupportContactUsApiResVO;
import com.scsk.util.EncryptorUtil;

@Service
public class SupportContactUsApiService extends AbstractBLogic<SupportContactUsApiReqVO, SupportContactUsApiResVO> {

    // docType = CONTACTUS
    private final String DOCTYPE = "CONTACTUS";
    @Autowired
    private EncryptorUtil encryptorUtil;

    @Override
    protected void preExecute(SupportContactUsApiReqVO input) throws Exception {
    }

    @Override
    protected SupportContactUsApiResVO doExecute(CloudantClient client, SupportContactUsApiReqVO input) throws Exception {
        //dbを接続して。
        Database db = client.database(Constants.DB_NAME, false);
        
        // レスポンスをインスタンスします。
        SupportContactUsApiResVO output = new SupportContactUsApiResVO();

        // 配信DOC対象作成
        ContactUsDoc contactUsDoc = new ContactUsDoc();
        contactUsDoc.setDocType(DOCTYPE);
        contactUsDoc.setUserId(encryptorUtil.encrypt(input.getUserId()));
        if (input.getDeviceTokenId() != null && !input.getDeviceTokenId().isEmpty()) {
            contactUsDoc.setDeviceTokenId(encryptorUtil.encrypt(input.getDeviceTokenId()));
        }
        contactUsDoc.setContactUs(input.getContactUs());
        try {
            // 配信DOC対象挿入
            repositoryUtil.save(db, contactUsDoc);

            // 更新成功の場合
            return output;
        } catch (BusinessException e) {
            // 更新失敗の場合、エラーとして処理終了。
            throw new BusinessException(MessageKeys.E_SUPPORT_1001);
        }
    }

}
