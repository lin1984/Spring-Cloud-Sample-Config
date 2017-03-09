package com.scsk.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scsk.config.AssistantChatConfig;

/**
 * 自動応答フリーテキスト
 * @author ylq
 */
@Component
public class AssistantChat {
    @Autowired
    private AssistantChatConfig config;

    /**
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private CloseableHttpClient getClient() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager manager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[] { manager }, null);
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);

        RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .setExpectContinueEnabled(true).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", factory).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry);

        return HttpClients.custom().setConnectionManager(connectionManager)
                .setDefaultRequestConfig(defaultRequestConfig).build();
    }

    /**
     * 
     * @param messageContent
     * @return String[StatusCode, ResponseEntity]
     * @throws Exception
     */
    public String[] sendMessage(String messageContent) throws Exception {

        CloseableHttpClient httpclient = null;
        try {
            httpclient = getClient();

            HttpPost httppost = new HttpPost(config.getServiceURL());

            httppost.setHeader("X-Agent-Client-Id", config.getClientId());
            httppost.setHeader("X-Agent-Client-Secret", config.getClientSecret());
            httppost.setHeader("Content-Type", "application/json");

            StringEntity stringEntity = new StringEntity(messageContent, ContentType.APPLICATION_JSON);
            httppost.setEntity(stringEntity);

            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                String entityString = EntityUtils.toString(response.getEntity(), "UTF-8");
                String codeString = String.valueOf(response.getStatusLine().getStatusCode());
                LogInfoUtil.LogDebug("----------------------------------------");
                LogInfoUtil.LogDebug("Send Message Body : " + messageContent);
                LogInfoUtil.LogDebug("Status Code       : " + codeString);
                LogInfoUtil.LogDebug("Response Entity   : " + entityString);
                LogInfoUtil.LogDebug("----------------------------------------");
                
                return new String[] { String.valueOf(response.getStatusLine().getStatusCode()), entityString};
            } finally {
                response.close();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                LogInfoUtil.logWarn(e.getMessage(), e);
            }
        }
    }
/**
 * 会話パラメーターを取得する
 * @author ylq
 * @param ask
 * @return content
 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String sendAsk(String ask) {
        
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> userData;
        String content = "";
        try {
            userData = mapper.readValue(ask, Map.class);
            userData = (Map<String, Object>) userData.get("result");
            List l = (List)userData.get("message_content");
            Map<String,String> dd = (Map<String,String>)l.get(0);
            content = dd.get("content");
        } catch (IOException e) {
            LogInfoUtil.logWarn("", e);
        }
        return content;
    }
}
