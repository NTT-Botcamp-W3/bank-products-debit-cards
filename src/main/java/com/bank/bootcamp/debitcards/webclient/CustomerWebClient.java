package com.bank.bootcamp.debitcards.webclient;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;
import com.bank.bootcamp.debitcards.entity.CustomerType;
import reactor.core.publisher.Mono;

public class CustomerWebClient {
  private final ReactiveCircuitBreaker reactiveCircuitBreaker;
  private WebClient webClient;
  
  public CustomerWebClient(ReactiveResilience4JCircuitBreakerFactory reactiveCircuitBreakerFactory, Environment env) {
    this.reactiveCircuitBreaker = reactiveCircuitBreakerFactory.create("products");
    webClient = WebClient.create(env.getProperty("gateway.url"));
  }
  
  public Mono<Boolean> existsCustomer(CustomerType customerType, String customerId) {

    return webClient.get()
        .uri(String.format("/%s/exists/{customerId}", customerType.getResource()), customerId)
        .retrieve()
        .bodyToMono(Boolean.class)
        .transform(balance -> reactiveCircuitBreaker.run(balance, throwable -> Mono.just(Boolean.FALSE)));
    
  }
  
}
