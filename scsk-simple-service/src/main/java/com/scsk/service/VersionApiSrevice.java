package com.scsk.service;

/**
 * 
 * バージョン更新
 * @author ylq
 *
 */
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.scsk.blogic.AbstractBLogic;
import com.scsk.constants.Constants;
import com.scsk.exception.BusinessException;
import com.scsk.model.VersionApiDoc;
import com.scsk.repository.RepositoryUtil;
import com.scsk.request.vo.VersionApiReqVO;
import com.scsk.response.vo.VersionApiResVO;
import com.scsk.util.GlobalRequest;

@Service
public class VersionApiSrevice extends AbstractBLogic<VersionApiReqVO, VersionApiResVO>{

    // 検索
    private String IOSID = "IOS_VERSION_ID";
    // 検索
    private String ANDROIDID = "ANDROID_VERSION_ID";
    @Autowired
    private RepositoryUtil repositoryUtil;

    @Override
    protected void preExecute(VersionApiReqVO input) throws Exception {
    }

    @Override
    protected VersionApiResVO doExecute(CloudantClient client, VersionApiReqVO input) throws Exception {

        // データベースを取得 
        Database db = client.database(Constants.DB_NAME, false);
        VersionApiResVO output = new VersionApiResVO();
        VersionApiDoc versionApiDoc = null;
        // ゲットヘッド情報
        HttpServletRequest request = GlobalRequest.request();
        
        String versionID ;

        if (request.getHeader("appOS").toUpperCase().startsWith("ANDROID")) {
            versionID = ANDROIDID;
            
        }else if (request.getHeader("appOS").toUpperCase().startsWith("IOS")) {
            versionID = IOSID;
        }else {
            throw new BusinessException("ヘッダの中にアプリ情報が存在しません。");
        }
        // 検索バージョンデータ
        try {
            versionApiDoc = repositoryUtil.find(db, versionID, VersionApiDoc.class);
            List<String> list = versionApiDoc.getVersionList();
            if (list.contains(input.getVersion())) {
                // 許可リストに入った場合、強制アップデートいらない
                output.setMessage("");
            }else {
                // 許可リストに外した場合、エラーとする、強制アップデートしてもらう
                output.setMessage(versionApiDoc.getMessage());
            }
        } catch (BusinessException e) {
            // 強制アップデートデータ
            throw new BusinessException("強制アップデートのチェックマスタデータがありません。");
        }
        return output;
    }
}
