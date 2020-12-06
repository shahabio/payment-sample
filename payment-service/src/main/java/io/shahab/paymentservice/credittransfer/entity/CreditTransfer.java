package io.shahab.paymentservice.credittransfer.entity;

import io.shahab.paymentservice.commons.entity.BaseEntity;
import io.shahab.paymentservice.creditcard.entity.CreditCard;
import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;


@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(indexes = @Index(name = "IDX_created_date", columnList = "createdDate"))
@Entity
public class CreditTransfer extends BaseEntity {

  @ManyToOne
  private CreditCard sourceCard;

  private String targetCardNumber;

  private BigInteger amount;

  @Enumerated(EnumType.STRING)
  private CreditTransferState state;
}
