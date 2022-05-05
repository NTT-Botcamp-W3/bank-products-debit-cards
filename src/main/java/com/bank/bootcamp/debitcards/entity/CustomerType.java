package com.bank.bootcamp.debitcards.entity;

public enum CustomerType {

  PERSONAL("personal"), 
  BUSINESS("business");
  
  private String resource;
  
  private CustomerType(String resource) {
    this.resource = resource;
  }
  
  public String getResource() {
    return this.resource;
  }
  
}
