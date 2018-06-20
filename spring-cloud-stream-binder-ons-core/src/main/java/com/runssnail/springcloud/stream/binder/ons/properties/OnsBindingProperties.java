package com.runssnail.springcloud.stream.binder.ons.properties;

public class OnsBindingProperties {

    private OnsConsumerProperties consumer = new OnsConsumerProperties();

    private OnsProducerProperties producer = new OnsProducerProperties();

    public OnsConsumerProperties getConsumer() {
        return consumer;
    }

    public void setConsumer(OnsConsumerProperties consumer) {
        this.consumer = consumer;
    }

    public OnsProducerProperties getProducer() {
        return producer;
    }

    public void setProducer(OnsProducerProperties producer) {
        this.producer = producer;
    }
}
