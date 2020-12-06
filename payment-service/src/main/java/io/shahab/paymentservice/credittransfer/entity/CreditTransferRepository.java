package io.shahab.paymentservice.credittransfer.entity;

import io.shahab.paymentservice.credittransfer.CreditTransferSearchProjectionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CreditTransferRepository extends JpaRepository<CreditTransfer, String> {

  @Query(value = "SELECT cc.id id, cc.number cardNumber, date(to_timestamp(ct.created_date)) createdDate, " +
    "SUM(case when ct.state = 'TRANSFER_FAILED' then 1 else 0 end)    failed, " +
    "SUM(case when ct.state = 'TRANSFER_COMPLETED' then 1 else 0 end) completed " +
    "FROM credit_transfer ct " +
    "JOIN credit_card cc ON ct.source_card_id = cc.id " +
    "WHERE ct.created_date BETWEEN :fromDate AND :toDate " +
    "GROUP BY cc.id, date(to_timestamp(ct.created_date))",

    countQuery = "SELECT COUNT(*) " +
      "FROM credit_transfer ct " +
      "JOIN credit_card cc ON ct.source_card_id = cc.id " +
      "WHERE ct.created_date BETWEEN :fromDate AND :toDate " +
      "GROUP BY cc.id, date(to_timestamp(ct.created_date))", nativeQuery = true)
  Page<CreditTransferSearchProjectionDto> searchByCardAndDates(@Param("fromDate") Long fromDate, @Param("toDate") Long toDate, Pageable pageable);
}
