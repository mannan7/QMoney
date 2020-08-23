package com.crio.warmup.stock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

// TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
//  Implement the Candle interface in such a way that it matches the
//  parameters returned inside Json response from AlphavantageService.

//Reference - https:www.baeldung.com/jackson-ignore-properties-on-serialization
//Reference - https:www.baeldung.com/jackson-name-of-property
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphavantageCandle implements Candle {
  @JsonProperty("1. open")
  private Double open;
  @JsonProperty("2. high")
  private Double high;
  @JsonProperty("3. low")
  private Double low;
  @JsonProperty("4. close")
  private Double close;
  @JsonIgnore
  private LocalDate date;

  public Double getOpen() {
    return open;
  }

  public void setOpen(Double open) {
    this.open = open;
  }

  public Double getClose() {
    return close;
  }

  public void setClose(Double close) {
    this.close = close;
  }

  public Double getHigh() {
    return high;
  }

  public void setHigh(Double high) {
    this.high = high;
  }

  public Double getLow() {
    return low;
  }

  public void setLow(Double low) {
    this.low = low;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public String toString() {
    return "AlphavantageCandle{" + "open=" + open + ", close=" + close + ", high=" 
      + high + ", low=" + low + ", date="
        + date + '}';
  }

  public AlphavantageCandle() {
  }

  public AlphavantageCandle(Double open, Double high, Double low, Double close, LocalDate date) {
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.date = date;
  }

}
