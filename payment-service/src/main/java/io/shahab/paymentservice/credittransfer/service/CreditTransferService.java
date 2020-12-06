package io.shahab.paymentservice.credittransfer.service;

import io.shahab.paymentservice.commons.dto.CreditTransferEvent;
import io.shahab.paymentservice.commons.entity.EpochDateTime;
import io.shahab.paymentservice.credittransfer.CreditTransferSearchProjectionDto;
import io.shahab.paymentservice.credittransfer.entity.CreditTransfer;
import io.shahab.paymentservice.credittransfer.entity.CreditTransferRepository;
import io.shahab.paymentservice.credittransfer.entity.CreditTransferState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CreditTransferService {

  private final CreditTransferRepository creditTransferRepository;

  public CreditTransferService(CreditTransferRepository creditTransferRepository) {
    this.creditTransferRepository = creditTransferRepository;
  }

  public Page<CreditTransferSearchProjectionDto> search(EpochDateTime fromDate, EpochDateTime toDate, Pageable pageable) {
    return creditTransferRepository.searchByCardAndDates(fromDate.getEpoch(), toDate.getEpoch(), pageable);
  }

  @Async
  public void create(CreditTransferEvent event, CreditTransferState state) {
    CreditTransfer creditTransfer =
      CreditTransfer.builder()
        .sourceCard(event.getSourceCard())
        .targetCardNumber(event.getTargetCardNumber())
        .amount(event.getAmount())
        .state(state)
        .build();

    creditTransferRepository.save(creditTransfer);
  }
}
