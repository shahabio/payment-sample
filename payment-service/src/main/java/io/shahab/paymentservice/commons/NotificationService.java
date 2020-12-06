package io.shahab.paymentservice.commons;

import io.shahab.paymentservice.commons.dto.NotificationEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {

  private final StreamBridge producer;

  public NotificationService(StreamBridge producer) {
    this.producer = producer;
  }

  @Async
  public void send(String mobile, String message) {
    producer.send("notification-in-0",
      NotificationEvent.builder().mobile(mobile).message(message).build());
  }
}
