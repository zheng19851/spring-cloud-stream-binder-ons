package com.runssnail.springcloud.stream.binder.ons.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cloud.stream.ons.binder")
public class OnsBinderConfigurationProperties {

    private String[] headers = new String[]{};

    private String accessKey;
    private String secretKey;
    private String onsAddress;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getOnsAddress() {
        return onsAddress;
    }

    public void setOnsAddress(String onsAddress) {
        this.onsAddress = onsAddress;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }
}
