package com.bank.bootcamp.debitcards;

import org.junit.jupiter.api.BeforeAll;
import com.bank.bootcamp.debitcards.dto.CreateDebitCardDTO;
import com.bank.bootcamp.debitcards.entity.AccountType;
import com.bank.bootcamp.debitcards.entity.CustomerType;
import com.bank.bootcamp.debitcards.repository.AccountRepository;
import com.bank.bootcamp.debitcards.repository.DebitCardRepository;
import com.bank.bootcamp.debitcards.service.DebitCardService;
import com.bank.bootcamp.debitcards.webclient.AccountWebClient;
import com.bank.bootcamp.debitcards.webclient.CustomerWebClient;

public class DebitCardsApplicationTests {
  
  private static DebitCardService debitCardService;
  private static AccountRepository accountRepository;
  private static DebitCardRepository debitCardRepository;
  private static CustomerWebClient customerWebClient;
  private static AccountWebClient accountWebClient;

	@BeforeAll
	public static void setup() {
	  debitCardService = new DebitCardService(accountRepository, debitCardRepository, customerWebClient, accountWebClient);
	}
	
	public void createDebitCard() {
	  var createDebitCardDTO = new CreateDebitCardDTO();
	  createDebitCardDTO.setAccountType(AccountType.CURRENT);
	  createDebitCardDTO.setCustomerId("PersonalCustomer001");
	  createDebitCardDTO.setCustomerType(CustomerType.PERSONAL);
	  createDebitCardDTO.setMainAccountId("FixedAccount001");
	  debitCardService.createDebitCard(createDebitCardDTO);
	}

}
