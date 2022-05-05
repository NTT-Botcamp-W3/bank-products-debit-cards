package com.bank.bootcamp.debitcards.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.bank.bootcamp.debitcards.entity.Account;
import com.bank.bootcamp.debitcards.entity.AccountType;
import reactor.core.publisher.Flux;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {

  Flux<Account> findByDebitCardId(String debitCardId);
  Flux<Account> findByAccountIdAndAccountType(String accountId, AccountType accountType);
}
