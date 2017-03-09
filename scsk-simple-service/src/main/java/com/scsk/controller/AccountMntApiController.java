package com.scsk.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scsk.constants.Constants;
import com.scsk.constants.MessageKeys;
import com.scsk.exception.BusinessException;
import com.scsk.request.vo.AccountMntAccountLoginApiReqVO;
import com.scsk.request.vo.AccountMntAccountLogoutApiReqVO;
import com.scsk.request.vo.AccountMntAccountRegistApiReqVO;
import com.scsk.request.vo.AccountMntChangeAccountEmailApiReqVO;
import com.scsk.request.vo.AccountMntFacebookRegistOrLoginApiReqVO;
import com.scsk.request.vo.AccountMntFacebookTogetherApiReqVO;
import com.scsk.request.vo.AccountMntLogoutSupplementApiReqVO;
import com.scsk.request.vo.AccountMntModifyUserInfoApiReqVO;
import com.scsk.request.vo.AccountMntUpdateUserPasswordApiReqVO;
import com.scsk.request.vo.AccountMntaccountResetPasswordByEmailReqVO;
import com.scsk.response.vo.AccountMntAccountLoginApiResVO;
import com.scsk.response.vo.AccountMntAccountLogoutApiResVO;
import com.scsk.response.vo.AccountMntAccountRegistApiResVO;
import com.scsk.response.vo.AccountMntChangeAccountEmailApiResVO;
import com.scsk.response.vo.AccountMntFacebookRegistOrLoginApiResVO;
import com.scsk.response.vo.AccountMntFacebookTogetherApiResVO;
import com.scsk.response.vo.AccountMntLogoutSupplementApiResVO;
import com.scsk.response.vo.AccountMntModifyUserInfoApiResVO;
import com.scsk.response.vo.AccountMntUpdateUserPasswordApiResVO;
import com.scsk.response.vo.AccountMntaccountResetPasswordByEmailResVO;
import com.scsk.responseentity.vo.ResponseEntityVO;
import com.scsk.service.AccountMntAccountLoginApiService;
import com.scsk.service.AccountMntAccountLogoutApiService;
import com.scsk.service.AccountMntAccountRegistApiService;
import com.scsk.service.AccountMntChangeAccountEmailApiService;
import com.scsk.service.AccountMntFacebookRegistOrLoginApiService;
import com.scsk.service.AccountMntFacebookTogetherApiService;
import com.scsk.service.AccountMntLogoutSupplementApiService;
import com.scsk.service.AccountMntModifyUserInfoApiService;
import com.scsk.service.AccountMntUpdateUserPasswordApiService;
import com.scsk.service.AccountMntaccountResetPasswordByEmaillApiService;
import com.scsk.util.CheckCommunicate;
import com.scsk.util.LogInfoUtil;

/**
 * アカウント設定コントローラ
 * 
 * @author ws
 *
 */
@RestController
@Controller
@RefreshScope
@RequestMapping("/api/accountMnt")
public class AccountMntApiController {

    /**
     * メールアドレスの登録
     */
    @Autowired
    AccountMntAccountRegistApiService accountResistApiService;

    /**
     * ユーザ情報変更
     */
    @Autowired
    AccountMntModifyUserInfoApiService accountMntModifyUserInfoApiService;

    /**
     * facebook登録＆ログイン
     */
    @Autowired
    AccountMntFacebookRegistOrLoginApiService accountMntFacebookRegistOrLoginApiService;

    /**
     * Facebook連携
     */
    @Autowired
    AccountMntFacebookTogetherApiService accountMntFacebookTogetherApiService;

    /**
     * パスワードを変更して。
     */
    @Autowired
    AccountMntUpdateUserPasswordApiService accountMntUpdateUserPasswordApiService;

    /**
     * ログアウト
     */
    @Autowired
    AccountMntAccountLogoutApiService accountMntAccountLogoutApiService;

    /**
     * メールアドレス変更
     */
    @Autowired
    AccountMntChangeAccountEmailApiService changeAccountEmailApiService;

    /**
     * メールアドレスのログイン
     */
    @Autowired
    AccountMntAccountLoginApiService accountMntAccountLoginApiService;

    /**
     * ログアウト補充
     */
    @Autowired
    AccountMntLogoutSupplementApiService accountMntLogoutSupplementApiService;

    /**
     * パスワード忘れ
     */
    @Autowired
    AccountMntaccountResetPasswordByEmaillApiService accountMntaccountResetPasswordByEmaillApiService;
    
    /**
     * メールアドレスの登録
     * 
     * @param input
     *            リクエストデータ
     * @return ResponseEntity レスポンスデータ
     */
    @RequestMapping(value = "/accountRegist", headers = { "authKey1", "authKey2", "authKey3", "appVer",
            "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntityVO<AccountMntAccountRegistApiResVO> accountRegist(HttpServletRequest request,
            @RequestBody AccountMntAccountRegistApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {

        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AccountMntAccountRegistApiResVO> entityBody = new ResponseEntityVO<AccountMntAccountRegistApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return entityBody;
        }

        try {
            AccountMntAccountRegistApiResVO output = accountResistApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode(MessageKeys.I_ACCOUNTMNT_1001);
            // ボーディ設定
            entityBody.setResultData(output);
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
        return entityBody;
    }

    /**
     * ユーザ情報変更
     * 
     * @param input
     * @return ResponseEntity add by 経天宇
     */
    @RequestMapping(value = "/modifyUserInfo", headers = { "authKey1", "authKey2", "authKey3", "appVer",
            "appOS" }, method = { RequestMethod.PUT,
                    RequestMethod.POST }, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AccountMntModifyUserInfoApiResVO>> modifyUserInfo(
            @RequestBody AccountMntModifyUserInfoApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {
        
        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AccountMntModifyUserInfoApiResVO> entityBody = new ResponseEntityVO<AccountMntModifyUserInfoApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AccountMntModifyUserInfoApiResVO>>(entityBody, HttpStatus.OK);
        }


        try {
            accountMntModifyUserInfoApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode(MessageKeys.I_ACCOUNTMNT_1005);
            LogInfoUtil.LogInfo(MessageKeys.I_ACCOUNTMNT_1005);
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
        return new ResponseEntity<ResponseEntityVO<AccountMntModifyUserInfoApiResVO>>(entityBody, HttpStatus.OK);
    }

    /**
     * facebook登録＆ログイン
     * 
     * @param req
     * @param res
     * @param model
     * @return
     */
    @RequestMapping(value = "/facebookRegistOrLogin", headers = { "authKey1", "authKey2", "authKey3", "appVer",
            "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AccountMntFacebookRegistOrLoginApiResVO>> facebookRegistOrLogin(
            @RequestBody AccountMntFacebookRegistOrLoginApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {
        
        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AccountMntFacebookRegistOrLoginApiResVO> entityBody = new ResponseEntityVO<AccountMntFacebookRegistOrLoginApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AccountMntFacebookRegistOrLoginApiResVO>>(entityBody, HttpStatus.OK);
        }

        // インスタンス
        try {
            AccountMntFacebookRegistOrLoginApiResVO output = accountMntFacebookRegistOrLoginApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            // ボーディ設定
            entityBody.setResultData(output);
            // メッセージ設定
            entityBody.setResultCode(output.getReslutCode());
            LogInfoUtil.LogInfo("");
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
        return new ResponseEntity<ResponseEntityVO<AccountMntFacebookRegistOrLoginApiResVO>>(entityBody, HttpStatus.OK);
    }

    /**
     * Facebook連携
     * 
     * @param input
     * @return ResponseEntity add by 経天宇
     */
    @RequestMapping(value = "/facebookTogether", headers = { "authKey1", "authKey2", "authKey3", "appVer",
            "appOS" }, method = { RequestMethod.PUT,
                    RequestMethod.POST }, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AccountMntFacebookTogetherApiResVO>> facebookTogether(
            @RequestBody AccountMntFacebookTogetherApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {
        
        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AccountMntFacebookTogetherApiResVO> entityBody = new ResponseEntityVO<AccountMntFacebookTogetherApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AccountMntFacebookTogetherApiResVO>>(entityBody, HttpStatus.OK);
        }

        // インスタンス ResponseEntityVO

        try {
            accountMntFacebookTogetherApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode("");
            LogInfoUtil.LogInfo("");
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
        return new ResponseEntity<ResponseEntityVO<AccountMntFacebookTogetherApiResVO>>(entityBody, HttpStatus.OK);

    }

    /**
     * パスワード変更
     * 
     * @param input
     * @return ResponseEntity add by 経天宇
     */
    @RequestMapping(value = "/updateUserPassword", headers = { "authKey1", "authKey2", "authKey3", "appVer",
            "appOS" }, method = { RequestMethod.PUT,
                    RequestMethod.POST }, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AccountMntUpdateUserPasswordApiResVO>> updateUserPassword(
            @RequestBody AccountMntUpdateUserPasswordApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {
        
        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AccountMntUpdateUserPasswordApiResVO> entityBody = new ResponseEntityVO<AccountMntUpdateUserPasswordApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AccountMntUpdateUserPasswordApiResVO>>(entityBody, HttpStatus.OK);
        }

        // インスタンス ResponseEntityVO

        try {
            accountMntUpdateUserPasswordApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode(MessageKeys.I_ACCOUNTMNT_1008);
            LogInfoUtil.LogInfo(MessageKeys.I_ACCOUNTMNT_1008);
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
        return new ResponseEntity<ResponseEntityVO<AccountMntUpdateUserPasswordApiResVO>>(entityBody, HttpStatus.OK);
    }

    /**
     * ログアウト
     * 
     * @param input
     * @return ResponseEntity add by 経天宇
     */
    @RequestMapping(value = "/accountLogout", headers = { "authKey1", "authKey2", "authKey3", "appVer",
            "appOS" }, method = { RequestMethod.PUT,
                    RequestMethod.POST }, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AccountMntAccountLogoutApiResVO>> accountLogout(
            @RequestBody AccountMntAccountLogoutApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {
        
        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AccountMntAccountLogoutApiResVO> entityBody = new ResponseEntityVO<AccountMntAccountLogoutApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.I_ACCOUNTMNT_1008);
            return new ResponseEntity<ResponseEntityVO<AccountMntAccountLogoutApiResVO>>(entityBody, HttpStatus.OK);
        }

        // インスタンス ResponseEntityVO

        try {
            accountMntAccountLogoutApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode(MessageKeys.I_ACCOUNTMNT_1007);
            LogInfoUtil.LogInfo(MessageKeys.I_ACCOUNTMNT_1007);
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
        return new ResponseEntity<ResponseEntityVO<AccountMntAccountLogoutApiResVO>>(entityBody, HttpStatus.OK);
    }

    /**
     * メールアドレス変更
     * 
     * @param input
     *            リクエストデータ
     * @return ResponseEntity レスポンスデータ
     */
    @RequestMapping(value = "/changeAccountEmail", headers = { "authKey1", "authKey2", "authKey3", "appVer",
            "appOS" }, method = { RequestMethod.PUT,
                    RequestMethod.POST }, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AccountMntChangeAccountEmailApiResVO>> changeAccountEmail(
            @RequestBody AccountMntChangeAccountEmailApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {
        
        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AccountMntChangeAccountEmailApiResVO> entityBody = new ResponseEntityVO<AccountMntChangeAccountEmailApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AccountMntChangeAccountEmailApiResVO>>(entityBody, HttpStatus.OK);
        }

        // インスタンス ResponseEntityVO

        try {
            changeAccountEmailApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode(MessageKeys.I_ACCOUNTMNT_1003);
            LogInfoUtil.LogInfo(MessageKeys.I_ACCOUNTMNT_1003);
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
        return new ResponseEntity<ResponseEntityVO<AccountMntChangeAccountEmailApiResVO>>(entityBody, HttpStatus.OK);
    }

    /**
     * メールアドレスのログイン
     * 
     * @param input
     * @return ResponseEntity add by 経天宇
     */
    @RequestMapping(value = "/accountLogin", headers = { "authKey1", "authKey2", "authKey3", "appVer",
            "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AccountMntAccountLoginApiResVO>> accountLogin(
            @RequestBody AccountMntAccountLoginApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {
        
        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AccountMntAccountLoginApiResVO> entityBody = new ResponseEntityVO<AccountMntAccountLoginApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AccountMntAccountLoginApiResVO>>(entityBody, HttpStatus.OK);
        }

        // インスタンス ResponseEntityVO

        try {
            AccountMntAccountLoginApiResVO output = accountMntAccountLoginApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode(MessageKeys.I_ACCOUNTMNT_1002);
            LogInfoUtil.LogInfo(MessageKeys.I_ACCOUNTMNT_1002);
            // ボーディ設定
            entityBody.setResultData(output);
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
        return new ResponseEntity<ResponseEntityVO<AccountMntAccountLoginApiResVO>>(entityBody, HttpStatus.OK);
    }

    /**
     * ログアウト補充
     * 
     * @param input
     * @return ResponseEntity add by 姚立全
     */
    @RequestMapping(value = "/logoutSupplement", headers = { "authKey1", "authKey2", "authKey3", "appVer",
            "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AccountMntLogoutSupplementApiResVO>> logoutSupplement(
            @RequestBody AccountMntLogoutSupplementApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {
        
        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AccountMntLogoutSupplementApiResVO> entityBody = new ResponseEntityVO<AccountMntLogoutSupplementApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AccountMntLogoutSupplementApiResVO>>(entityBody, HttpStatus.OK);
        }

        // インスタンス ResponseEntityVO

        try {
            accountMntLogoutSupplementApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode("");
            LogInfoUtil.LogInfo("");

        } catch (BusinessException e) {
            LogInfoUtil.LogInfo(e.getErrorCode());
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
        } catch (Exception e) {
            LogInfoUtil.LogError(e.getMessage(), e);
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            // TODO
            entityBody.setErrorCode("");
        }
        
        LogInfoUtil.LogInfoEnd(this.getClass().getSimpleName());
        return new ResponseEntity<ResponseEntityVO<AccountMntLogoutSupplementApiResVO>>(entityBody, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/accountResetPasswordByEmail", headers = { "authKey1", "authKey2", "authKey3", "appVer", "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AccountMntaccountResetPasswordByEmailResVO>> accountResetPasswordByEmail(
            @RequestBody AccountMntaccountResetPasswordByEmailReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {

        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AccountMntaccountResetPasswordByEmailResVO> entityBody = new ResponseEntityVO<AccountMntaccountResetPasswordByEmailResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AccountMntaccountResetPasswordByEmailResVO>>(entityBody, HttpStatus.OK);
        }

        // インスタンス ResponseEntityVO

        try {
            accountMntaccountResetPasswordByEmaillApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode(MessageKeys.I_ACCOUNTMNT_1009);
            LogInfoUtil.LogInfo(MessageKeys.I_ACCOUNTMNT_1009);

        } catch (BusinessException e) {
            // ヘッダ設定
            LogInfoUtil.LogInfo(e.getErrorCode());
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setErrorCode(e.getErrorCode());
        } catch (Exception e) {
            LogInfoUtil.LogError(e.getMessage(), e);
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            // TODO
            entityBody.setErrorCode("");
        }
        LogInfoUtil.LogInfoEnd(this.getClass().getSimpleName());
        return new ResponseEntity<ResponseEntityVO<AccountMntaccountResetPasswordByEmailResVO>>(entityBody, HttpStatus.OK);
    }
}