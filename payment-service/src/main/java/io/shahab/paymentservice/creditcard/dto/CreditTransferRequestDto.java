package io.shahab.paymentservice.creditcard.dto;

import io.shahab.paymentservice.commons.entity.EncryptedValue;
import io.shahab.paymentservice.commons.entity.EpochDateTime;
import io.shahab.paymentservice.creditcard.entity.CreditCard;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigInteger;

import static io.shahab.paymentservice.commons.dto.ValidationPatterns.*;

@Getter
@Setter
public class CreditTransferRequestDto {

  @NotNull
  @Min(0)
  @Max(1000000)
  private BigInteger amount;

  @NotBlank
  @Pattern(regexp = CARD_NUMBER)
  private String sourceCardNumber;

  @NotBlank
  @Pattern(regexp = EXPIRY_DATE)
  private String sourceCardExpiryDate;

  @NotBlank
  @Pattern(regexp = CARD_CVV2)
  private String sourceCardCvv2;

  @NotBlank
  private String sourceCardPassword;

  @NotBlank
  @Pattern(regexp = CARD_NUMBER)
  private String targetCardNumber;

  public CreditCard toSourceCreditCard() {
    return CreditCard.builder()
      .number(sourceCardNumber)
      .expiryDate(EpochDateTime.ofFormattedDate(sourceCardExpiryDate))
      .cvv2(EncryptedValue.of(sourceCardCvv2))
      .password(EncryptedValue.of(sourceCardPassword))
      .build();
  }
}
