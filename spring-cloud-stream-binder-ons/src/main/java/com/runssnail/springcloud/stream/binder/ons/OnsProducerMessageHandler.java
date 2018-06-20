package com.runssnail.springcloud.stream.binder.ons;

import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsBinderConfigurationProperties;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsProducerProperties;

import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

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

        Assert.notNull(this.producer, "Producer is required");

    }

    @Override
    protected Object handleRequestMessage(Message<?> requestMessage) {

        SendResult sendResult = producer.send(new com.aliyun.openservices.ons.api.Message(this.topic, null, (byte[]) requestMessage.getPayload()));

        return null;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }
}
