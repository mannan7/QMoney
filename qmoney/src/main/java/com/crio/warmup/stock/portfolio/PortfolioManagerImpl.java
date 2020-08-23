package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AlphavantageCandle;
import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.crio.warmup.stock.quotes.StockQuoteServiceFactory;
import com.crio.warmup.stock.quotes.StockQuotesService;
import com.crio.warmup.stock.quotes.TiingoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.naming.TimeLimitExceededException;

import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {
  
  private RestTemplate restTemplate;
  private StockQuotesService stockQuotesService;
  private ObjectMapper objectMapper = getObjectMapper();


  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  // TODO: CRIO_TASK_MODULE_REFACTOR
  // Now we want to convert our code into a module, so we will not call it from main anymore.
  // Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  // into #calculateAnnualizedReturn function here and make sure that it
  // follows the method signature.
  // Logic to read Json file and convert them into Objects will not be required further as our
  // clients will take care of it, going forward.
  // Test your code using Junits provided.
  // Make sure that all of the tests inside PortfolioManagerTest using command below -
  // ./gradlew test --tests PortfolioManagerTest
  // This will guard you against any regressions.
  // run ./gradlew build in order to test yout code, and make sure that
  // the tests and static code quality pass.

  protected PortfolioManagerImpl(RestTemplate restTemplate, 
      StockQuotesService stockQuotesService) {
    this.stockQuotesService = stockQuotesService;
    this.restTemplate = restTemplate;
  }


  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate) throws StockQuoteServiceException {
    List<AnnualizedReturn> annualizedReturns = new ArrayList<AnnualizedReturn>();
    for (PortfolioTrade obj : portfolioTrades) {
      List<Candle> candleList = new ArrayList<>();
      try {
        candleList = stockQuotesService.getStockQuote(obj.getSymbol(), 
            obj.getPurchaseDate(), endDate);
      } catch (JsonProcessingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (StockQuoteServiceException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      TiingoCandle candleObj = (TiingoCandle) candleList.get(candleList.size() - 1);
      Double buyPrice = candleList.get(0).getOpen();
      Double sellPrice = candleObj.getClose();
      Double totalReturn = (sellPrice - buyPrice) / buyPrice; 
      double totalNoOfYears = ChronoUnit.DAYS.between(obj.getPurchaseDate(),endDate) / 365.0;
      Double annualizedReturn = Math.pow((1 + totalReturn),(1.0 / totalNoOfYears)) - 1;
      AnnualizedReturn anRet =  new AnnualizedReturn(obj.getSymbol(),annualizedReturn,totalReturn);
      annualizedReturns.add(anRet);
    }
    Collections.sort(annualizedReturns,getComparator());
    return annualizedReturns;
  }
  
  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  //CHECKSTYLE:OFF

  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
    //calculating total_return
    Double totalReturns = (sellPrice - buyPrice) / buyPrice;
    LocalDate startDate = trade.getPurchaseDate();
    //calculating the period b/w start date and end date
    Double totalNumYears = (double) ChronoUnit.DAYS.between(startDate, endDate);
    //calcualting annualized_returns
    Double annualizedReturns = Math.pow((1 + totalReturns),(365 / totalNumYears)) - 1;
    AnnualizedReturn annualizedReturn = new AnnualizedReturn(trade.getSymbol(),
        annualizedReturns, totalReturns);
    return annualizedReturn;
  }

  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Extract the logic to call Tiingo thirdparty APIs to a separate function.
  //  It should be split into fto parts.
  //  Part#1 - Prepare the Url to call Tiingo based on a template constant,
  //  by replacing the placeholders.
  //  Constant should look like
  //  https://api.tiingo.com/tiingo/daily/<ticker>/prices?startDate=?&endDate=?&token=?
  //  Where ? are replaced with something similar to <ticker> and then actual url produced by
  //  replacing the placeholders with actual parameters.


  public List<TiingoCandle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException, StockQuoteServiceException {
      String uri = buildUri(symbol, from, to);
      String result = (restTemplate.getForObject(uri,String.class));
      List<TiingoCandle> candleList = objectMapper.readValue(result,
                        new TypeReference<List<TiingoCandle>>() {});
      return candleList;
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    String uriTemplate = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?"
            + "startDate=" + startDate + "&endDate=" + endDate 
            + "&token=f1f0c3563894d72598e398ab2fee37ed9d58c66a";
    return uriTemplate;  
  }

  public PortfolioManagerImpl(StockQuotesService stockQuotesService) {
    this.stockQuotesService = stockQuotesService;
  }


  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturnParallel(
      List<PortfolioTrade> portfolioTrades,
      LocalDate endDate, int numThreads) throws InterruptedException,
      StockQuoteServiceException{
    List<AnnualizedReturn> annualizedReturnList = new ArrayList<AnnualizedReturn>();
    ExecutorService executor = Executors.newFixedThreadPool(numThreads);
    List<Future<AnnualizedReturn>> list = new ArrayList<Future<AnnualizedReturn>> ();
    // Future<PortfolioTrade> future = executor.submit(new CallableReturn(service,symbol,startDate,endDate));
    // List<Future<PortfolioTrade>> list = new ArrayList<Future<PortfolioTrade>> ();
    for (PortfolioTrade trade : portfolioTrades) { 
      LocalDate startDate = trade.getPurchaseDate();
      if(startDate.isAfter(endDate)){
        throw new RuntimeException();
      }
      String symbol = trade.getSymbol();

      Future<AnnualizedReturn> future = executor.submit(new CallableReturn(stockQuotesService,symbol,startDate,endDate,trade));
      list.add(future);
    }
    for (Future<AnnualizedReturn> f : list){
      try{
        annualizedReturnList.add(f.get());
      } catch (InterruptedException e){
        throw new StockQuoteServiceException("msg");
      } catch (ExecutionException e){
        throw new StockQuoteServiceException("message");
      } catch (Exception e){
        throw new StockQuoteServiceException("message");
      }
    }
    
    Collections.sort(annualizedReturnList, getComparator());
    return annualizedReturnList;
    
  }


  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Modify the function #getStockQuote and start delegating to calls to
  //  stockQuoteService provided via newly added constructor of the class.
  //  You also have a liberty to completely get rid of that function itself, however, make sure
  //  that you do not delete the #getStockQuote function.
  
}
