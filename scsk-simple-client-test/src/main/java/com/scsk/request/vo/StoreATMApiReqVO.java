package com.scsk.request.vo;
/**
 * 店舗ATM情報リクエストデータ
 * 
 * @author dy
 *
 */
public class StoreATMApiReqVO {

	// モード
	private String searchMode;
	// 店舗コード_母店番号
	private String storeNumber;
	// 店舗名_（漢字）
	private String storeATMName;

	public String getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}

	public String getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	public String getStoreATMName() {
		return storeATMName;
	}

	public void setStoreATMName(String storeATMName) {
		this.storeATMName = storeATMName;
	}
}
