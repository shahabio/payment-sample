package io.shahab.paymentservice.creditcard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.Map;

@Getter
@Builder
public class CreditCardBankProviderRequestDto {
  @Singular
  private final Map<String, Object> payloads;
  private final String url;
}
