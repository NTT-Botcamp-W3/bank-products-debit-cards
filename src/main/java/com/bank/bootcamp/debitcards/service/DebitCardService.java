package com.bank.bootcamp.debitcards.service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.function.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.bank.bootcamp.debitcards.dto.CreateDebitCardDTO;
import com.bank.bootcamp.debitcards.entity.DebitCard;
import com.bank.bootcamp.debitcards.exception.BankValidationException;
import com.bank.bootcamp.debitcards.repository.AccountRepository;
import com.bank.bootcamp.debitcards.repository.DebitCardRepository;
import com.bank.bootcamp.debitcards.webclient.AccountWebClient;
import com.bank.bootcamp.debitcards.webclient.CustomerWebClient;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DebitCardService {
  
  private final AccountRepository accountRepository;
  private final DebitCardRepository debitCardRepository;
  private final CustomerWebClient customerWebClient;
  private final AccountWebClient accountWebClient;
  private ModelMapper mapper = new ModelMapper();

  public Mono<DebitCard> createDebitCard(CreateDebitCardDTO createDebitCardDTO) {
    return Mono.just(createDebitCardDTO)
      .then(check(createDebitCardDTO, dto -> ObjectUtils.isEmpty(dto), "Action has not data"))
      .then(check(createDebitCardDTO, dto -> ObjectUtils.isEmpty(dto.getAccountType()), "Account type is required"))
      .then(check(createDebitCardDTO, dto -> ObjectUtils.isEmpty(dto.getCustomerId()), "Customer ID is required"))
      .then(check(createDebitCardDTO, dto -> ObjectUtils.isEmpty(dto.getCustomerType()), "Customer type is required"))
      .then(check(createDebitCardDTO, dto -> ObjectUtils.isEmpty(dto.getMainAccountId()), "Main account ID is required"))
      .flatMap(dto -> {
        return customerWebClient.existsCustomer(dto.getCustomerType(), dto.getCustomerId())
            .switchIfEmpty(Mono.error(new BankValidationException("Customer not exists")))
            .<CreateDebitCardDTO>handle((exists, sink) -> {
              if (!exists) {
                sink.error(new BankValidationException("Customer not exists"));
              } else {
                sink.next(dto);
              }
            });
      })
      .flatMap(dto -> {
        return accountWebClient.existsAccount(dto.getAccountType(), dto.getMainAccountId())
            .switchIfEmpty(Mono.error(new BankValidationException("Account not exists")))
            .<CreateDebitCardDTO>handle((exists, sink) -> {
              if (!exists) {
                sink.error(new BankValidationException("Account not exists"));
              } else {
                sink.next(dto);
              }
            });
      })
      .flatMap(dto -> {
        return accountRepository.findByAccountIdAndAccountType(dto.getMainAccountId(), dto.getAccountType())
            .hasElements()
            .<CreateDebitCardDTO>handle((hasElements, sink) -> { 
              if (hasElements)
                sink.error(new BankValidationException("The account is already associated with a debit card"));
              else 
                sink.next(dto);
            });
      })
      .flatMap(dto -> {
        var debitCard = mapper.map(dto, DebitCard.class);
        debitCard.setCardNumber(generateCardNumber());
        debitCard.setCreationDate(LocalDateTime.now());
        return debitCardRepository.save(debitCard);
      });
    
  }
  
  /*
   * https://gist.github.com/josefeg/5781824
   */
  private String generateCardNumber() {
    var random = new Random(System.currentTimeMillis());
    var bin = "3353";
    int randomNumberLength = 16 - (bin.length() + 1);

    var builder = new StringBuilder(bin);
    for (int i = 0; i < randomNumberLength; i++) {
      int digit = random.nextInt(10);
      builder.append(digit);
    }

    int checkDigit = this.getCheckDigit(builder.toString());
    builder.append(checkDigit);
    return builder.toString();
  }

  /*
   * https://gist.github.com/josefeg/5781824
   */
  private int getCheckDigit(String number) {
    int sum = 0;
    for (int i = 0; i < number.length(); i++) {
      int digit = Integer.parseInt(number.substring(i, (i + 1)));
      if ((i % 2) == 0) {
        digit = digit * 2;
        if (digit > 9) {
          digit = (digit / 10) + (digit % 10);
        }
      }
      sum += digit;
    }
    int mod = sum % 10;
    return ((mod == 0) ? 0 : 10 - mod);
  }
  
  private <T> Mono<T> check(T t, Predicate<T> predicate, String messageForException) {
    return Mono.create(sink -> {
      if (predicate.test(t)) {
        sink.error(new BankValidationException(messageForException));
        return;
      } else {
        sink.success(t);
      }
    });
  }
  
//  private <T> Mono<T> validate(T t, Mono<T> mono) {
//    return
//  }

}
