package io.shahab.notificationservice.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
public class NotificationSmsProviderService {

  private final WebClient webClient;
  private final String providerUrl;

  public NotificationSmsProviderService(@Value("${service.providers.sms-provider-url}") String providerUrl) {
    this.providerUrl = providerUrl;
    this.webClient = WebClient.builder().baseUrl(providerUrl).build();
  }

  public void sendSms(SmsNotificationRequestDto requestDto) {
    log.info("Sending SMS Notification-> target: {}, msg: {}", requestDto.getTarget(), requestDto.getMsg());

    webClient.post()
      .bodyValue(requestDto)
      .retrieve()
      .toBodilessEntity()
      .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(10)).filter(this::is5xxServerError))
      .subscribeOn(Schedulers.parallel())
      .doOnError((e) -> log.info("Error on SMS Notification-> target: {}, msg: {}", requestDto.getTarget(), requestDto.getMsg(), e))
      .doOnSuccess((e) -> log.info("SMS Notification sent-> target: {}, msg: {}", requestDto.getTarget(), requestDto.getMsg()))
      .subscribe();
  }

  private boolean is5xxServerError(Throwable throwable) {
    return throwable instanceof WebClientResponseException &&
      ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();
  }
}
