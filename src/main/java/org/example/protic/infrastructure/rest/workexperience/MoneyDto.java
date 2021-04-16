package org.example.protic.infrastructure.rest.workexperience;

import org.javamoney.moneta.Money;

import java.math.BigDecimal;

public class MoneyDto {

  public BigDecimal value;
  public String currency;

  public static MoneyDto of(Money money) {
    MoneyDto moneyDto = new MoneyDto();
    moneyDto.value = money.getNumberStripped();
    moneyDto.currency = money.getCurrency().getCurrencyCode();
    return moneyDto;
  }
}
