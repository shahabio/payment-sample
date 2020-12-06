package io.shahab.paymentservice.config;

import io.shahab.paymentservice.commons.entity.EpochDateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEpochDateTimeConverter
  implements Converter<String, EpochDateTime> {

  @Override
  public EpochDateTime convert(String source) {
    return EpochDateTime.ofFormattedDate(source, "yy/MM/dd");
  }
}