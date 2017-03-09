package com.scsk.response.vo;

import java.util.List;

import com.scsk.vo.StoreATMInitVO;
/**
 * 店舗ATM情報レスポンスデータ
 * 
 * @author dy
 *
 */
public class StoreATMApiResVO {

	// 店舗情報一覧取得
	private List<StoreATMInitVO> StoreATMApiList;

	public List<StoreATMInitVO> getStoreATMApiList() {
		return StoreATMApiList;
	}

	public void setStoreATMApiList(List<StoreATMInitVO> storeATMApiList) {
		StoreATMApiList = storeATMApiList;
	}

}