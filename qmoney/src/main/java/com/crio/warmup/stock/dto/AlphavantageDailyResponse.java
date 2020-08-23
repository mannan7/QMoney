package com.crio.warmup.stock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.TreeMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphavantageDailyResponse {

  @JsonProperty(value = "Time Series (Daily)")
  private TreeMap<String, AlphavantageCandle> candles;

  public TreeMap<String, AlphavantageCandle> getCandles() {
    return candles;
  }

  public void setCandles(TreeMap<String, AlphavantageCandle> candles) {
    this.candles = candles;
  }

  public AlphavantageDailyResponse() {
  }

  public AlphavantageDailyResponse(TreeMap<String, AlphavantageCandle> candles) {
    this.candles = candles;
  }

}