package com.challenge.report.application.service.utils;

import static com.challenge.report.application.service.utils.Constants.DEBIT;

import com.challenge.report.domain.Account;
import com.challenge.report.domain.Client;
import com.challenge.report.domain.Movement;
import com.challenge.report.domain.Report;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface ReportMapper {

  @Mapping(target = "account.number", source = "account.number")
  @Mapping(target = "account.type", source = "account.type")
  @Mapping(target = "account.status", source = "account.status")
  @Mapping(target = "account.balance", source = "movement", qualifiedByName = "calculateInitialBalance")
  Report toReport(Client client, Account account, Movement movement);

  @Named("calculateInitialBalance")
  default BigDecimal calculateInitialBalance(Movement movement) {
    if (movement.getType().equals(DEBIT)) {
      return movement.getBalance().add(movement.getValue());
    } else {
      return movement.getBalance().subtract(movement.getValue());
    }
  }
}
