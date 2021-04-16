package org.example.protic.commons;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;

public class CurrencyContext {

  private static final List<CurrencyUnit> ALLOWED_CURRENCIES = List.of(Monetary.getCurrency("EUR"));

  public static boolean isAllowed(CurrencyUnit currency) {
    return ALLOWED_CURRENCIES.contains(currency);
  }
}
