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
import com.scsk.request.vo.StoreATMApiReqVO;
import com.scsk.response.vo.AccountApplicationApplyApiResVO;
import com.scsk.response.vo.StoreATMApiResVO;
import com.scsk.responseentity.vo.ResponseEntityVO;
import com.scsk.service.StoreATMApiService;
import com.scsk.util.CheckCommunicate;
import com.scsk.util.LogInfoUtil;
import com.scsk.util.ResultMessages;

/**
 * 店舗ATMApi用情報の取得<br>
 */
@Controller
@RequestMapping("/api/storeATM")
public class StoreATMApiController {

	/**
	 * 店舗ATMApi用情報の取得
	 */
	@Autowired
	StoreATMApiService storeATMApiService;

	/**
	 * 店舗ATMApi用情報の取得
	 * 
	 * @param input
	 *            リクエストデータ
	 * @return ResponseEntity レスポンスデータ
	 */
	@RequestMapping(value = "/storeATMInfo", headers = { "authKey1", "authKey2", "authKey3", "appVer",
    "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseEntityVO<StoreATMApiResVO>> storeATMInfo(
			HttpServletRequest request, @RequestHeader String authKey1,
            @RequestHeader String authKey2, @RequestHeader String authKey3, @RequestHeader String appVer,
            @RequestHeader String appOS, @RequestBody StoreATMApiReqVO input) {

		LogInfoUtil.LogInfoStrat(this.getClass().getSimpleName());
		ResponseEntityVO<StoreATMApiResVO> entityBody = new ResponseEntityVO<StoreATMApiResVO>();
		if (!CheckCommunicate.check(authKey1, authKey2, authKey3, appVer, appOS)) {
            entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
            entityBody.setResultCode(MessageKeys.E_COMMONAUTH_1001);
            LogInfoUtil.LogInfoEnd(MessageKeys.E_COMMONAUTH_1001);
            return new ResponseEntity<ResponseEntityVO<StoreATMApiResVO>>(entityBody, HttpStatus.OK);
        }

		try {
			StoreATMApiResVO output = storeATMApiService.execute(input);
			// データない場合、Messageをセットする
			if (output.getStoreATMApiList().isEmpty()) {
				throw new BusinessException(MessageKeys.E_STOREINFO_1001);
			}
			// ヘッダ設定（処理成功の場合）
			entityBody.setResultStatus(Constants.RESULT_STATUS_OK);
			entityBody.setResultCode(Constants.RESULT_SUCCESS_CODE);
			// ボーディ設定（処理成功の場合）
			entityBody.setResultData(output);
		} catch (BusinessException e) {
			// ヘッダ設定（処理失敗の場合）
			entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
			entityBody.setErrorCode(e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			// 予想エラー以外の場合
			entityBody.setResultStatus(Constants.RESULT_STATUS_NG);
			entityBody.setErrorCode("");
		}
		LogInfoUtil.LogInfoEnd(this.getClass().getSimpleName());
		return new ResponseEntity<ResponseEntityVO<StoreATMApiResVO>>(
				entityBody, HttpStatus.OK);
	}

}