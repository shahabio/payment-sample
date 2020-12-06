package io.shahab.paymentservice.creditcard.service;

import io.shahab.paymentservice.creditcard.dto.CreditCardBankProviderRequestDto;
import io.shahab.paymentservice.creditcard.dto.CreditTransferRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CreditCardBankProviderService {
  private final RestTemplate restTemplate;

  private final String bankProviderUrl;

  public CreditCardBankProviderService(RestTemplate restTemplate,
                                       @Value("${service.bank-provider-url}") String bankProviderUrl) {
    this.restTemplate = restTemplate;
    this.bankProviderUrl = bankProviderUrl;
  }

  public void transferCredit(CreditTransferRequestDto creditTransferRequestDto) {
    CreditCardBankProviderRequestDto.CreditCardBankProviderRequestDtoBuilder bankRequest = CreditCardBankProviderRequestDto.builder()
      .payload("source", creditTransferRequestDto.getSourceCardNumber())
      .payload("cvv2", creditTransferRequestDto.getSourceCardCvv2())
      .payload("amount", creditTransferRequestDto.getAmount().toString());

    if (shouldTransferViaProviderOne(creditTransferRequestDto)) {
      bankRequest
        .url(bankProviderUrl)
        .payload("dest", creditTransferRequestDto.getTargetCardNumber())
        .payload("expDate", creditTransferRequestDto.getSourceCardExpiryDate())
        .payload("pin", creditTransferRequestDto.getSourceCardPassword());
    } else {
      bankRequest
        .url(bankProviderUrl) // could be different in real scenario
        .payload("target", creditTransferRequestDto.getTargetCardNumber())
        .payload("expire", creditTransferRequestDto.getSourceCardExpiryDate())
        .payload("pin2", creditTransferRequestDto.getSourceCardPassword());
    }

    transfer(bankRequest.build());
  }

  private boolean shouldTransferViaProviderOne(CreditTransferRequestDto creditTransferRequestDto) {
    return creditTransferRequestDto.getSourceCardNumber().startsWith("6037");
  }

  private void transfer(CreditCardBankProviderRequestDto request) {

    ResponseEntity<String> transferResponse =
      restTemplate.postForEntity(request.getUrl(), request.getPayloads(), String.class);

    if (!transferResponse.getStatusCode().is2xxSuccessful())
      throw new RuntimeException("Bank provider credit transfer error");
  }

}
