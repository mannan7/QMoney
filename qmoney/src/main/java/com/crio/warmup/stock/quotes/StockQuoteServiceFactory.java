
package com.crio.warmup.stock.quotes;

import org.springframework.web.client.RestTemplate;

public enum StockQuoteServiceFactory {

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Its only read task - understand what we are doing before we use them.
  //  Pro's and Con's of implementing singleton via enum -
  //  https://softwareengineering.stackexchange.com/q/179386/253205
  INSTANCE;

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Make sure that you have implemented TiingoService and AlphavantageService
  //  as per the instructions and the tests are passing for them.
  //  Implement the factory function such that it will return TiingoService instance when
  //  provider == 'tiingo' (Case insensitive)
  //  Return new instance of AlphavantageService in all other cases.
  //  RestTemplate is passed as a parameter along, and you will have to pass the same to
  //  the constructor of corresponding class.
  //  Run the tests using command below and make sure it passes
  //  ./gradlew test --tests StockQuoteServiceFactory

  public static StockQuotesService getService(String provider, RestTemplate restTemplate) {
    if (provider.equalsIgnoreCase("tiingo")) {
      return new TiingoService(restTemplate);
    }

    return new AlphavantageService(restTemplate);

  }
}
