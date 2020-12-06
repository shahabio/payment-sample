package io.shahab.paymentservice.creditcard.dto;

import io.shahab.paymentservice.commons.entity.Bank;
import io.shahab.paymentservice.creditcard.entity.CreditCard;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.math.BigInteger;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
public class CreditCardResponseDto {
  private String id;
  private String number;
  private String expiryDate;
  private BigInteger creditAmount;
  private String createdDate;
  private String updatedDate;
  private Bank bank;

  public static CreditCardResponseDto of(CreditCard creditCard) {
    return builder()
      .id(creditCard.getId())
      .number(creditCard.getNumber())
      .creditAmount(creditCard.getCreditAmount())
      .expiryDate(creditCard.getExpiryDate().toHumanReadableShort())
      .bank(creditCard.getBank())
      .createdDate(creditCard.getCreatedDate().toHumanReadableLong())
      .updatedDate(creditCard.getUpdatedDate().toHumanReadableLong())
      .build();
  }

  public static Page<CreditCardResponseDto> convert(Page<CreditCard> creditCards) {
    return creditCards.map(CreditCardResponseDto::of);
  }

}
