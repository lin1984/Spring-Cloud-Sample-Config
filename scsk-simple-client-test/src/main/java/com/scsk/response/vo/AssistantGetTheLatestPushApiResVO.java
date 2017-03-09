package com.scsk.response.vo;

import java.util.List;

import com.scsk.vo.AskAnswerVO;
/**
 * 最新のプッシュを取得レスポンスデータ
 * @author ylq
 *
 */
public class AssistantGetTheLatestPushApiResVO {

    // タイプ
    private int unreadNum;
    // pushコンテンツ
    private List<AskAnswerVO> pushList;
    
    public int getUnreadNum() {
        return unreadNum;
    }
    public void setUnreadNum(int unreadNum) {
        this.unreadNum = unreadNum;
    }
    public List<AskAnswerVO> getPushList() {
        return pushList;
    }
    public void setPushList(List<AskAnswerVO> pushList) {
        this.pushList = pushList;
    }
}
