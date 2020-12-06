package io.shahab.notificationservice;

import io.shahab.notificationservice.provider.NotificationSmsProviderService;
import io.shahab.notificationservice.provider.SmsNotificationRequestDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class NotificationEventHandler {

  private final NotificationSmsProviderService notificationSmsProviderService;

  public NotificationEventHandler(NotificationSmsProviderService notificationSmsProviderService) {
    this.notificationSmsProviderService = notificationSmsProviderService;
  }

  @Bean
  public Consumer<NotificationEvent> notification() {
    return event -> notificationSmsProviderService.sendSms(
      SmsNotificationRequestDto.builder()
        .target(event.getMobile())
        .msg(event.getMessage()).build()
    );
  }
}
