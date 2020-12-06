package io.shahab.paymentservice.creditcard.entity;

import io.shahab.paymentservice.commons.entity.Bank;
import io.shahab.paymentservice.commons.entity.BaseEntity;
import io.shahab.paymentservice.commons.entity.EncryptedValue;
import io.shahab.paymentservice.commons.entity.EpochDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigInteger;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(uniqueConstraints = {@UniqueConstraint(name = "UK_number_active", columnNames = {"number", "active"})})
@Entity
public class CreditCard extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private Bank bank;

  private Boolean active;

  @Column(nullable = false)
  private String number;

  @Column(nullable = false)
  private BigInteger creditAmount;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "cvv2", nullable = false))
  private EncryptedValue cvv2;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "password", nullable = false))
  private EncryptedValue password;

  @Embedded
  @AttributeOverride(name = "epoch", column = @Column(name = "expiryDate", nullable = false))
  private EpochDateTime expiryDate;

  public void updateBank(Bank bank) {
    this.bank = bank;
  }

  public void disable() {
    this.active = false;
  }

  public void decreaseAmount(BigInteger transferAmount) {
    validateDecreaseAmount(transferAmount);
    creditAmount = creditAmount.subtract(transferAmount);
  }

  private void validateDecreaseAmount(BigInteger transferAmount) {
    if (transferAmount.compareTo(this.getCreditAmount()) > 0)
      throw new RuntimeException("You haven't enough credit for transfer");
  }

  @Override
  protected void prePersist() {
    super.prePersist();
    this.active = true;
  }
}
