package io.shahab.paymentservice.commons.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class NotificationEvent {
  String mobile;
  String message;
}
