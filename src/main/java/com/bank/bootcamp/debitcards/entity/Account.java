package com.bank.bootcamp.debitcards.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "DebitCardAccounts")
@Data
public class Account {

  private String id;
  private String debitCardId;
  private AccountType accountType;
  private String accountId;
}
