package com.bank.bootcamp.debitcards.dto;

import com.bank.bootcamp.debitcards.entity.AccountType;
import com.bank.bootcamp.debitcards.entity.CustomerType;
import lombok.Data;

@Data
public class CreateDebitCardDTO {

  private String customerId;
  private CustomerType customerType;
  private String mainAccountId;
  private AccountType accountType;
}
