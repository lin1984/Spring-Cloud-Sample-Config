package com.scsk.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.constants.ApplicationKeys;
import com.scsk.constants.Constants;
import com.scsk.model.StoreATMInfoDoc;
import com.scsk.request.vo.StoreATMApiReqVO;
import com.scsk.response.vo.StoreATMApiResVO;
import com.scsk.vo.StoreATMInitVO;

/**
 * 店舗ATMApi用情報検索サービス。<br>
 * <br>
 * 店舗ATMApi用情報検索を実装するロジック。<br>
 */
@Service
public class StoreATMApiService extends
		AbstractBLogic<StoreATMApiReqVO, StoreATMApiResVO> {

	/**
	 * 主処理実行する前にパラメータを整理メソッド（基礎クラスの抽象メソッドを実現）。
	 * 
	 * @param なし
	 *            検索条件
	 */
	@Override
	protected void preExecute(StoreATMApiReqVO ReqVO) throws Exception {

	}

	/**
	 * 主処理実行するメソッド（基礎クラスの抽象メソッドを実現）。
	 * 
	 * @param client
	 *            クラウドDBに接続オブジェクト
	 * @param ReqVO
	 *            検索条件
	 * @return storeATMApiResVO 店舗ATMApi用情報
	 */
	@Override
	protected StoreATMApiResVO doExecute(CloudantClient client,
			StoreATMApiReqVO ReqVO) throws Exception {
		// データベースを取得
		Database db = client.database(Constants.DB_NAME, false);

		// 店舗ATMApi用情報データを取得
		List<StoreATMInfoDoc> storeATMInfoDocList = new ArrayList<>();
		List<StoreATMInitVO> storeATMList = new ArrayList<>();
		StoreATMApiResVO storeATMApiResVO = new StoreATMApiResVO();

		if (ReqVO.getSearchMode().equals("1")) {
			// 検索キーを整理する
			String queryKey = "storeNumber:\"" + ReqVO.getStoreNumber()
					+ "\" AND subStoreNumber:\"00\" AND atmStoreNumber:\"00\" AND typeKbn:0 "; 
			System.out.println(queryKey);
			storeATMInfoDocList = repositoryUtil.getIndex(db,
					ApplicationKeys.INSIGHTINDEX_STORE_STOREATM_SEARCHINFO,
					queryKey, StoreATMInfoDoc.class);
			if (storeATMInfoDocList != null && storeATMInfoDocList.size() != 0) {
				for (StoreATMInfoDoc doc : storeATMInfoDocList) {
					StoreATMInitVO storeATMInitVO = new StoreATMInitVO();
					storeATMInitVO = setData(doc);

					storeATMList.add(storeATMInitVO);
				}
			}
		} else if(ReqVO.getSearchMode().equals("2")){
			storeATMInfoDocList = repositoryUtil.getView(db,
					ApplicationKeys.INSIGHTVIEW_STOREATMLIST_STOREATMLIST,
					StoreATMInfoDoc.class);

			if (storeATMInfoDocList != null && storeATMInfoDocList.size() != 0) {
				for (StoreATMInfoDoc doc : storeATMInfoDocList) {
					if (doc.getStoreATMName().contains(ReqVO.getStoreATMName())
							&& doc.getTypeKbn() == 0) {
						StoreATMInitVO storeATMInitVO = new StoreATMInitVO();
						storeATMInitVO = setData(doc);

						storeATMList.add(storeATMInitVO);
					}
				}
			}
		} else if(ReqVO.getSearchMode().equals("3")){
			storeATMInfoDocList = repositoryUtil.getView(db,
					ApplicationKeys.INSIGHTVIEW_STOREATMLIST_STOREATMLIST,
					StoreATMInfoDoc.class);

			if (storeATMInfoDocList != null && storeATMInfoDocList.size() != 0) {
				for (StoreATMInfoDoc doc : storeATMInfoDocList) {
					if (doc.getTypeKbn() == 0 || doc.getTypeKbn() == 1) {
						StoreATMInitVO storeATMInitVO = new StoreATMInitVO();
						storeATMInitVO = setData(doc);

						storeATMList.add(storeATMInitVO);
					}
				}
			}
		} else {
			// 何もしない
		}

		storeATMApiResVO.setStoreATMApiList(storeATMList);

		return storeATMApiResVO;
	}

	/**
	 * 店舗ATMApi用情報一覧取得
	 * @param StoreATMInfoDoc 店舗情報Doc
	 * @return StoreATMInitVO 店舗ATMApi用情報一覧
	 */
	private StoreATMInitVO setData(StoreATMInfoDoc doc) {
		
		StoreATMInitVO storeATMInitVO = new StoreATMInitVO();
		
		/******** 利用可否フラグ ********/
		//システム日付取得
		Calendar rightNow = Calendar.getInstance(); 
		
		//曜日取得
		int dayOfWeek = rightNow.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0){        
			dayOfWeek = 0;      
	    } 
		
		//システム時間HHMM		
		String hour = rightNow.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + rightNow.get(Calendar.HOUR_OF_DAY) : String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY));
		String minute = rightNow.get(Calendar.MINUTE) < 10 ? "0" + rightNow.get(Calendar.MINUTE) : String.valueOf(rightNow.get(Calendar.MINUTE));
		String systemTime = hour + minute;		

		//利用可否フラグ   true:利用可能   false:利用不可
		Boolean canUseFlag = true;
		
		//店舗区分  0:窓口店舗　1:店舗外ATM　2:企業内
		int typeKbn = doc.getTypeKbn();	
		
		//始業時間
		String startTime;
		//ATM始業時間　：　窓口時間帯で判定した後にATM時間帯で判定する２段階でフラグ識別
		String atmStartTime = "";
		
		if (typeKbn == 0){
			//0:窓口店舗の場合
			if(dayOfWeek == 0){
				//日曜日の場合、窓口営業時間_日曜日始業
				startTime = doc.getWindowOpenStartTime_SUN();
				//日曜日の場合、ATM営業時間_日曜日始業
				atmStartTime = doc.getAtmOpenStartTime_SUN();
			} else if (dayOfWeek == 6){
				//土曜日の場合、窓口営業時間_土曜日始業
				startTime = doc.getWindowOpenStartTime_SAT();
				//土曜日の場合、ATM営業時間_土曜日始業
				atmStartTime = doc.getAtmOpenStartTime_SAT();
			} else {
				//平日の場合、窓口営業時間_平日始業
				startTime = doc.getWindowOpenStartTime();
				//平日の場合、ATM営業時間_平日始業
				atmStartTime = doc.getAtmOpenStartTime();
			}			
		} else {
			//1:店舗外ATM　2:企業内
			if(dayOfWeek == 0){
				//日曜日の場合、ATM営業時間_日曜日始業
				startTime = doc.getAtmOpenStartTime_SUN();
			} else if (dayOfWeek == 6){
				//土曜日の場合、ATM営業時間_土曜日始業
				startTime = doc.getAtmOpenStartTime_SAT();
			} else {
				//平日の場合、ATM営業時間_平日始業
				startTime = doc.getAtmOpenStartTime();
			}		
		}
		
		// 基準の時間帯が未設定の場合、時間帯外と取扱い
		if (startTime == null || startTime.equals("")){
			canUseFlag = false;
		} else {
			startTime = startTime.replace(":", "");
			startTime = startTime.length() < 4 ? "0" + startTime : startTime;
			
			//0:窓口店舗の場合
			if(typeKbn == 0){
				if (atmStartTime != null && !atmStartTime.equals("")){
					atmStartTime = atmStartTime.replace(":", "");
					atmStartTime = atmStartTime.length() < 4 ? "0" + atmStartTime : atmStartTime;
					
					if (atmStartTime.compareTo(startTime) < 0 ){
						startTime = atmStartTime;
					}
				}
			}
		}
		
		
		//終業時間
		String endTime;
		//ATM終業時間　：　窓口時間帯で判定した後にATM時間帯で判定する２段階でフラグ識別
		String atmEndTime = "";
		
		if (typeKbn == 0){
			//0:窓口店舗の場合
			if(dayOfWeek == 0){
				//日曜日の場合、窓口営業時間_日曜日始業
				endTime = doc.getWindowOpenEndTime_SUN();
				//日曜日の場合、ATM営業時間_日曜日始業
				atmEndTime = doc.getAtmOpenEndTime_SUN();
			} else if (dayOfWeek == 6){
				//土曜日の場合、窓口営業時間_土曜日始業
				endTime = doc.getWindowOpenEndTime_SAT();
				//土曜日の場合、ATM営業時間_土曜日始業
				atmEndTime = doc.getAtmOpenEndTime_SAT();
			} else {
				//平日の場合、窓口営業時間_平日始業
				endTime = doc.getWindowOpenEndTime();
				//平日の場合、ATM営業時間_平日始業
				atmEndTime = doc.getAtmOpenEndTime();
			}			
		} else {
			//1:店舗外ATM　2:企業内
			if(dayOfWeek == 0){
				//日曜日の場合、ATM営業時間_日曜日始業
				endTime = doc.getAtmOpenEndTime_SUN();
			} else if (dayOfWeek == 6){
				//土曜日の場合、ATM営業時間_土曜日始業
				endTime = doc.getAtmOpenEndTime_SAT();
			} else {
				//平日の場合、ATM営業時間_平日始業
				endTime = doc.getAtmOpenEndTime();
			}		
		}
		
		// 基準の時間帯が未設定の場合、時間帯外と取扱い
		if (endTime == null || endTime.equals("")){
			canUseFlag = false;
		} else {
			endTime = endTime.replace(":", "");
			endTime = endTime.length() < 4 ? "0" + endTime : endTime;
			
			//0:窓口店舗の場合
			if(typeKbn == 0){
				if (atmEndTime != null && !atmEndTime.equals("")){
					atmEndTime = atmEndTime.replace(":", "");
					atmEndTime = atmEndTime.length() < 4 ? "0" + atmEndTime : atmEndTime;
					
					if (endTime.compareTo(atmEndTime) < 0 ){
						endTime = atmEndTime;
					}
				}
			}
		}
		
		// 時間帯を使って判断
		if (canUseFlag){
			if (systemTime.compareTo(startTime) < 0 || endTime.compareTo(systemTime) < 0 ){
				canUseFlag = false;
			}
		}		
		
		// 利用可否フラグ の設定
		storeATMInitVO.setCanUseFlag(canUseFlag);
		
		/******** 取扱いサービス ********/
		// 商品サービス_外貨両替（ﾄﾞﾙ、ﾕｰﾛ）
		String serviceDollarEuro = doc.getServiceDollarEuro() == 1 ? "外貨両替（ﾄﾞﾙ、ﾕｰﾛ）" : "";		
		storeATMInitVO.setServiceDollarEuro(serviceDollarEuro);
		
		// 商品サービス_外貨両替（ｱｼﾞｱ通貨）
		String serviceAsia = doc.getServiceAsia() == 1 ? "外貨両替（ｱｼﾞｱ通貨）" : "";		
		storeATMInitVO.setServiceAsia(serviceAsia);		
		
		// 商品サービス_外貨両替（その他）
		String serviceOther = doc.getServiceOther() == 1 ? "外貨両替（その他）" : "";		
		storeATMInitVO.setServiceOther(serviceOther);		
		
		// 商品サービス_外貨買取
		String serviceForeignExchange = doc.getServiceForeignExchange() == 1 ? "外貨買取" : "";		
		storeATMInitVO.setServiceForeignExchange(serviceForeignExchange);		
		
		// 商品サービス_投資信託
		String serviceInvestmentTrust = doc.getServiceInvestmentTrust() == 1 ? "投資信託" : "";		
		storeATMInitVO.setServiceInvestmentTrust(serviceInvestmentTrust);		
		
		// 商品サービス_年金保険
		String servicePensionInsurance = doc.getServicePensionInsurance() == 1 ? "年金保険" : "";		
		storeATMInitVO.setServicePensionInsurance(servicePensionInsurance);		
		
		// 商品サービス_金融商品仲介（みずほ証券）
		String serviceMizuho = doc.getServiceMizuho() == 1 ? "金融商品仲介（みずほ証券）" : "";		
		storeATMInitVO.setServiceMizuho(serviceMizuho);		
		
		// 商品サービス_金融商品仲介（ひろぎんウツミ屋）
		String serviceHirginUtsumiya = doc.getServiceHirginUtsumiya() == 1 ? "金融商品仲介（ひろぎんウツミ屋）" : "";		
		storeATMInitVO.setServiceHirginUtsumiya(serviceHirginUtsumiya);		
		
		// 商品サービス_全自動貸金庫
		String serviceAutoSafeDepositBox = doc.getServiceAutoSafeDepositBox() == 1 ? "全自動貸金庫" : "";		
		storeATMInitVO.setServiceAutoSafeDepositBox(serviceAutoSafeDepositBox);		
		
		// 商品サービス_一般貸金庫
		String serviceSafeDepositBox = doc.getServiceSafeDepositBox() == 1 ? "一般貸金庫" : "";		
		storeATMInitVO.setServiceSafeDepositBox(serviceSafeDepositBox);		
		
		// 商品サービス_ｾｰﾌﾃｨｹｰｽ
		String serviceSafeBox = doc.getServiceSafeBox() == 1 ? "ｾｰﾌﾃｨｹｰｽ" : "";		
		storeATMInitVO.setServiceSafeBox(serviceSafeBox);		
		
		// ATM機能_振込
		String atmFunctionTransfer = doc.getAtmFunctionTransfer() == 1 ? "振込" : "";		
		storeATMInitVO.setAtmFunctionTransfer(atmFunctionTransfer);		
		
		// ATM機能_硬貨入出金
		String atmFunctionCoinAccess = doc.getAtmFunctionCoinAccess() == 1 ? "硬貨入出金" : "";		
		storeATMInitVO.setAtmFunctionCoinAccess(atmFunctionCoinAccess);		
		
		// ATM機能_宝くじｻｰﾋﾞｽ
		String atmFunctionLotteryService = doc.getAtmFunctionLotteryService() == 1 ? "宝くじｻｰﾋﾞｽ" : "";		
		storeATMInitVO.setAtmFunctionLotteryService(atmFunctionLotteryService);		
		
		// ATM機能_手のひら認証
		String atmFunctionPalmAuthentication = doc.getAtmFunctionPalmAuthentication() == 1 ? "手のひら認証" : "";		
		storeATMInitVO.setAtmFunctionPalmAuthentication(atmFunctionPalmAuthentication);		
		
		// ATM機能_IC対応
		String atmFunctionIC = doc.getAtmFunctionIC() == 1 ? "IC対応" : "";		
		storeATMInitVO.setAtmFunctionIC(atmFunctionIC);		
		
		// ATM機能_PASPYチャージ
		String atmFunctionPASPY = doc.getAtmFunctionPASPY() == 1 ? "PASPYチャージ" : "";		
		storeATMInitVO.setAtmFunctionPASPY(atmFunctionPASPY);		
		
		// ATM機能_他行幹事
		String atmFunctionOtherBankingAffairs = doc.getAtmFunctionOtherBankingAffairs() == 1 ? "他行幹事" : "";		
		storeATMInitVO.setAtmFunctionOtherBankingAffairs(atmFunctionOtherBankingAffairs);		
		
		// 店舗ATMApi用情報データを戻る
		storeATMInitVO.set_id(doc.get_id());
		storeATMInitVO.set_rev(doc.get_rev());
		storeATMInitVO.setAddress(doc.getAddress());
		storeATMInitVO.setAtmCount(doc.getAtmCount());
//		storeATMInitVO.setAtmFunctionCoinAccess(doc.getAtmFunctionCoinAccess());
//		storeATMInitVO.setAtmFunctionIC(doc.getAtmFunctionIC());
//		storeATMInitVO.setAtmFunctionLotteryService(doc.getAtmFunctionLotteryService());
//		storeATMInitVO.setAtmFunctionOtherBankingAffairs(doc.getAtmFunctionOtherBankingAffairs());
//		storeATMInitVO.setAtmFunctionPalmAuthentication(doc.getAtmFunctionPalmAuthentication());
//		storeATMInitVO.setAtmFunctionPASPY(doc.getAtmFunctionPASPY());
//		storeATMInitVO.setAtmFunctionTransfer(doc.getAtmFunctionTransfer());
		storeATMInitVO.setAtmOpenEndTime(doc.getAtmOpenEndTime());
		storeATMInitVO.setAtmOpenEndTime_SAT(doc.getAtmOpenEndTime_SAT());
		storeATMInitVO.setAtmOpenEndTime_SUN(doc.getAtmOpenEndTime_SUN());
		storeATMInitVO.setAtmOpenStartTime(doc.getAtmOpenStartTime());
		storeATMInitVO.setAtmOpenStartTime_SAT(doc.getAtmOpenStartTime_SAT());
		storeATMInitVO.setAtmOpenStartTime_SUN(doc.getAtmOpenStartTime_SUN());
		storeATMInitVO.setAtmStoreNumber(doc.getAtmStoreNumber());
		storeATMInitVO.setBarrierFreeAED(doc.getBarrierFreeAED());
		storeATMInitVO.setBarrierFreeBrailleBlock(doc.getBarrierFreeBrailleBlock());
		storeATMInitVO.setBarrierFreeVisualImpairment(doc.getBarrierFreeVisualImpairment());
		storeATMInitVO.setBarrierFreeVoiceGuide(doc.getBarrierFreeVoiceGuide());
		storeATMInitVO.setChildrenSpace(doc.getChildrenSpace());
		storeATMInitVO.setComment1(doc.getComment1());
		storeATMInitVO.setComment2(doc.getComment2());
		storeATMInitVO.setComment3(doc.getComment3());
		storeATMInitVO.setComment4(doc.getComment4());
		storeATMInitVO.setComment5(doc.getComment5());
		storeATMInitVO.setDocType(doc.getDocType());
		storeATMInitVO.setEndDateTime(doc.getEndDateTime());
		storeATMInitVO.setHirginUtsumiya(doc.getHirginUtsumiya());
		storeATMInitVO.setInternationalTradePC(doc.getInternationalTradePC());
		storeATMInitVO.setKanaStoreATMName(doc.getKanaStoreATMName());
		storeATMInitVO.setLandMark(doc.getLandMark());
		storeATMInitVO.setLatitude(doc.getLatitude());
		storeATMInitVO.setLongitude(doc.getLongitude());
		storeATMInitVO.setPark(doc.getPark());
		storeATMInitVO.setParkComment(doc.getParkComment());
		storeATMInitVO.setParkServiceForDisabled(doc.getParkServiceForDisabled());
		storeATMInitVO.setPostCode(doc.getPostCode());
//		storeATMInitVO.setServiceAsia(doc.getServiceAsia());
//		storeATMInitVO.setServiceAutoSafeDepositBox(doc.getServiceAutoSafeDepositBox());
//		storeATMInitVO.setServiceDollarEuro(doc.getServiceDollarEuro());
//		storeATMInitVO.setServiceForeignExchange(doc.getServiceForeignExchange());
//		storeATMInitVO.setServiceHirginUtsumiya(doc.getServiceHirginUtsumiya());
//		storeATMInitVO.setServiceInvestmentTrust(doc.getServiceInvestmentTrust());
//		storeATMInitVO.setServiceMizuho(doc.getServiceMizuho());
//		storeATMInitVO.setServiceOther(doc.getServiceOther());
//		storeATMInitVO.setServicePensionInsurance(doc.getServicePensionInsurance());
//		storeATMInitVO.setServiceSafeBox(doc.getServiceSafeBox());
//		storeATMInitVO.setServiceSafeDepositBox(doc.getServiceSafeDepositBox());
		storeATMInitVO.setStartDateTime(doc.getStartDateTime());
		storeATMInitVO.setStoreATMName(doc.getStoreATMName());
		storeATMInitVO.setStoreNumber(doc.getStoreNumber());
		storeATMInitVO.setSubStoreNumber(doc.getSubStoreNumber());
		storeATMInitVO.setTeleNumber(doc.getTeleNumber());
		storeATMInitVO.setTypeKbn(doc.getTypeKbn());
		storeATMInitVO.setWindowOpenEndTime(doc.getWindowOpenEndTime());
		storeATMInitVO.setWindowOpenEndTime_SAT(doc.getWindowOpenEndTime_SAT());
		storeATMInitVO.setWindowOpenEndTime_SUN(doc.getWindowOpenEndTime_SUN());
		storeATMInitVO.setWindowOpenStartTime(doc.getWindowOpenStartTime());
		storeATMInitVO.setWindowOpenStartTime_SAT(doc.getWindowOpenStartTime_SAT());
		storeATMInitVO.setWindowOpenStartTime_SUN(doc.getWindowOpenStartTime_SUN());
		
		return storeATMInitVO;
	}
}
