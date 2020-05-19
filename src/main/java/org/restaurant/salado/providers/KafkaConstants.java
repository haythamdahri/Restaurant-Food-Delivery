package org.restaurant.salado.providers;

/**
 * @author Haytham DAHRI
 */
public class KafkaConstants {
    public static final String KAFKA_PRIVATE_TOPIC = "kafka-private-chat";
    public static final String KAFKA_USER_TOPIC = "kafka-public-user";
    public static final String GROUP_ID = "kafka-sandbox";
    public static final String KAFKA_DEV_BROKER = "localhost:9092";
    public static final String KAFKA_PROD_BROKER = "kafka:9092";

    private KafkaConstants() {}

}
