package com.scsk.controller;
/**
 * 
 * バージョン更新
 * @author ylq
 *
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
import com.scsk.request.vo.VersionApiReqVO;
import com.scsk.response.vo.VersionApiResVO;
import com.scsk.responseentity.vo.ResponseEntityVO;
import com.scsk.service.VersionApiSrevice;
import com.scsk.util.CheckCommunicate;
import com.scsk.util.LogInfoUtil;

@RestController
@Controller
@RefreshScope
@RequestMapping("/api/version")
public class VersionApiController {
    /**
     * メンテナンス
     */
    @Autowired
    VersionApiSrevice versionApiSrevice;
    
    /**
     * メンテナンス
     * 
     * @param input
     *            リクエストデータ
     * @return ResponseEntity レスポンスデータ
     */
    @RequestMapping(value = "/version", headers = { "authKey1", "authKey2", "authKey3", "appVer",
    "appOS" }, method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ResponseEntityVO<VersionApiResVO>> assistantChat(HttpServletRequest request,
            @RequestBody VersionApiReqVO input, @RequestHeader String authKey1, @RequestHeader String authKey2,
            @RequestHeader String authKey3, @RequestHeader String appVer, @RequestHeader String appOS) {

        LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
        VersionApiResVO output = new VersionApiResVO();
        ResponseEntityVO<VersionApiResVO> entityBody = new ResponseEntityVO<VersionApiResVO>();
        if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<VersionApiResVO>>(entityBody, HttpStatus.OK);
        }

        try {
            output = versionApiSrevice.execute(input);
            // 強制アップデート文言があるの場合、
            if (!"".equals(output.getMessage())) {
                throw new BusinessException(MessageKeys.E_COMMONVERSON_1001);
            }
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
            entityBody.setResultCode("");
            entityBody.setResultData(output);

            LogInfoUtil.LogInfo(MessageKeys.I_ACCOUNTMNT_1001);
        } catch (BusinessException e) {
            
            // 強制アップデートデータがなければ、ログ出力
            LogInfoUtil.LogInfo(e.getErrorCode());
            // ヘッダ設定
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultData(output);
            entityBody.setErrorCode(e.getErrorCode());
        } catch (Exception e) {
            LogInfoUtil.LogError(e.getMessage(), e);
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            // システムエラー
            entityBody.setErrorCode("");
        }
        LogInfoUtil.LogInfoEnd(this.getClass().getSimpleName());
        return new ResponseEntity<ResponseEntityVO<VersionApiResVO>>(entityBody, HttpStatus.OK);
    }
}
