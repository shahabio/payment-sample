package io.shahab.notificationservice.provider;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SmsNotificationRequestDto {
  String target;
  String msg;
}
