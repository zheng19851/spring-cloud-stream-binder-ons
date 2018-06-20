package com.runssnail.springcloud.stream.binder.ons.provisioning;

import com.runssnail.springcloud.stream.binder.ons.properties.OnsBinderConfigurationProperties;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsConsumerProperties;
import com.runssnail.springcloud.stream.binder.ons.properties.OnsProducerProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningException;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;

public class OnsTopicProvisioner implements ProvisioningProvider<ExtendedConsumerProperties<OnsConsumerProperties>, ExtendedProducerProperties<OnsProducerProperties>>, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private OnsBinderConfigurationProperties configurationProperties;

    public OnsTopicProvisioner(OnsBinderConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public ProducerDestination provisionProducerDestination(String name, ExtendedProducerProperties<OnsProducerProperties> properties) throws ProvisioningException {

        if (this.logger.isInfoEnabled()) {
            this.logger.info("Using RocketMQ topic for outbound: {}", name);
        }

        int partitions = properties.getPartitionCount();

        OnsProducerDestination producerDestination = new OnsProducerDestination(name, partitions);

        return producerDestination;
    }

    @Override
    public ConsumerDestination provisionConsumerDestination(String name, String group, ExtendedConsumerProperties<OnsConsumerProperties> properties) throws ProvisioningException {

        if (this.logger.isInfoEnabled()) {
            this.logger.info("Using aliyun ons topic for inbound: {}", name);
        }

        if (properties.getInstanceCount() == 0) {
            throw new IllegalArgumentException("Instance count cannot be zero");
        }

        int partitionCount = properties.getInstanceCount() * properties.getConcurrency();

        ConsumerDestination consumerDestination = new OnsConsumerDestination(name);

        return consumerDestination;
    }


    private static final class OnsProducerDestination implements ProducerDestination {

        private final String producerDestinationName;

        private final int partitions;

        OnsProducerDestination(String destinationName, int partitions) {
            this.producerDestinationName = destinationName;
            this.partitions = partitions;
        }

        @Override
        public String getName() {
            return producerDestinationName;
        }

        @Override
        public String getNameForPartition(int partition) {
            return producerDestinationName;
        }

        @Override
        public String toString() {
            return "OnsProducerDestination{" +
                    "producerDestinationName='" + producerDestinationName + '\'' +
                    ", partitions=" + partitions +
                    '}';
        }
    }


    private static final class OnsConsumerDestination implements ConsumerDestination {

        private final String consumerDestinationName;

        private final int partitions;

        private final String dlqName;

        OnsConsumerDestination(String consumerDestinationName) {
            this(consumerDestinationName, 0, null);
        }

        OnsConsumerDestination(String consumerDestinationName, int partitions) {
            this(consumerDestinationName, partitions, null);
        }

        OnsConsumerDestination(String consumerDestinationName, Integer partitions, String dlqName) {
            this.consumerDestinationName = consumerDestinationName;
            this.partitions = partitions;
            this.dlqName = dlqName;
        }

        @Override
        public String getName() {
            return this.consumerDestinationName;
        }

        @Override
        public String toString() {
            return "OnsConsumerDestination{" +
                    "consumerDestinationName='" + consumerDestinationName + '\'' +
                    ", partitions=" + partitions +
                    ", dlqName='" + dlqName + '\'' +
                    '}';
        }
    }
}
