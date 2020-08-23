
package com.crio.warmup.stock.exception;

public class StockQuoteServiceException extends Exception {

  private static final long serialVersionUID = 5531790993728308303L;

  public StockQuoteServiceException(String message) {
    super(message);
  }

  public StockQuoteServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
