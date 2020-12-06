package io.shahab.paymentservice.creditcard.service;

import io.shahab.paymentservice.creditcard.dto.CreditTransferRequestDto;
import io.shahab.paymentservice.creditcard.entity.CreditCard;
import io.shahab.paymentservice.creditcard.entity.CreditCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Optional;

@Slf4j
@Service
public class CreditCardService {
  private final CreditCardBankProviderService creditCardBankProviderService;
  private final CreditTransferEventPublisher creditTransferEventPublisher;
  private final CreditCardRepository creditCardRepository;

  public CreditCardService(CreditCardBankProviderService creditCardBankProviderService, CreditTransferEventPublisher creditTransferEventPublisher,
                           CreditCardRepository creditCardRepository) {
    this.creditCardBankProviderService = creditCardBankProviderService;

    this.creditTransferEventPublisher = creditTransferEventPublisher;
    this.creditCardRepository = creditCardRepository;
  }

  @Transactional
  public CreditCard createOrUpdate(CreditCard creditCardDto) {
    return creditCardRepository.save(creditCardDto);
  }

  @Transactional
  public void remove(String id) {
    Optional<CreditCard> dbCreditCard = creditCardRepository.findByActiveIsTrueAndIdEquals(id);
    CreditCard creditCard = dbCreditCard.orElseThrow(() -> new RuntimeException("Card not found or already removed."));

    creditCard.disable();

    creditCardRepository.save(creditCard);

    log.info("Your Credit card [{}] has been Removed.", creditCard.getNumber());
  }

  public Page<CreditCard> findAll(Pageable pageable) {
    return creditCardRepository.findAllByActiveIsTrue(pageable);
  }

  public CreditCard findByCardNumber(String cardNumber) {
    return creditCardRepository.findByActiveIsTrueAndNumber(cardNumber)
      .orElseThrow(() -> new RuntimeException("Cannot find card"));
  }

  @Transactional
  public void transferCredit(CreditTransferRequestDto transferRequestDto) {

    CreditCard sourceCreditCardDto = transferRequestDto.toSourceCreditCard();
    String targetCreditCardNumber = transferRequestDto.getTargetCardNumber();
    BigInteger transferAmount = transferRequestDto.getAmount();

    log.info("Transfer Credit amount Requested. [{}] ({})-> [{}]", sourceCreditCardDto.getNumber(), transferAmount, targetCreditCardNumber);

    CreditCard sourceCreditCard = findByCardNumber(sourceCreditCardDto.getNumber());

    creditTransferEventPublisher.publish(sourceCreditCard, targetCreditCardNumber, transferAmount);

    validateSourceCreditCard(sourceCreditCard, sourceCreditCardDto);

    creditCardBankProviderService.transferCredit(transferRequestDto);

    decreaseSourceCreditAmount(sourceCreditCard, transferAmount);

    log.info("Transfer Credit amount Completed. [{}] ({})-> [{}]", sourceCreditCardDto.getNumber(), transferAmount, targetCreditCardNumber);
  }

  private void decreaseSourceCreditAmount(CreditCard sourceCreditCard,
                                          BigInteger transferAmount) {
    sourceCreditCard.decreaseAmount(transferAmount);
    createOrUpdate(sourceCreditCard);
  }


  private void validateSourceCreditCard(CreditCard sourceCreditCard,
                                        CreditCard sourceCreditCardDto) {
    RuntimeException exception = null;

    if (sourceCreditCard.getExpiryDate().notEquals(sourceCreditCardDto.getExpiryDate())) {
      exception = new RuntimeException("Incorrect Expiry date");

    } else if (sourceCreditCard.getCvv2().notMatches(sourceCreditCardDto.getCvv2())) {
      exception = new RuntimeException("Incorrect Cvv2");

    } else if (sourceCreditCard.getPassword().notMatches(sourceCreditCardDto.getPassword())) {
      exception = new RuntimeException("Incorrect password");
    }

    if (exception != null)
      throw exception;
  }
}
