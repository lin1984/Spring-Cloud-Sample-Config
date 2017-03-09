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
import com.scsk.request.vo.AccountApplicationApplyApiReqVO;
import com.scsk.request.vo.AccountApplicationCheckPortSeatApiReqVO;
import com.scsk.response.vo.AccountApplicationApplyApiResVO;
import com.scsk.response.vo.AccountApplicationCheckPortSeatApiResVO;
import com.scsk.responseentity.vo.ResponseEntityVO;
import com.scsk.service.AccountApplicationApplyApiService;
import com.scsk.service.AccountApplicationCheckPortSeatApiService;
import com.scsk.util.CheckCommunicate;
import com.scsk.util.IPaddress;
import com.scsk.util.LogInfoUtil;
/**
 * 口座開設コントローラ
 * 
 * @author ylq
 *
 */
@Controller
@RequestMapping("/api/accountApplication")
public class AccountApplicationApiController {
    
    /**
     * 口座開設
     */
    @Autowired
    AccountApplicationApplyApiService accountApplicationApplyApiService;
    
    /**
     * 検査口座開設
     */
    @Autowired
    AccountApplicationCheckPortSeatApiService accountApplicationCheckPortSeatApiService;
    
    /**
     * 口座開設
     * 
     * @param input
     * @return ResponseEntity add by 姚立全
     */
    @RequestMapping(value = "/accountApplicationAccept", headers = { "authKey1", "authKey2", "authKey3", "appVer",
    "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AccountApplicationApplyApiResVO>> accountApplicationApply(
            @RequestBody AccountApplicationApplyApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS, HttpServletRequest request) {
        
        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AccountApplicationApplyApiResVO> entityBody = new ResponseEntityVO<AccountApplicationApplyApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AccountApplicationApplyApiResVO>>(entityBody, HttpStatus.OK);
        }
        
        try {
            input.setIpAddress(IPaddress.getIp());
            accountApplicationApplyApiService.execute(input);
            
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode("");
            LogInfoUtil.LogInfo(MessageKeys.I_ACCOUNTMNT_1001);
            // ボーディ設定
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
        return new ResponseEntity<ResponseEntityVO<AccountApplicationApplyApiResVO>>(entityBody, HttpStatus.OK);
    }
    /**
     * 検査口座開設
     * 
     * @param input
     * @return ResponseEntity add by 姚立全
     */
    @RequestMapping(value = "/checkPortSeat", headers = { "authKey1", "authKey2", "authKey3", "appVer",
    "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AccountApplicationCheckPortSeatApiResVO>> checkPortSeat(
            @RequestBody AccountApplicationCheckPortSeatApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS, HttpServletRequest request) {
        
        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AccountApplicationCheckPortSeatApiResVO> entityBody = new ResponseEntityVO<AccountApplicationCheckPortSeatApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AccountApplicationCheckPortSeatApiResVO>>(entityBody, HttpStatus.OK);
        }
        
        try {
            
            AccountApplicationCheckPortSeatApiResVO output = accountApplicationCheckPortSeatApiService.execute(input);
            
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode("");
            entityBody.setResultData(output);
            LogInfoUtil.LogInfo(MessageKeys.I_ACCOUNTMNT_1001);
            // ボーディ設定
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
        return new ResponseEntity<ResponseEntityVO<AccountApplicationCheckPortSeatApiResVO>>(entityBody, HttpStatus.OK);
    }


}
