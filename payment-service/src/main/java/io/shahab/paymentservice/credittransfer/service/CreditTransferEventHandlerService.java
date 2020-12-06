package io.shahab.paymentservice.credittransfer.service;

import io.shahab.paymentservice.commons.NotificationService;
import io.shahab.paymentservice.commons.dto.CreditTransferEvent;
import io.shahab.paymentservice.credittransfer.entity.CreditTransferState;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class CreditTransferEventHandlerService {
  private final CreditTransferService creditTransferService;
  private final NotificationService notificationService;

  public CreditTransferEventHandlerService(CreditTransferService creditTransferService,
                                           NotificationService notificationService) {
    this.creditTransferService = creditTransferService;
    this.notificationService = notificationService;
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onCreditTransferCompletion(CreditTransferEvent event) {
    creditTransferService.create(event, CreditTransferState.TRANSFER_COMPLETED);

    notificationService.send("0936xxxxxxx",
      String.format("%d of your Credit from %s has been transferred to %s",
        event.getAmount(), event.getSourceCard().getNumber(), event.getTargetCardNumber()));
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  public void onCreditTransferFailure(CreditTransferEvent event) {
    creditTransferService.create(event, CreditTransferState.TRANSFER_FAILED);
  }

}
