package com.scsk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class EncryptorConfig {
    @Value("${security.encryptKey}")
    private String key;
    
    @Value("${security.encryptIv}")
    private String iv;

    public String getKey() {
        return key;
    }

    public String getIv() {
        return iv;
    }

}
