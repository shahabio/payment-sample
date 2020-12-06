package io.shahab.paymentservice.credittransfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.shahab.paymentservice.commons.entity.EpochDateTime;

public interface CreditTransferSearchProjectionDto {
  String getId();

  String getCardNumber();

  @JsonIgnore
  String getCreatedDate();

  Long getFailed();

  Long getCompleted();

  default String getTransferDate() {
    return EpochDateTime.ofFormattedGregorianDate(getCreatedDate(), "yyyy-MM-dd").toHumanReadableLong();
  }
}
