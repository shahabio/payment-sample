package io.shahab.notificationservice;

import lombok.Value;

@Value
public class NotificationEvent {
  String mobile;
  String message;
}
