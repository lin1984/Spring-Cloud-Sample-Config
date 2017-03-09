package com.scsk.controller;

/**
 * Agent転送
 * 
 * @author ylq
 */
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
import com.scsk.request.vo.AssistantChatApiReqVO;
import com.scsk.request.vo.AssistantChatCohesionApiReqVO;
import com.scsk.request.vo.AssistantGetTheLatestPushApiReqVO;
import com.scsk.request.vo.AssistantUnderRefreshApiReqVO;
import com.scsk.response.vo.AssistantChatApiResVO;
import com.scsk.response.vo.AssistantChatCohesionApiResVO;
import com.scsk.response.vo.AssistantGetTheLatestPushApiResVO;
import com.scsk.response.vo.AssistantUnderRefreshApiResVO;
import com.scsk.responseentity.vo.ResponseEntityVO;
import com.scsk.service.AssistantChatApiService;
import com.scsk.service.AssistantChatCohesionApiService;
import com.scsk.service.AssistantGetTheLatestPushApiService;
import com.scsk.service.AssistantUnderRefreshApiSrevice;
import com.scsk.util.CheckCommunicate;
import com.scsk.util.LogInfoUtil;

@RestController
@Controller
@RefreshScope
@RequestMapping("/api/assistant")
public class AssistantChatApiController {

    /**
     * 自動応答フリーテキスト
     */
    @Autowired
    AssistantChatApiService assistantChatApiService;

    /**
     * Agentドロップダウンリフレッシュ
     */
    @Autowired
    AssistantUnderRefreshApiSrevice assistantUnderRefreshApiSrevice;

    /**
     * 担当接続
     */
    @Autowired
    AssistantChatCohesionApiService assistantChatCohesionApiService;

    /**
     * 最新のプッシュを取得
     */
    @Autowired
    AssistantGetTheLatestPushApiService assistantGetTheLatestPushApiService;

    /**
     * Agent自動応答フリーテキスト
     * 
     * @param input
     *            リクエストデータ
     * @return ResponseEntity レスポンスデータ
     */
    @RequestMapping(value = "/assistantChat", headers = { "authKey1", "authKey2", "authKey3", "appVer",
    "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AssistantChatApiResVO>> assistantChat(HttpServletRequest request,
            @RequestBody AssistantChatApiReqVO input, @RequestHeader String authKey1, @RequestHeader String authKey2,
            @RequestHeader String authKey3, @RequestHeader String appVer, @RequestHeader String appOS) {

        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AssistantChatApiResVO> entityBody = new ResponseEntityVO<AssistantChatApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AssistantChatApiResVO>>(entityBody, HttpStatus.OK);
        }

        try {
            AssistantChatApiResVO output = assistantChatApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode("");
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
        return new ResponseEntity<ResponseEntityVO<AssistantChatApiResVO>>(entityBody, HttpStatus.OK);
    }

    /**
     * Agentドロップダウンリフレッシュ
     * 
     * @param input
     *            リクエストデータ
     * @return ResponseEntity レスポンスデータ
     */
    @RequestMapping(value = "/nderRefresh", headers = { "authKey1", "authKey2", "authKey3", "appVer",
    "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AssistantUnderRefreshApiResVO>> nderRefresh(HttpServletRequest request,
            @RequestBody AssistantUnderRefreshApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {

        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AssistantUnderRefreshApiResVO> entityBody = new ResponseEntityVO<AssistantUnderRefreshApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AssistantUnderRefreshApiResVO>>(entityBody, HttpStatus.OK);
        }

        try {
            AssistantUnderRefreshApiResVO output = assistantUnderRefreshApiSrevice.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode("");
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
        return new ResponseEntity<ResponseEntityVO<AssistantUnderRefreshApiResVO>>(entityBody, HttpStatus.OK);
    }

    /**
     * 最新のプッシュを取得
     * 
     * @param input
     *            リクエストデータ
     * @return ResponseEntity レスポンスデータ
     */
    
    @RequestMapping(value = "/assistantChatCohesion", headers = { "authKey1", "authKey2", "authKey3", "appVer",
    "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AssistantChatCohesionApiResVO>> nderRefresh(HttpServletRequest request,
            @RequestBody AssistantChatCohesionApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {

        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AssistantChatCohesionApiResVO> entityBody = new ResponseEntityVO<AssistantChatCohesionApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AssistantChatCohesionApiResVO>>(entityBody, HttpStatus.OK);
        }

        try {
            AssistantChatCohesionApiResVO output = assistantChatCohesionApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode("");
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
        return new ResponseEntity<ResponseEntityVO<AssistantChatCohesionApiResVO>>(entityBody, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/getTheLatestPush", headers = { "authKey1", "authKey2", "authKey3", "appVer",
    "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<AssistantGetTheLatestPushApiResVO>> nderRefresh(HttpServletRequest request,
            @RequestBody AssistantGetTheLatestPushApiReqVO input, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS) {

        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        ResponseEntityVO<AssistantGetTheLatestPushApiResVO> entityBody = new ResponseEntityVO<AssistantGetTheLatestPushApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<AssistantGetTheLatestPushApiResVO>>(entityBody, HttpStatus.OK);
        }

        try {
            AssistantGetTheLatestPushApiResVO output = assistantGetTheLatestPushApiService.execute(input);
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode("");
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
        return new ResponseEntity<ResponseEntityVO<AssistantGetTheLatestPushApiResVO>>(entityBody, HttpStatus.OK);
    }
}
