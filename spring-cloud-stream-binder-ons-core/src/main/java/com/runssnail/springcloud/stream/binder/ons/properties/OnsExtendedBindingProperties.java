package com.runssnail.springcloud.stream.binder.ons.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.ExtendedBindingProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("spring.cloud.stream.ons")
public class OnsExtendedBindingProperties implements ExtendedBindingProperties<OnsConsumerProperties, OnsProducerProperties> {


    private Map<String, OnsBindingProperties> bindings = new HashMap<>();

    public Map<String, OnsBindingProperties> getBindings() {
        return this.bindings;
    }

    public void setBindings(Map<String, OnsBindingProperties> bindings) {
        this.bindings = bindings;
    }
    @Override
    public OnsConsumerProperties getExtendedConsumerProperties(String channelName) {
        if (bindings.containsKey(channelName)) {
            if (bindings.get(channelName).getConsumer() != null) {
                return bindings.get(channelName).getConsumer();
            }
            else {
                OnsConsumerProperties properties = new OnsConsumerProperties();
                this.bindings.get(channelName).setConsumer(properties);
                return properties;
            }
        }
        else {
            OnsConsumerProperties properties = new OnsConsumerProperties();
            OnsBindingProperties rbp = new OnsBindingProperties();
            rbp.setConsumer(properties);
            bindings.put(channelName, rbp);
            return properties;
        }
    }

    @Override
    public OnsProducerProperties getExtendedProducerProperties(String channelName) {
        if (bindings.containsKey(channelName)) {
            if (bindings.get(channelName).getProducer() != null) {
                return bindings.get(channelName).getProducer();
            }
            else {
                OnsProducerProperties properties = new OnsProducerProperties();
                this.bindings.get(channelName).setProducer(properties);
                return properties;
            }
        }
        else {
            OnsProducerProperties properties = new OnsProducerProperties();
            OnsBindingProperties rbp = new OnsBindingProperties();
            rbp.setProducer(properties);
            bindings.put(channelName, rbp);
            return properties;
        }
    }
}
