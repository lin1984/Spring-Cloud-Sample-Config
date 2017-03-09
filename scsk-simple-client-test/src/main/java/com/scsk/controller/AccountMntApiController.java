package com.scsk.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scsk.config.RibbonConfiguration;
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
import com.scsk.response.vo.AccountAppSeqResVO;
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
import com.scsk.service.AccountMntApiService;

/**
 * アカウント設定コントローラ
 * 
 * @author ws
 *
 */
@RestController
@RibbonClient(name = "scsk-provider", configuration = RibbonConfiguration.class)
@RequestMapping("/api/accountMnt")
public class AccountMntApiController {
	@Autowired
	AccountMntApiService accountMntApiService;

	@RequestMapping(value="/accountAppSeqRes")
	public ResponseEntity<List<AccountAppSeqResVO>> readUserInfo(){
		
		List<AccountAppSeqResVO> accountAppSeqResVOs= new ArrayList<AccountAppSeqResVO>();
		AccountAppSeqResVO accountAppSeqResVO= new AccountAppSeqResVO();
		accountAppSeqResVO.setAppSeq("1");
		accountAppSeqResVOs.add(accountAppSeqResVO);
		accountAppSeqResVO= new AccountAppSeqResVO();
		accountAppSeqResVO.setAppSeq("2");
		accountAppSeqResVOs.add(accountAppSeqResVO);
		accountAppSeqResVO= new AccountAppSeqResVO();
		accountAppSeqResVO.setAppSeq("3");
		accountAppSeqResVOs.add(accountAppSeqResVO);
		

		
		return new ResponseEntity<List<AccountAppSeqResVO>>(accountAppSeqResVOs,HttpStatus.OK);
	}
	
	
	/**
	 * メールアドレスの登録
	 * 
	 * @param input
	 *            リクエストデータ
	 * @return ResponseEntity レスポンスデータ
	 */
	@RequestMapping(value = "/accountRegist", headers = { "authKey1",
			"authKey2", "authKey3", "appVer", "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseEntityVO<AccountMntAccountRegistApiResVO>> accountRegist(
			HttpServletRequest request,
			@RequestBody AccountMntAccountRegistApiReqVO input,
			@RequestHeader String authKey1, @RequestHeader String authKey2,
			@RequestHeader String authKey3, @RequestHeader String appVer,
			@RequestHeader String appOS) {
		ResponseEntityVO<AccountMntAccountRegistApiResVO> entityBody = accountMntApiService
				.accountRegist(input, authKey1, authKey2, authKey3, appVer,
						appOS);
		return new ResponseEntity<ResponseEntityVO<AccountMntAccountRegistApiResVO>>(
				entityBody, HttpStatus.OK);
	}

	/**
	 * ユーザ情報変更
	 * 
	 * @param input
	 * @return ResponseEntity add by 経天宇
	 */
	@RequestMapping(value = "/modifyUserInfo", headers = { "authKey1",
			"authKey2", "authKey3", "appVer", "appOS" }, method = {
			RequestMethod.PUT, RequestMethod.POST }, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseEntityVO<AccountMntModifyUserInfoApiResVO>> modifyUserInfo(
			@RequestBody AccountMntModifyUserInfoApiReqVO input,
			@RequestHeader String authKey1, @RequestHeader String authKey2,
			@RequestHeader String authKey3, @RequestHeader String appVer,
			@RequestHeader String appOS) {
		ResponseEntityVO<AccountMntModifyUserInfoApiResVO> entityBody = accountMntApiService
				.modifyUserInfo(input, authKey1, authKey2, authKey3, appVer,
						appOS);
		return new ResponseEntity<ResponseEntityVO<AccountMntModifyUserInfoApiResVO>>(
				entityBody, HttpStatus.OK);
	}

	/**
	 * facebook登録＆ログイン
	 * 
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/facebookRegistOrLogin", headers = { "authKey1",
			"authKey2", "authKey3", "appVer", "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseEntityVO<AccountMntFacebookRegistOrLoginApiResVO>> facebookRegistOrLogin(
			@RequestBody AccountMntFacebookRegistOrLoginApiReqVO input,
			@RequestHeader String authKey1, @RequestHeader String authKey2,
			@RequestHeader String authKey3, @RequestHeader String appVer,
			@RequestHeader String appOS) {
		ResponseEntityVO<AccountMntFacebookRegistOrLoginApiResVO> entityBody = accountMntApiService
				.facebookRegistOrLogin(input, authKey1, authKey2, authKey3,
						appVer, appOS);
		return new ResponseEntity<ResponseEntityVO<AccountMntFacebookRegistOrLoginApiResVO>>(
				entityBody, HttpStatus.OK);
	}

	/**
	 * Facebook連携
	 * 
	 * @param input
	 * @return ResponseEntity add by 経天宇
	 */
	@RequestMapping(value = "/facebookTogether", headers = { "authKey1",
			"authKey2", "authKey3", "appVer", "appOS" }, method = {
			RequestMethod.PUT, RequestMethod.POST }, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseEntityVO<AccountMntFacebookTogetherApiResVO>> facebookTogether(
			@RequestBody AccountMntFacebookTogetherApiReqVO input,
			@RequestHeader String authKey1, @RequestHeader String authKey2,
			@RequestHeader String authKey3, @RequestHeader String appVer,
			@RequestHeader String appOS) {
		ResponseEntityVO<AccountMntFacebookTogetherApiResVO> entityBody = accountMntApiService
				.facebookTogether(input, authKey1, authKey2, authKey3, appVer,
						appOS);
		return new ResponseEntity<ResponseEntityVO<AccountMntFacebookTogetherApiResVO>>(
				entityBody, HttpStatus.OK);

	}

	/**
	 * パスワード変更
	 * 
	 * @param input
	 * @return ResponseEntity add by 経天宇
	 */
	@RequestMapping(value = "/updateUserPassword", headers = { "authKey1",
			"authKey2", "authKey3", "appVer", "appOS" }, method = {
			RequestMethod.PUT, RequestMethod.POST }, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseEntityVO<AccountMntUpdateUserPasswordApiResVO>> updateUserPassword(
			@RequestBody AccountMntUpdateUserPasswordApiReqVO input,
			@RequestHeader String authKey1, @RequestHeader String authKey2,
			@RequestHeader String authKey3, @RequestHeader String appVer,
			@RequestHeader String appOS) {
		ResponseEntityVO<AccountMntUpdateUserPasswordApiResVO> entityBody = accountMntApiService
				.updateUserPassword(input, authKey1, authKey2, authKey3,
						appVer, appOS);
		return new ResponseEntity<ResponseEntityVO<AccountMntUpdateUserPasswordApiResVO>>(
				entityBody, HttpStatus.OK);
	}

	/**
	 * ログアウト
	 * 
	 * @param input
	 * @return ResponseEntity add by 経天宇
	 */
	@RequestMapping(value = "/accountLogout", headers = { "authKey1",
			"authKey2", "authKey3", "appVer", "appOS" }, method = {
			RequestMethod.PUT, RequestMethod.POST }, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseEntityVO<AccountMntAccountLogoutApiResVO>> accountLogout(
			@RequestBody AccountMntAccountLogoutApiReqVO input,
			@RequestHeader String authKey1, @RequestHeader String authKey2,
			@RequestHeader String authKey3, @RequestHeader String appVer,
			@RequestHeader String appOS) {

		ResponseEntityVO<AccountMntAccountLogoutApiResVO> entityBody = accountMntApiService
				.accountLogout(input, authKey1, authKey2, authKey3, appVer,
						appOS);
		return new ResponseEntity<ResponseEntityVO<AccountMntAccountLogoutApiResVO>>(
				entityBody, HttpStatus.OK);
	}

	/**
	 * メールアドレス変更
	 * 
	 * @param input
	 *            リクエストデータ
	 * @return ResponseEntity レスポンスデータ
	 */
	@RequestMapping(value = "/changeAccountEmail", headers = { "authKey1",
			"authKey2", "authKey3", "appVer", "appOS" }, method = {
			RequestMethod.PUT, RequestMethod.POST }, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseEntityVO<AccountMntChangeAccountEmailApiResVO>> changeAccountEmail(
			@RequestBody AccountMntChangeAccountEmailApiReqVO input,
			@RequestHeader String authKey1, @RequestHeader String authKey2,
			@RequestHeader String authKey3, @RequestHeader String appVer,
			@RequestHeader String appOS) {
		ResponseEntityVO<AccountMntChangeAccountEmailApiResVO> entityBody = accountMntApiService
				.changeAccountEmail(input, authKey1, authKey2, authKey3,
						appVer, appOS);
		return new ResponseEntity<ResponseEntityVO<AccountMntChangeAccountEmailApiResVO>>(
				entityBody, HttpStatus.OK);
	}

	/**
	 * メールアドレスのログイン
	 * 
	 * @param input
	 * @return ResponseEntity add by 経天宇
	 */
	@RequestMapping(value = "/accountLogin", headers = { "authKey1",
			"authKey2", "authKey3", "appVer", "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseEntityVO<AccountMntAccountLoginApiResVO>> accountLogin(
			@RequestBody AccountMntAccountLoginApiReqVO input,
			@RequestHeader String authKey1, @RequestHeader String authKey2,
			@RequestHeader String authKey3, @RequestHeader String appVer,
			@RequestHeader String appOS) {

		ResponseEntityVO<AccountMntAccountLoginApiResVO> entityBody = accountMntApiService
				.accountLogin(input, authKey1, authKey2, authKey3, appVer,
						appOS);
		return new ResponseEntity<ResponseEntityVO<AccountMntAccountLoginApiResVO>>(
				entityBody, HttpStatus.OK);
	}

	/**
	 * ログアウト補充
	 * 
	 * @param input
	 * @return ResponseEntity add by 姚立全
	 */
	@RequestMapping(value = "/logoutSupplement", headers = { "authKey1",
			"authKey2", "authKey3", "appVer", "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseEntityVO<AccountMntLogoutSupplementApiResVO>> logoutSupplement(
			@RequestBody AccountMntLogoutSupplementApiReqVO input,
			@RequestHeader String authKey1, @RequestHeader String authKey2,
			@RequestHeader String authKey3, @RequestHeader String appVer,
			@RequestHeader String appOS) {

		ResponseEntityVO<AccountMntLogoutSupplementApiResVO> entityBody = accountMntApiService
				.logoutSupplement(input, authKey1, authKey2, authKey3, appVer,
						appOS);
		return new ResponseEntity<ResponseEntityVO<AccountMntLogoutSupplementApiResVO>>(
				entityBody, HttpStatus.OK);
	}

	@RequestMapping(value = "/accountResetPasswordByEmail", headers = {
			"authKey1", "authKey2", "authKey3", "appVer", "appOS" }, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<ResponseEntityVO<AccountMntaccountResetPasswordByEmailResVO>> accountResetPasswordByEmail(
			@RequestBody AccountMntaccountResetPasswordByEmailReqVO input,
			@RequestHeader String authKey1, @RequestHeader String authKey2,
			@RequestHeader String authKey3, @RequestHeader String appVer,
			@RequestHeader String appOS) {

		ResponseEntityVO<AccountMntaccountResetPasswordByEmailResVO> entityBody = accountMntApiService
				.accountResetPasswordByEmail(input, authKey1, authKey2,
						authKey3, appVer, appOS);
		return new ResponseEntity<ResponseEntityVO<AccountMntaccountResetPasswordByEmailResVO>>(
				entityBody, HttpStatus.OK);
	}
}