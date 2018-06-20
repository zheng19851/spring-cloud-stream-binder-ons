package com.runssnail.springcloud.stream.binder.ons;

import com.runssnail.springcloud.stream.binder.ons.properties.OnsBinderConfigurationProperties;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsConsumerProperties;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsExtendedBindingProperties;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsProducerProperties;
import com.runssnail.springcloud.stream.binder.ons.provisioning.OnsTopicProvisioner;

import org.springframework.cloud.stream.binder.AbstractMessageChannelBinder;
import org.springframework.cloud.stream.binder.BinderHeaders;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.binder.ExtendedPropertiesBinder;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.core.MessageProducer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

public class OnsMessageChannelBinder extends AbstractMessageChannelBinder<ExtendedConsumerProperties<OnsConsumerProperties>,
        ExtendedProducerProperties<OnsProducerProperties>, OnsTopicProvisioner>
        implements ExtendedPropertiesBinder<MessageChannel, OnsConsumerProperties, OnsProducerProperties> {


    private OnsBinderConfigurationProperties configurationProperties;

    private OnsExtendedBindingProperties extendedBindingProperties;

    public OnsMessageChannelBinder(OnsBinderConfigurationProperties configurationProperties, OnsTopicProvisioner provisioningProvider) {
        super(false, headersToMap(configurationProperties), provisioningProvider);
        this.configurationProperties = configurationProperties;

    }

    private static String[] headersToMap(OnsBinderConfigurationProperties configurationProperties) {
        String[] headersToMap;
        if (ObjectUtils.isEmpty(configurationProperties.getHeaders())) {
            headersToMap = BinderHeaders.STANDARD_HEADERS;
        }
        else {
            String[] combinedHeadersToMap = Arrays.copyOfRange(BinderHeaders.STANDARD_HEADERS, 0,
                    BinderHeaders.STANDARD_HEADERS.length + configurationProperties.getHeaders().length);
            System.arraycopy(configurationProperties.getHeaders(), 0, combinedHeadersToMap,
                    BinderHeaders.STANDARD_HEADERS.length,
                    configurationProperties.getHeaders().length);
            headersToMap = combinedHeadersToMap;
        }
        return headersToMap;
    }


    @Override
    protected MessageHandler createProducerMessageHandler(ProducerDestination destination, ExtendedProducerProperties<OnsProducerProperties> producerProperties, MessageChannel errorChannel) throws Exception {

        OnsProducerMessageHandler messageHandler = new OnsProducerMessageHandler(destination.getName(), configurationProperties, producerProperties);
        messageHandler.setBeanFactory(this.getBeanFactory());
        return messageHandler;
    }

    @Override
    protected MessageProducer createConsumerEndpoint(ConsumerDestination destination, String group, ExtendedConsumerProperties<OnsConsumerProperties> properties) throws Exception {

        OnsMessageDrivenChannelAdapter messageDrivenChannelAdapter = new OnsMessageDrivenChannelAdapter(destination, group, properties, this.configurationProperties);

        return messageDrivenChannelAdapter;
    }

    @Override
    public OnsConsumerProperties getExtendedConsumerProperties(String channelName) {
        return this.extendedBindingProperties.getExtendedConsumerProperties(channelName);
    }

    @Override
    public OnsProducerProperties getExtendedProducerProperties(String channelName) {
        return extendedBindingProperties.getExtendedProducerProperties(channelName);
    }

    public OnsBinderConfigurationProperties getConfigurationProperties() {
        return configurationProperties;
    }

    public void setConfigurationProperties(OnsBinderConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public OnsExtendedBindingProperties getExtendedBindingProperties() {
        return extendedBindingProperties;
    }

    public void setExtendedBindingProperties(OnsExtendedBindingProperties extendedBindingProperties) {
        this.extendedBindingProperties = extendedBindingProperties;
    }

}
