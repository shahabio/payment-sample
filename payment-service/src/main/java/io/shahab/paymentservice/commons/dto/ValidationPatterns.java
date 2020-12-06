package io.shahab.paymentservice.commons.dto;

public final class ValidationPatterns {
  public final static String CARD_NUMBER = "\\d{4}-\\d{4}-\\d{4}-\\d{4}";

  public final static String CARD_CVV2 = "\\d{3,4}";

  public final static String EXPIRY_DATE = "\\d{2}\\/\\d{2}";

}
