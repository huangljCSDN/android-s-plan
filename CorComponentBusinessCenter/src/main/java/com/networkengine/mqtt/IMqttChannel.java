package com.networkengine.mqtt;

public interface IMqttChannel {

    void registMqttObserver(String appKey);

    void unregistMqttObserver(String appKey);

    void subscribeTopic(String topicName);

    void unsubscribeTopic(String topicName);

}
