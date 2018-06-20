package com.runssnail.springcloud.stream.binder.ons;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsBinderConfigurationProperties;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsConsumerProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.Message;

import java.util.Properties;

/**
 * @author zhengwei
 */
public class OnsMessageDrivenChannelAdapter extends MessageProducerSupport {


    private Consumer consumer;

    private ConsumerDestination consumerDestination;

    private String consumerGroup;

    private ExtendedConsumerProperties<OnsConsumerProperties> consumerProperties;

    private OnsBinderConfigurationProperties configurationProperties;

    public OnsMessageDrivenChannelAdapter(ConsumerDestination destination, String consumerGroup, ExtendedConsumerProperties<OnsConsumerProperties> consumerProperties, OnsBinderConfigurationProperties configurationProperties) {
        this.consumerDestination = destination;
        this.consumerGroup = consumerGroup;
        this.consumerProperties = consumerProperties;

        this.configurationProperties = configurationProperties;

        this.consumerGroup = consumerGroup;
    }


    @Override
    protected void doStart() {

        Properties properties = new Properties();
        // 您在 MQ 控制台创建的 Producer ID
        properties.put(PropertyKeyConst.ConsumerId, configurationProperties.getConsumerId());
        // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, configurationProperties.getAccessKey());
        // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, configurationProperties.getSecretKey());
        // 设置 TCP 接入域名（此处以公共云的公网接入为例）
        properties.put(PropertyKeyConst.ONSAddr, configurationProperties.getOnsAddress());

        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe(this.consumerDestination.getName(), "*", new OnsMessageListener());
        consumer.start();

        this.consumer = consumer;

    }


    private class OnsMessageListener implements MessageListener {

        private final Logger logger = LoggerFactory.getLogger(OnsMessageListener.class);

        @Override
        public Action consume(com.aliyun.openservices.ons.api.Message message, ConsumeContext context) {
            logger.info("receive messages: {}", message);

            byte[] payload = message.getBody();

            Message<?> internalMsgObject = getMessageBuilderFactory().withPayload(payload).build();
            sendMessage(internalMsgObject);
            return Action.CommitMessage;
        }
    }


    @Override
    protected void doStop() {
        this.consumer.shutdown();
    }

}
