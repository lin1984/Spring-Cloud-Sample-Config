package com.scsk.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.DesignDocument.MapReduce;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.constants.ApplicationKeys;
import com.scsk.constants.Constants;
import com.scsk.exception.BusinessException;
import com.scsk.model.AccountAppAgreedOperationDateDoc;
import com.scsk.model.AccountAppDoc;
import com.scsk.model.AssistantChatApiDoc;
import com.scsk.model.LaunchAgreeApiDoc;
import com.scsk.model.PushrecordDoc;
import com.scsk.model.UserMigrationDoc;
import com.scsk.repository.RepositoryUtil;
import com.scsk.request.vo.userMigrationApiReqVO;
import com.scsk.response.vo.userMigrationApiResVO;

@Service
public class userMigrationApiService extends AbstractBLogic<userMigrationApiReqVO, userMigrationApiResVO> {

    @Autowired
    private RepositoryUtil repositoryUtil;

    // 検索key
    private final String USERINFO = "userId:\"%s\"";
    // IDデータを変更します
    private final String CHANGEIDDATA = "1";
    // DOCタイプ
    private String DOCTYPE = "USERMIGRATION";

    @Override
    protected void preExecute(userMigrationApiReqVO input) throws Exception {
    }

    @Override
    protected userMigrationApiResVO doExecute(CloudantClient client, userMigrationApiReqVO input) throws Exception {
        Database db = client.database(Constants.DB_NAME, false);
        List<AccountAppDoc> accountAppDocList = new ArrayList<AccountAppDoc>();
        List<PushrecordDoc> pushrecordDocList = new ArrayList<PushrecordDoc>();
        List<AssistantChatApiDoc> assistantChatApiDocList = new ArrayList<AssistantChatApiDoc>();
        List<AccountAppAgreedOperationDateDoc> accountAppAgreedOperationDateDocslist = new ArrayList<AccountAppAgreedOperationDateDoc>();
        List<LaunchAgreeApiDoc> launchAgreeApiDocList = new ArrayList<LaunchAgreeApiDoc>();
        String flag = "0";
        // 口座開設
        try {
            MapReduce view = new MapReduce();
            view.setMap("function (doc) {if(doc.docType && doc.docType === \"ACCOUNTAPPLICATION\"&& doc.userId===\""
                    + input.getOldUser() + "\"){emit(doc.applicationDate, [1]);}}");
            accountAppDocList = repositoryUtil.queryByDynamicView(db, view, AccountAppDoc.class);
        } catch (BusinessException e) {
            flag = "1";
        }
        // 配信履歴
        try {
            MapReduce view = new MapReduce();
            view.setMap("function (doc) {if(doc.docType && doc.docType === \"PUSHRECORD\"&& doc.userId===\""
                    + input.getOldUser() + "\") {emit(doc.createdDate, [doc.delFlg]);}}");
            pushrecordDocList = repositoryUtil.queryByDynamicView(db, view, PushrecordDoc.class);
        } catch (BusinessException e) {
            flag = "1";
        }
        // 同意操作データ
        try {
            MapReduce view = new MapReduce();
            view.setMap("function (doc) {if(doc.docType && doc.docType === \"AGREEDOPERATIONDATE\"&& doc.userId===\""
                    + input.getOldUser() + "\") {emit(doc.createdDate, [doc.delFlg]);}}");
            accountAppAgreedOperationDateDocslist = repositoryUtil.queryByDynamicView(db, view, AccountAppAgreedOperationDateDoc.class);
        } catch (BusinessException e) {
            flag = "1";
        }
        // 利用規約
        try {
            MapReduce view = new MapReduce();
            view.setMap("function (doc) {if(doc.docType && doc.docType === \"LANUCHAGREE\"&& doc.userId===\""
                    + input.getOldUser() + "\") {emit(doc.createdDate, [doc.delFlg]);}}");
            launchAgreeApiDocList = repositoryUtil.queryByDynamicView(db, view, LaunchAgreeApiDoc.class);
        } catch (BusinessException e) {
            flag = "1";
        }
        // 対話履歴
        String ke = String.format(USERINFO, input.getOldUser());
        try {
            assistantChatApiDocList = repositoryUtil.getIndex(db,
                    ApplicationKeys.INSIGHTINDEX_ASSISTANT_SEARCHBYUSERID_ASSISTANTCHAT, ke, AssistantChatApiDoc.class);
        } catch (BusinessException e) {
            flag = "1";
        }

        // 口座開設
        if (accountAppDocList != null && accountAppDocList.size() > 0) {

            for (int i = 0; i < accountAppDocList.size(); i++) {
                AccountAppDoc accountAppDoc = accountAppDocList.get(i);
                accountAppDoc.setUserId(input.getNewUser());
                try {
                    repositoryUtil.update(db, accountAppDoc);
                } catch (Exception e) {
                    flag = "1";
                }
            }
        }

        // 配信履歴
        if (pushrecordDocList != null && pushrecordDocList.size() > 0) {

            for (int i = 0; i < pushrecordDocList.size(); i++) {
                PushrecordDoc pushrecordDoc = pushrecordDocList.get(i);
                pushrecordDoc.setUserId(input.getNewUser());
                try {
                    repositoryUtil.update(db, pushrecordDoc);
                } catch (Exception e) {
                    flag = "1";
                }
            }
        }

        // 同意操作データ
        if (accountAppAgreedOperationDateDocslist != null && accountAppAgreedOperationDateDocslist.size() > 0) {

            for (int i = 0; i < accountAppAgreedOperationDateDocslist.size(); i++) {
                AccountAppAgreedOperationDateDoc accountAppAgreedOperationDateDoc = accountAppAgreedOperationDateDocslist.get(i);
                accountAppAgreedOperationDateDoc.setUserId(input.getNewUser());
                try {
                    repositoryUtil.update(db, accountAppAgreedOperationDateDoc);
                } catch (Exception e) {
                    flag = "1";
                }
            }
        }

        // 利用規約
        if (launchAgreeApiDocList != null && launchAgreeApiDocList.size() > 0) {

            for (int i = 0; i < launchAgreeApiDocList.size(); i++) {
                LaunchAgreeApiDoc launchAgreeApiDoc = launchAgreeApiDocList.get(i);
                launchAgreeApiDoc.setUserId(input.getNewUser());
                try {
                    repositoryUtil.update(db, launchAgreeApiDoc);
                } catch (Exception e) {
                    flag = "1";
                }
            }
        }

        // 対話履歴
        if (assistantChatApiDocList != null && assistantChatApiDocList.size() == 1) {
            AssistantChatApiDoc assistantChatApiDoc = assistantChatApiDocList.get(0);
            assistantChatApiDoc.setUserId(input.getNewUser());
            assistantChatApiDoc.setChangeIdData(CHANGEIDDATA);
            try {
                repositoryUtil.update(db, assistantChatApiDoc);
            } catch (Exception e) {
                flag = "1";
            }
        }
        UserMigrationDoc userMigrationDoc = new UserMigrationDoc();
        userMigrationDoc.setDocType(DOCTYPE);
        userMigrationDoc.setOldUser(input.getOldUser());
        userMigrationDoc.setNewUser(input.getNewUser());
        userMigrationDoc.setFlag(flag);
        try {
            repositoryUtil.save(db, userMigrationDoc);
        } catch (BusinessException e) {
            // TODO
        }
        return null;
    }
}
