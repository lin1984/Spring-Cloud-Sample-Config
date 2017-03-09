package com.scsk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scsk.constants.Constants;
import com.scsk.constants.MessageKeys;
import com.scsk.exception.BusinessException;
import com.scsk.request.vo.SupportContactUsApiReqVO;
import com.scsk.response.vo.SupportContactUsApiResVO;
import com.scsk.responseentity.vo.ResponseEntityVO;
import com.scsk.service.SupportContactUsApiService;
import com.scsk.util.CheckCommunicate;
import com.scsk.util.LogInfoUtil;


@Controller
@RequestMapping("/api/support")
public class SupportApiController {

    /**
     * ユーザ情報変更
     */
    @Autowired
    SupportContactUsApiService supportContactUsApiService;

    /**
     * お問い合わせ
     * 
     * @param input
     * @return ResponseEntity
     * add by 経天宇
     */
    @RequestMapping(value = "/contactUs", headers = { "authKey1", "authKey2", "authKey3", "appVer",
    "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<SupportContactUsApiResVO>> contactUs(
            @RequestBody SupportContactUsApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {
        
        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<SupportContactUsApiResVO> entityBody = new ResponseEntityVO<SupportContactUsApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(this.getClass().getSimpleName());
            return new ResponseEntity<ResponseEntityVO<SupportContactUsApiResVO>>(entityBody, HttpStatus.OK);
        }
        
        try {
            supportContactUsApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode(MessageKeys.I_SUPPORT_1001);
        } catch (BusinessException e) {
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setErrorCode(e.getErrorCode());
        } catch (Exception e) {
            LogInfoUtil.LogError(e.getMessage(),e);
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            // TODO
            entityBody.setErrorCode("");
        }
        LogInfoUtil.LogInfoEnd(this.getClass().getSimpleName());
        return new ResponseEntity<ResponseEntityVO<SupportContactUsApiResVO>>(entityBody, HttpStatus.OK);
    }
}
