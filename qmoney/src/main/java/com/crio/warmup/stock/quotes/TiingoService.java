
package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.client.RestTemplate;

public class TiingoService implements StockQuotesService {

  private RestTemplate restTemplate;
  private ObjectMapper om = getObjectMapper();


  protected TiingoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Now we will be separating communication with Tiingo from PortfolioManager.
  //  Generate the functions as per the declarations in the interface and then
  //  Move the code from PortfolioManagerImpl#getSTockQuotes inside newly created method.
  //  Run the tests using command below -
  //  ./gradlew test --tests TiingoServiceTest and make sure it passes.

  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws 
      JsonMappingException, JsonProcessingException, StockQuoteServiceException  {
    String uri = buildUri(symbol, from, to);
    String result = restTemplate.getForObject(uri,String.class);
    List<TiingoCandle> candles = null;
    try {
      candles = om.readValue(result,new
      TypeReference<ArrayList<TiingoCandle>>() {}); 
    } catch (JsonMappingException e) {
      throw new StockQuoteServiceException("Error from TiingoService : cannot process json");
    } catch (NullPointerException e) {
      throw new StockQuoteServiceException("Error from TiingoService : Null values received");
    } catch (Exception e) {
      throw new RuntimeException();
    }  

    List<Candle> candleList = new ArrayList<>();
    for (int i = 0; i < candles.size(); i++) {
      candleList.add(candles.get(i));
    }
    
    return candleList;
  }
  
  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }


  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  // Write a method to create appropriate url to call tiingo service.

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    String uriTemplate = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?"
            + "startDate=" + startDate + "&endDate=" + endDate 
            + "&token=f1f0c3563894d72598e398ab2fee37ed9d58c66a";
    return uriTemplate;  
  }

}






  // TODO: CRIO_TASK_MODULE_EXCEPTIONS
  //  Update the method signature to match the signature change in the interface.
  //  Start throwing new StockQuoteServiceException when you get some invalid response from
  //  Tiingo, or if Tiingo returns empty results for whatever reason,
  //  or you encounter a runtime exception during Json parsing.
  //  Make sure that the exception propagates all the way from
  //  PortfolioManager#calculateAnnualisedReturns,
  //  so that the external user's of our API are able to explicitly handle this exception upfront.

  //CHECKSTYLE:OFF



