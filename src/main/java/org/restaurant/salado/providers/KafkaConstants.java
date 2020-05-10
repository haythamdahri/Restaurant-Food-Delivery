package org.restaurant.salado.providers;

/**
 * @author Haytham DAHRI
 */
public class KafkaConstants {
    public static final String KAFKA_PRIVATE_TOPIC = "kafka-private-chat";
    public static final String KAFKA_USER_TOPIC = "kafka-public-user";
    public static final String GROUP_ID = "kafka-sandbox";
    public static final String KAFKA_BROKER = "localhost:9092";

    private KafkaConstants() {}

}
