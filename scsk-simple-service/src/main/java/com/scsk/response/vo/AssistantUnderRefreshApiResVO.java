package com.scsk.response.vo;

import java.util.List;

import com.scsk.vo.AskAnswerVO;

/**
 * Agentドロップダウンリフレッシュレスポンスデータ
 * @author ylq
 */
public class AssistantUnderRefreshApiResVO {

    // ユーザーID
    private String userId;
    // 未読数
    private int unreadCount;
    // 始めますRev
    private String startRev;
    // データFlg
    private String noDataFlg;
    // 問答VOList
    private List<AskAnswerVO> askAnswerVOList;

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

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public List<AskAnswerVO> getAskAnswerVOList() {
        return askAnswerVOList;
    }

    public void setAskAnswerVOList(List<AskAnswerVO> askAnswerVOList) {
        this.askAnswerVOList = askAnswerVOList;
    }
}
