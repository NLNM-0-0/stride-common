package com.stride.tracking.commons.constants;

public class KafkaTopics {
    private KafkaTopics() {}

    public static final String NOTIFICATION_TOPIC = "notification_delivery";
    public static final String FCM_TOPIC = "fcm_delivery";

    public static final String ACTIVITY_CREATED_TOPIC = "activity_created";
    public static final String ACTIVITY_UPDATED_TOPIC = "activity_updated";
    public static final String ACTIVITY_DELETED_TOPIC = "activity_deleted";

    public static final String SPORT_CREATED_TOPIC = "sport_created";
    public static final String SPORT_UPDATED_TOPIC = "sport_updated";

    public static final String USER_CREATED_TOPIC = "user_created";
}
