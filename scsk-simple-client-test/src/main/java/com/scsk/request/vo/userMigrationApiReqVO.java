package com.scsk.request.vo;
/**
 * 匿名公式ユーザーターン，ザーID变更
 * @author ylq
 *
 */
public class userMigrationApiReqVO {

    // 古いユーザーID
    private String oldUser;
    // 新しいユーザーID
    private String newUser;
    public String getOldUser() {
        return oldUser;
    }
    public void setOldUser(String oldUser) {
        this.oldUser = oldUser;
    }
    public String getNewUser() {
        return newUser;
    }
    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }
    
    
}
