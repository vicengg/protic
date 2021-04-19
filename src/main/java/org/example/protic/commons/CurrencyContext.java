package org.example.protic.commons;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;

public class CurrencyContext {

  private static final CurrencyUnit PROTOTYPE_ALLOWED_CURRENCY = Monetary.getCurrency("EUR");
  private static final List<CurrencyUnit> ALLOWED_CURRENCIES = List.of(PROTOTYPE_ALLOWED_CURRENCY);

  public static boolean isAllowed(CurrencyUnit currency) {
    return ALLOWED_CURRENCIES.contains(currency);
  }

  public static CurrencyUnit getPrototypeAllowedCurrency() {
    return PROTOTYPE_ALLOWED_CURRENCY;
  }
}
