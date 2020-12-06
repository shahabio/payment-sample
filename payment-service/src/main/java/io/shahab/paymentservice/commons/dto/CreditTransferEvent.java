package io.shahab.paymentservice.commons.dto;

import io.shahab.paymentservice.creditcard.entity.CreditCard;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@Builder
public class CreditTransferEvent {
  private final CreditCard sourceCard;

  private final String targetCardNumber;

  private final BigInteger amount;
}
