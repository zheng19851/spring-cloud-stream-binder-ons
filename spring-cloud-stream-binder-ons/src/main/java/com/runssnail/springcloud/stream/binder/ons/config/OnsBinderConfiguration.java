package com.runssnail.springcloud.stream.binder.ons.config;


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

@Configuration
@ConditionalOnMissingBean(Binder.class)
@Import({KryoCodecAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class})
@EnableConfigurationProperties({OnsExtendedBindingProperties.class})
public class OnsBinderConfiguration {

    @Autowired
    private Codec codec;

    @Autowired
    private OnsExtendedBindingProperties extendedBindingProperties;


    @Bean
    OnsBinderConfigurationProperties configurationProperties() {
        return new OnsBinderConfigurationProperties();
    }

    @Bean
    OnsTopicProvisioner provisioningProvider(OnsBinderConfigurationProperties configurationProperties) {
        return new OnsTopicProvisioner(configurationProperties);
    }

    @Bean
    OnsMessageChannelBinder messageChannelBinder(OnsBinderConfigurationProperties configurationProperties,
                                                         OnsTopicProvisioner provisioningProvider) {

        OnsMessageChannelBinder messageChannelBinder = new OnsMessageChannelBinder(
                configurationProperties, provisioningProvider);
        messageChannelBinder.setExtendedBindingProperties(this.extendedBindingProperties);
//        messageChannelBinder.setProducer(this.producer);

        messageChannelBinder.setCodec(this.codec);


        return messageChannelBinder;
    }

}
