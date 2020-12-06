package io.shahab.paymentservice.commons.entity;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import lombok.*;

import javax.persistence.Embeddable;
import java.text.ParseException;
import java.time.Instant;

@Getter
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class EpochDateTime {

  Long epoch;

  private final static ULocale uLocale = new ULocale("@calendar=persian");
  private final static SimpleDateFormat shortFormatter = new SimpleDateFormat("yy/MM", uLocale);
  private final static SimpleDateFormat longFormatter = new SimpleDateFormat("yyyy/MM/dd", uLocale);

  public static EpochDateTime now() {
    return EpochDateTime.builder().epoch(Instant.now().getEpochSecond()).build();
  }

  public static EpochDateTime ofFormattedDate(String formattedEpoch) {
    try {
      long epoch = shortFormatter.parse(formattedEpoch).getTime() / 1000;
      return EpochDateTime.builder().epoch(epoch).build();

    } catch (ParseException e) {
      throw new RuntimeException("incorrect date");
    }
  }

  public static EpochDateTime ofFormattedDate(String formattedEpoch, String pattern) {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat(pattern, uLocale);
      long epoch = formatter.parse(formattedEpoch).getTime() / 1000;
      return EpochDateTime.builder().epoch(epoch).build();

    } catch (ParseException e) {
      throw new RuntimeException("incorrect date");
    }
  }

  public static EpochDateTime ofFormattedGregorianDate(String gregorianDate, String pattern) {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat(pattern, ULocale.ENGLISH);
      long epoch = formatter.parse(gregorianDate).getTime() / 1000;
      return EpochDateTime.builder().epoch(epoch).build();

    } catch (ParseException e) {
      throw new RuntimeException("incorrect date");
    }
  }

  public String toHumanReadableShort() {
    return shortFormatter.format(toCalendar());
  }

  public String toHumanReadableLong() {
    return longFormatter.format(toCalendar());
  }

  public boolean notEquals(EpochDateTime expiryDate) {
    return !equals(expiryDate);
  }

  public boolean equals(EpochDateTime targetDateTime) {
    Calendar targetCalendar = targetDateTime.toCalendar();
    Calendar sourceCalendar = toCalendar();

    int targetMonth = targetCalendar.get(Calendar.MONTH);
    int targetYear = targetCalendar.get(Calendar.YEAR);

    int sourceMonth = sourceCalendar.get(Calendar.MONTH);
    int sourceYear = sourceCalendar.get(Calendar.YEAR);

    boolean matchesMonth = targetMonth == sourceMonth;
    boolean matchesYear = targetYear == sourceYear;

    return matchesMonth && matchesYear;
  }

  @Override
  public String toString() {
    return longFormatter.format(this.toCalendar());
  }

  private Calendar toCalendar() {
    Calendar calendar = Calendar.getInstance(uLocale);
    calendar.setTimeInMillis(epoch * 1000);
    return calendar;
  }
}
