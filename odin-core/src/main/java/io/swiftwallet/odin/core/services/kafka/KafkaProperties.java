package io.swiftwallet.odin.core.services.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by gibugeorge on 26/12/2016.
 */
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private Producer producer = new Producer();
    private Consumer consumer = new Consumer();


    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
}
