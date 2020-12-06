package io.shahab.paymentservice.commons.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Embeddable
public class EncryptedValue {

  private String value;

  @Transient
  private String rawValue;

  public static EncryptedValue of(String rawValue) {
    return new EncryptedValue(encoder().encode(rawValue), rawValue);
  }

  public boolean matches(EncryptedValue valueToMatch) {
    return encoder().matches(valueToMatch.rawValue, this.value);
  }

  private static BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  public boolean notMatches(EncryptedValue valueToMatch) {
    return !matches(valueToMatch);
  }
}
