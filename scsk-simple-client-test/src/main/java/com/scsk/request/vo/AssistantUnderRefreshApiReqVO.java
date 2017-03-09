package com.scsk.request.vo;

/**
 * Agentドロップダウンリフレッシュリクエストデータ
 * @author ylq
 */
public class AssistantUnderRefreshApiReqVO {

    // ユーザーID
    private String userId;
    // 開始Rev
    private String startRev;
    // データなしFlg
    private String noDataFlg;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartRev() {
        return startRev;
    }

    public void setStartRev(String startRev) {
        this.startRev = startRev;
    }

    public String getNoDataFlg() {
        return noDataFlg;
    }

    public void setNoDataFlg(String noDataFlg) {
        this.noDataFlg = noDataFlg;
    }
}
