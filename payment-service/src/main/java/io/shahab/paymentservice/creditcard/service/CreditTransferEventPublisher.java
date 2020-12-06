package io.shahab.paymentservice.creditcard.service;

import io.shahab.paymentservice.commons.dto.CreditTransferEvent;
import io.shahab.paymentservice.creditcard.entity.CreditCard;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class CreditTransferEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public CreditTransferEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  public void publish(CreditCard sourceCreditCard,
                      String targetCreditCard,
                      BigInteger amount) {

    CreditTransferEvent creditTransferEvent = CreditTransferEvent.builder()
      .sourceCard(sourceCreditCard)
      .targetCardNumber(targetCreditCard)
      .amount(amount)
      .build();

    eventPublisher.publishEvent(creditTransferEvent);
  }

}
