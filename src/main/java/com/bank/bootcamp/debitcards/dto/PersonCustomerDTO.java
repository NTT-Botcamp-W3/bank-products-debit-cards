package com.bank.bootcamp.debitcards.dto;

import lombok.Data;

@Data
public class PersonCustomerDTO {

  private String id;
  private String documentType;
  private String documentNumber;
  private String name;
  private String lastName;
}
