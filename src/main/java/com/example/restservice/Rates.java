package com.example.restservice;
import java.util.Map;

public class Rates {
  private boolean success;
  private int timestamp;
  private String base;
  private String date;
  private Map<String, Float> rates;
  public boolean getSuccess() {
    return success;
  }

  public int getTimestamp() {
    return timestamp;
  }

  public String getBase() {
    return base;
  }

  public String getDate() {
    return date;
  }

  public Map<String, Float> getRates() {
    return rates;
  }

  public Float getRate(String currency) {
    return rates.get(currency);
  }

}
