package com.bank.bootcamp.debitcards.entity;

import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "DebitCards")
public class DebitCard {

  private String id;
  private CustomerType customerType;
  private String customerId;
  private LocalDateTime creationDate;
  private String cardNumber;
}
