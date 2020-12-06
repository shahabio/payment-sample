package io.shahab.paymentservice.creditcard;

import io.shahab.paymentservice.creditcard.dto.CreditCardRequestDto;
import io.shahab.paymentservice.creditcard.dto.CreditCardResponseDto;
import io.shahab.paymentservice.creditcard.dto.CreditTransferRequestDto;
import io.shahab.paymentservice.creditcard.entity.CreditCard;
import io.shahab.paymentservice.creditcard.service.CreditCardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <pre>
 * Credit Card Controller responsible for all credit operations:
 * - CRUD operations, such as create, get, and remove Credit Cards 💳
 * - Transfer Credit between cards 💸
 * </pre>
 */
@RestController
@RequestMapping("${service.api}/credit-cards")
public class CreditCardController {

  private final CreditCardService creditCardService;

  public CreditCardController(CreditCardService creditCardService) {
    this.creditCardService = creditCardService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  CreditCardResponseDto create(@RequestBody @Valid CreditCardRequestDto creditCardRequestDto) {
    CreditCard creditCard = creditCardService.createOrUpdate(creditCardRequestDto.toCreditCard());
    return CreditCardResponseDto.of(creditCard);
  }

  @DeleteMapping("/{id}")
  void remove(@PathVariable String id) {
    creditCardService.remove(id);
  }

  @PostMapping("/transfer")
  void transferCredit(@RequestBody CreditTransferRequestDto creditTransferRequestDto) {
    try {
      creditCardService.transferCredit(creditTransferRequestDto);
    } catch (Exception e) {
      throw new CreditTransferException(e);
    }
  }

  @GetMapping
  Page<CreditCardResponseDto> findAll(Pageable pageable) {
    Page<CreditCard> creditCards = creditCardService.findAll(pageable);

    return CreditCardResponseDto.convert(creditCards);
  }

}
