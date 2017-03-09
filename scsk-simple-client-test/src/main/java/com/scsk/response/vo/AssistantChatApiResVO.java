package com.scsk.response.vo;
/**
 * 自動応答フリーテキストレスポンスデータ
 * @author ylq
 *
 */
public class AssistantChatApiResVO {

    //  ユーザーID
    private String userId;
    //  質問
    private String ask;
    //  質問時間
    private String askTime;
    //  回答
    private String answer;
    //  回答時間
    private String answerTime;
    //
    private String startRev;
    
    public String getStartRev() {
        return startRev;
    }
    public void setStartRev(String startRev) {
        this.startRev = startRev;
    }
    public String getAskTime() {
        return askTime;
    }
    public void setAskTime(String askTime) {
        this.askTime = askTime;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getAnswerTime() {
        return answerTime;
    }
    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getAsk() {
        return ask;
    }
    public void setAsk(String ask) {
        this.ask = ask;
    }
}
