package com.example.restservice;

public class Calculator {
  
  public static double calculate(float fromRate, float toRate, double amount) {
    return toRate * amount / fromRate;
  }
  
}
