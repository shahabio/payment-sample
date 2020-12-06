package io.shahab.paymentservice.creditcard.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCard, String> {

  Optional<CreditCard> findByActiveIsTrueAndIdEquals(String id);

  Optional<CreditCard> findByActiveIsTrueAndNumber(String number);

  Page<CreditCard> findAllByActiveIsTrue(Pageable pageable);
}