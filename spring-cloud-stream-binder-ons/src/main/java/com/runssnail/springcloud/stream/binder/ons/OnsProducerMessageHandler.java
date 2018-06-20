package com.runssnail.springcloud.stream.binder.ons;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsBinderConfigurationProperties;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsProducerProperties;

import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.messaging.Message;

import java.util.Properties;

public class OnsProducerMessageHandler extends AbstractReplyProducingMessageHandler {

    private String topic;

    private OnsBinderConfigurationProperties configurationProperties;

    private ExtendedProducerProperties<OnsProducerProperties> producerProperties;

    private Producer producer;


    public OnsProducerMessageHandler(String topic, OnsBinderConfigurationProperties configurationProperties, ExtendedProducerProperties<OnsProducerProperties> producerProperties) {
        this.topic = topic;
        this.configurationProperties = configurationProperties;
        this.producerProperties = producerProperties;
    }


    @Override
    protected void doInit() {
        super.doInit();

        Properties properties = new Properties();
        // 您在 MQ 控制台创建的 Producer ID
        properties.put(PropertyKeyConst.ProducerId, configurationProperties.getProducerId());
        // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, configurationProperties.getAccessKey());
        // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, configurationProperties.getSecretKey());
        // 设置 TCP 接入域名（此处以公共云的公网接入为例）
        properties.put(PropertyKeyConst.ONSAddr, configurationProperties.getOnsAddress());

        Producer producer = ONSFactory.createProducer(properties);
        producer.start();

        this.producer = producer;

    }

    @Override
    protected Object handleRequestMessage(Message<?> requestMessage) {

        SendResult sendResult = producer.send(new com.aliyun.openservices.ons.api.Message(this.topic, null, (byte[]) requestMessage.getPayload()));

        return null;
    }

}
