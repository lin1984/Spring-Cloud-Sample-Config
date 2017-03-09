package com.scsk.controller;

import javax.servlet.http.HttpServletRequest;
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
import com.scsk.request.vo.LaunchAgreeApiReqVO;
import com.scsk.response.vo.LaunchAgreeApiResVO;
import com.scsk.responseentity.vo.ResponseEntityVO;
import com.scsk.service.LaunchAgreeApiService;
import com.scsk.util.CheckCommunicate;
import com.scsk.util.LogInfoUtil;

/**
 * 同意
 * @author ylq
 *
 */
@Controller
@RequestMapping("/api/launch")
public class LaunchAgreeApiController {
    
    /**
     * 同意
     */
    @Autowired
    LaunchAgreeApiService launchAgreeApiService;
    
    /**
     * 同意
     * 
     * @param input
     *            リクエストデータ
     * @return ResponseEntity レスポンスデータ
     */
    @RequestMapping(value = "/agree", headers = { "authKey1", "authKey2", "authKey3", "appVer",
            "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<LaunchAgreeApiResVO>> accountRegist(HttpServletRequest request,
            @RequestBody LaunchAgreeApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {

        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<LaunchAgreeApiResVO> entityBody = new ResponseEntityVO<LaunchAgreeApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<LaunchAgreeApiResVO>>(entityBody, HttpStatus.OK);
        }

        try {
            launchAgreeApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode(MessageKeys.I_ACCOUNTMNT_1001);
            // ボーディ設定
            LogInfoUtil.LogInfo(MessageKeys.I_ACCOUNTMNT_1001);
        } catch (BusinessException e) {
            LogInfoUtil.LogInfo(e.getErrorCode());
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setErrorCode(e.getErrorCode());
        } catch (Exception e) {
            LogInfoUtil.LogError(e.getMessage(), e);
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            // TODO
            entityBody.setErrorCode("");
        }
        LogInfoUtil.LogInfoEnd(this.getClass().getSimpleName());
        return new ResponseEntity<ResponseEntityVO<LaunchAgreeApiResVO>>(entityBody, HttpStatus.OK);
    }
}
