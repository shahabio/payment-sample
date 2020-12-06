package io.shahab.paymentservice.creditcard.dto;

import io.shahab.paymentservice.commons.entity.Bank;
import io.shahab.paymentservice.commons.entity.EncryptedValue;
import io.shahab.paymentservice.commons.entity.EpochDateTime;
import io.shahab.paymentservice.creditcard.entity.CreditCard;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigInteger;

import static io.shahab.paymentservice.commons.dto.ValidationPatterns.*;

@Getter
@Setter
public class CreditCardRequestDto {

  @NotBlank
  @Pattern(regexp = CARD_NUMBER)
  private String number;

  @NotNull
  @Min(0)
  private BigInteger creditAmount;

  @NotNull
  private Bank bank;

  @NotNull
  @Pattern(regexp = EXPIRY_DATE)
  private String expiryDate;

  @NotNull
  @Pattern(regexp = CARD_CVV2)
  private String cvv2;

  @NotNull
  private String password;

  public CreditCard toCreditCard() {
    return CreditCard.builder()
      .bank(bank)
      .number(number)
      .creditAmount(creditAmount)
      .expiryDate(EpochDateTime.ofFormattedDate(expiryDate))
      .cvv2(EncryptedValue.of(cvv2))
      .password(EncryptedValue.of(password))
      .build();
  }
}
