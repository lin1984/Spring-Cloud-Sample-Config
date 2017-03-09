package com.scsk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 
 *
 */
@Component
@RefreshScope
public class AssistantChatConfig {
    @Value("${assistantChat.serviceURL}")
    private String serviceURL;
    @Value("${assistantChat.clientId}")
    private String clientId;
    @Value("${assistantChat.clientSecret}")
    private String clientSecret;
    
    public String getServiceURL() {
        return serviceURL;
    }
    public String getClientId() {
        return clientId;
    }
    public String getClientSecret() {
        return clientSecret;
    }
}
