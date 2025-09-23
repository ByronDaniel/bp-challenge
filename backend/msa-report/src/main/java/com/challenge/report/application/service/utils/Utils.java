package com.challenge.report.application.service.utils;

import com.challenge.report.domain.DateRange;
import com.challenge.report.domain.Movement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

  public static DateRange parseDateRange(String date) {
    if (date == null || !date.contains(",")) {
      return new DateRange(null, null);
    }
    String[] parts = date.split(",");
    LocalDate start = LocalDate.parse(parts[0], DateTimeFormatter.ISO_LOCAL_DATE);
    LocalDate end = LocalDate.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE);
    return new DateRange(start, end);
  }

  public static boolean isWithinRange(Movement movement, DateRange range) {
    if (range.getStart() == null || range.getEnd() == null) {
      return true;
    }
    LocalDate movementDate = LocalDateTime
        .parse(movement.getDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .toLocalDate();
    return !movementDate.isBefore(range.getStart()) && !movementDate.isAfter(range.getEnd());
  }
}
