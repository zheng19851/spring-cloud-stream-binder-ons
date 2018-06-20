package com.runssnail.springcloud.stream.binder.ons.config;


import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.runssnail.springcloud.stream.binder.ons.OnsMessageChannelBinder;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsBinderConfigurationProperties;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsExtendedBindingProperties;
import com.runssnail.springcloud.stream.binder.ons.provisioning.OnsTopicProvisioner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.cloud.stream.config.codec.kryo.KryoCodecAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.codec.Codec;

import java.util.Properties;

@Configuration
@ConditionalOnMissingBean(Binder.class)
@Import({KryoCodecAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class})
@EnableConfigurationProperties({OnsExtendedBindingProperties.class})
public class OnsBinderConfiguration {

    @Autowired
    private Codec codec;

    @Autowired
    private OnsExtendedBindingProperties extendedBindingProperties;

    @Autowired
    private Producer producer;


    /**
     * 可以和应用共享producer，所以这里先判断有没有bean存在
     *
     * @param configurationProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(Producer.class)
    Producer createProducer(OnsBinderConfigurationProperties configurationProperties) {

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
        return producer;
    }

    @Bean
    OnsBinderConfigurationProperties configurationProperties() {
        return new OnsBinderConfigurationProperties();
    }

    @Bean
    OnsTopicProvisioner provisioningProvider(OnsBinderConfigurationProperties configurationProperties) {
        return new OnsTopicProvisioner(configurationProperties);
    }

    @Bean
    OnsMessageChannelBinder rocketMQMessageChannelBinder(OnsBinderConfigurationProperties configurationProperties,
                                                         OnsTopicProvisioner provisioningProvider) {

        OnsMessageChannelBinder messageChannelBinder = new OnsMessageChannelBinder(
                configurationProperties, provisioningProvider);
        messageChannelBinder.setExtendedBindingProperties(this.extendedBindingProperties);
        messageChannelBinder.setProducer(this.producer);

        messageChannelBinder.setCodec(this.codec);


        return messageChannelBinder;
    }

}
