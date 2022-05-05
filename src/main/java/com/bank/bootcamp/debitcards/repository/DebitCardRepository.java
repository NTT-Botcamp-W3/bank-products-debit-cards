package com.bank.bootcamp.debitcards.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.bank.bootcamp.debitcards.entity.CustomerType;
import com.bank.bootcamp.debitcards.entity.DebitCard;
import reactor.core.publisher.Mono;

public interface DebitCardRepository extends ReactiveMongoRepository<DebitCard, String> {

  Mono<DebitCard> findByCustomerIdAndCustomerType(String customerId, CustomerType customerType);
}
