package com.example.restservice;

import static java.util.Objects.isNull;
import java.util.Map;
import java.util.HashMap;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class RateController {

	@Autowired
	Cache cache;

	@GetMapping("/rates")
	public Map<String, Double> greeting(
		  @RequestParam(value = "from", defaultValue = "EUR") String from,
		  @RequestParam(value = "to", defaultValue = "EUR") String to,
		  @RequestParam(value = "amount", defaultValue = "1") double amount,
		  HttpServletResponse response
		) throws Exception {
		System.out.println(from + " " + to + " " + amount);
		long startTime = System.nanoTime();
		Rates rates = cache.getRates();
		Float fromRate = rates.getRate(from);
		if (isNull(fromRate)) throw new Exception("Incorrect fromRate");
		Float toRate = rates.getRate(to);
		if (isNull(toRate)) throw new Exception("Incorrect toRate");
		double result = Calculator.calculate(fromRate, toRate, amount);
		long endTime = System.nanoTime();
    long relapsedTime = endTime - startTime;
		Map<String, Double> resultMap = new HashMap<String, Double>();
		resultMap.put("result", result);
		response.setHeader("Server-Timing", "calc;desc=\"Calculation\";dur=" + relapsedTime);
		return resultMap;
	}

	@ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<String> handleException(
      Exception exception
  ) {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(exception.getMessage());
  }
}
