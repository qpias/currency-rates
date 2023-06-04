package com.example.restservice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

@Component
@Scope("singleton")
public class Cache {

  @Value("${key}")
  private String key;
  
  private Rates rates;

  //TODO: check timestamp from Rates and try to refresh on regular intervals, based on the monthly request quota
  public Rates getRates() {
    if (this.rates != null) {
      return this.rates;
    } else {
      WebClient client = WebClient.create();
      Mono<Rates> ratesMono = client.get()
        .uri("http://api.exchangeratesapi.io/v1/latest?access_key=" + key)
        .retrieve()
        .bodyToMono(Rates.class);
      Rates rates = ratesMono
        .share().block();  
      this.rates = rates;
      return this.rates;
    }
  }

}