package io.shahab.paymentservice.credittransfer;

import io.shahab.paymentservice.commons.entity.EpochDateTime;
import io.shahab.paymentservice.credittransfer.service.CreditTransferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * Credit Transfer Controller responsible for query credit transfers
 * </pre>
 */
@RestController
@RequestMapping("${service.api}/credit-transfers")
public class CreditTransferController {

  private final CreditTransferService creditTransferService;

  public CreditTransferController(CreditTransferService creditTransferService) {
    this.creditTransferService = creditTransferService;
  }

  @GetMapping
  public Page<CreditTransferSearchProjectionDto> search(@RequestParam EpochDateTime fromDate,
                                                        @RequestParam EpochDateTime toDate,
                                                        Pageable pageable) {

    return creditTransferService.search(fromDate, toDate, pageable);
  }
}
