package com.scsk.service;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.loadbalancer.ILoadBalancer;
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

@Service
public class AccountMntApiService {
	@Autowired
	RestTemplate restTemplate;

	private final String SERVICE_NAME = "scsk-simple-service";
	@Autowired
	LoadBalancerClient loadBalancerClient;
	@Autowired
	SpringClientFactory clientFactory;
	public ResponseEntityVO<AccountMntAccountRegistApiResVO> accountRegist(
			AccountMntAccountRegistApiReqVO input, String authKey1,
			String authKey2, String authKey3, String appVer, String appOS) {
		HttpEntity<String> formEntity = setjson(input, authKey1, authKey2,
				authKey3, appVer, appOS);
		
//		ServiceInstance instance = loadBalancerClient.choose(SERVICE_NAME);
//		
//
//		
//		ILoadBalancer loadBalancer = this.clientFactory.getLoadBalancer(SERVICE_NAME);
//// loadBalancer.getAllServers().get(0).getMetaInfo().isAlive();
//		
//		
//		
		
		
		
		
		
		ResponseEntityVO<AccountMntAccountRegistApiResVO> responseEntity = restTemplate
				.postForObject("http://" + SERVICE_NAME+ "/api/accountMnt/accountRegist", formEntity,
						ResponseEntityVO.class);
		return responseEntity;
	}

	@HystrixCommand(fallbackMethod = "accountRegistFallback")
	public ResponseEntityVO<AccountMntModifyUserInfoApiResVO> modifyUserInfo(
			AccountMntModifyUserInfoApiReqVO input, String authKey1,
			String authKey2, String authKey3, String appVer, String appOS) {

		HttpEntity<String> formEntity = setjson(input, authKey1, authKey2,
				authKey3, appVer, appOS);

		ResponseEntityVO<AccountMntModifyUserInfoApiResVO> responseEntity = restTemplate
				.postForObject("http://" + SERVICE_NAME
						+ "/api/accountMnt/modifyUserInfo", formEntity,
						ResponseEntityVO.class);
		return responseEntity;

	}

	@HystrixCommand(fallbackMethod = "accountRegistFallback")
	public ResponseEntityVO<AccountMntFacebookRegistOrLoginApiResVO> facebookRegistOrLogin(
			AccountMntFacebookRegistOrLoginApiReqVO input, String authKey1,
			String authKey2, String authKey3, String appVer, String appOS) {

		HttpEntity<String> formEntity = setjson(input, authKey1, authKey2,
				authKey3, appVer, appOS);
		ResponseEntityVO<AccountMntFacebookRegistOrLoginApiResVO> responseEntity = restTemplate
				.postForObject("http://" + SERVICE_NAME
						+ "/api/accountMnt/facebookRegistOrLogin", formEntity,
						ResponseEntityVO.class);
		return responseEntity;
	}

	@HystrixCommand(fallbackMethod = "accountRegistFallback")
	public ResponseEntityVO<AccountMntUpdateUserPasswordApiResVO> updateUserPassword(
			AccountMntUpdateUserPasswordApiReqVO input, String authKey1,
			String authKey2, String authKey3, String appVer, String appOS) {
		HttpEntity<String> formEntity = setjson(input, authKey1, authKey2,
				authKey3, appVer, appOS);
		ResponseEntityVO<AccountMntUpdateUserPasswordApiResVO> responseEntity = restTemplate
				.postForObject("http://" + SERVICE_NAME
						+ "/api/accountMnt/updateUserPassword", formEntity,
						ResponseEntityVO.class);
		return responseEntity;

	}

	@HystrixCommand(fallbackMethod = "accountRegistFallback")
	public ResponseEntityVO<AccountMntFacebookTogetherApiResVO> facebookTogether(
			AccountMntFacebookTogetherApiReqVO input, String authKey1,
			String authKey2, String authKey3, String appVer, String appOS) {
		HttpEntity<String> formEntity = setjson(input, authKey1, authKey2,
				authKey3, appVer, appOS);
		ResponseEntityVO<AccountMntFacebookTogetherApiResVO> responseEntity = restTemplate
				.postForObject("http://" + SERVICE_NAME
						+ "/api/accountMnt/facebookTogether", formEntity,
						ResponseEntityVO.class);
		return responseEntity;

	}

	@HystrixCommand(fallbackMethod = "accountRegistFallback")
	public ResponseEntityVO<AccountMntAccountLogoutApiResVO> accountLogout(
			AccountMntAccountLogoutApiReqVO input, String authKey1,
			String authKey2, String authKey3, String appVer, String appOS) {
		HttpEntity<String> formEntity = setjson(input, authKey1, authKey2,
				authKey3, appVer, appOS);
		ResponseEntityVO<AccountMntAccountLogoutApiResVO> responseEntity = restTemplate
				.postForObject("http://" + SERVICE_NAME
						+ "/api/accountMnt/accountLogout", formEntity,
						ResponseEntityVO.class);
		return responseEntity;
	}

	@HystrixCommand(fallbackMethod = "accountRegistFallback")
	public ResponseEntityVO<AccountMntChangeAccountEmailApiResVO> changeAccountEmail(
			@RequestBody AccountMntChangeAccountEmailApiReqVO input,
			@RequestHeader String authKey1, @RequestHeader String authKey2,
			@RequestHeader String authKey3, @RequestHeader String appVer,
			@RequestHeader String appOS) {
		HttpEntity<String> formEntity = setjson(input, authKey1, authKey2,
				authKey3, appVer, appOS);
		ResponseEntityVO<AccountMntChangeAccountEmailApiResVO> responseEntity = restTemplate
				.postForObject("http://" + SERVICE_NAME
						+ "/api/accountMnt/changeAccountEmail", formEntity,
						ResponseEntityVO.class);
		return responseEntity;
	}

	@HystrixCommand(fallbackMethod = "accountRegistFallback")
	public ResponseEntityVO<AccountMntAccountLoginApiResVO> accountLogin(
			AccountMntAccountLoginApiReqVO input, String authKey1,
			String authKey2, String authKey3, String appVer, String appOS) {
		HttpEntity<String> formEntity = setjson(input, authKey1, authKey2,
				authKey3, appVer, appOS);
		ResponseEntityVO<AccountMntAccountLoginApiResVO> responseEntity = restTemplate
				.postForObject("http://" + SERVICE_NAME
						+ "/api/accountMnt/accountLogin", formEntity,
						ResponseEntityVO.class);
		return responseEntity;
	}

	@HystrixCommand(fallbackMethod = "accountRegistFallback")
	public ResponseEntityVO<AccountMntLogoutSupplementApiResVO> logoutSupplement(
			AccountMntLogoutSupplementApiReqVO input, String authKey1,
			String authKey2, String authKey3, String appVer, String appOS) {
		HttpEntity<String> formEntity = setjson(input, authKey1, authKey2,
				authKey3, appVer, appOS);
		ResponseEntityVO<AccountMntLogoutSupplementApiResVO> responseEntity = restTemplate
				.postForObject("http://" + SERVICE_NAME
						+ "/api/accountMnt/logoutSupplement", formEntity,
						ResponseEntityVO.class);
		return responseEntity;
	}

	@HystrixCommand(fallbackMethod = "accountRegistFallback")
	public ResponseEntityVO<AccountMntaccountResetPasswordByEmailResVO> accountResetPasswordByEmail(
			AccountMntaccountResetPasswordByEmailReqVO input, String authKey1,
			String authKey2, String authKey3, String appVer, String appOS) {
		HttpEntity<String> formEntity = setjson(input, authKey1, authKey2,
				authKey3, appVer, appOS);
		ResponseEntityVO<AccountMntaccountResetPasswordByEmailResVO> responseEntity = restTemplate
				.postForObject("http://" + SERVICE_NAME
						+ "/api/accountMnt/accountResetPasswordByEmail",
						formEntity, ResponseEntityVO.class);
		return responseEntity;
	}

	private HttpEntity<String> setjson(Object obj, String authKey1,
			String authKey2, String authKey3, String appVer, String appOS) {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType
				.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		headers.add("authKey1", authKey1);
		headers.add("authKey2", authKey2);
		headers.add("authKey3", authKey3);
		headers.add("appVer", appVer);
		headers.add("appOS", appOS);
		JSONObject jsonObj = JSONObject.fromObject(obj);
		HttpEntity<String> formEntity = new HttpEntity<String>(
				jsonObj.toString(), headers);
		return formEntity;

	}

	private ResponseEntityVO<AccountMntAccountRegistApiResVO> accountRegistFallback() {
		System.out.println("HystrixCommand fallbackMethod handle!");
		ResponseEntityVO<AccountMntAccountRegistApiResVO> responseEntity = new ResponseEntityVO<AccountMntAccountRegistApiResVO>();
		responseEntity.setErrorCode("error");
		return responseEntity;
	}
}
