package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.crio.warmup.stock.portfolio.PortfolioManagerImpl;
import com.crio.warmup.stock.quotes.StockQuoteServiceFactory;
import com.crio.warmup.stock.quotes.StockQuotesService;
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

public class CallableReturn implements Callable<AnnualizedReturn> {
  private StockQuotesService service;
  private String symbol;
  private LocalDate startDate;
  private LocalDate endDate;
  private PortfolioTrade trade;
  private AnnualizedReturn an;

  CallableReturn(StockQuotesService service, String symbol, LocalDate startDate, LocalDate endDate,
      PortfolioTrade trade) {
    this.service = service;
    this.symbol = symbol;
    this.endDate = endDate;
    this.startDate = startDate;
    this.trade = trade;
  }

  public AnnualizedReturn call() throws Exception {
    List<Candle> collection = service.getStockQuote(symbol, startDate, endDate);
    Double buyPrice = null;
    Double sellPrice = null;
    LocalDate closePriceDate = null;
    for (Candle t : collection) {
      if (sellPrice == null || t.getDate().isEqual(endDate) 
          || t.getDate().isAfter(closePriceDate)) {
        sellPrice = t.getClose();
        closePriceDate = t.getDate();
      }
      if (t.getDate().isEqual((endDate))) {
        break;
      }
    }
    for (Candle t : collection) {
      if (t.getDate().isEqual(startDate)) {
        buyPrice = t.getOpen();
        break;
      }
    }
    if (sellPrice != null && buyPrice != null) {
      an = PortfolioManagerImpl.calculateAnnualizedReturns(endDate, trade, buyPrice, sellPrice);
    }
    return an;
  }

}