package com.Microservice.ModulosPagosAccounts.services;

import com.Microservice.ModulosPagosAccounts.repositories.AccountRepository;
import com.Microservice.ModulosPagosAccounts.dtos.TransactionInfoDTO;
import com.Microservice.ModulosPagosAccounts.entities.Account;
import com.Microservice.ModulosPagosAccounts.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import utils.Utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service ("accountServiceRestTemplate")

public class AccountService implements InterfaceAccountService{

    @Autowired
    private RestTemplate clientRest;
    @Autowired
    private AccountRepository accountRepository;

    private Logger logger = LoggerFactory.getLogger(AccountService.class);

    // Service to create account from user with rest template connect to microservice
    public Account createdAccount(User user){
        Account accountNew;
        do {
            accountNew = new Account(user);
        } while (accountRepository.findByNumberAccount(accountNew.getNumberAccount()) != null
                || accountRepository.findByCbu(accountNew.getCbu()) != null);
        logger.info("MSAccounts RestTemplated: created account ");
        accountRepository.save(accountNew);
        return accountNew;
    }

    // Service to looking for last account created from idUser
    @Transactional(readOnly = true)
    public Account LastAccountCreated(Long idUser){
        logger.info("MSAccounts RestTemplated: find user exists to create account");
        return accountRepository.findAllByIdUserOrderByIdDesc(idUser)
                            .stream().findFirst()
                            .orElse(null);
    }

    // service show a list of all accounts in repository
    @Transactional(readOnly = true) //spring
    public List<Account> findAll(){
        logger.info("MSAccounts RestTemplated: list all account");
        return (List<Account>) accountRepository.findAll();
    }

    // service show account from its id
    @Transactional(readOnly = true)
    public Account findById(Long id){
        logger.info("MSAccounts RestTemplated: find account with id");
        return accountRepository.findById(id).orElse(null);
    }

    // service show a list of accounts from idUser
    @Override
    public List<Account> findByIdUser(Long idUser) {
        logger.info("MSAccounts RestTemplated: find account with idUser");
        return accountRepository.findByIdUserAndIsActive(idUser,Boolean.TRUE);
    }

    // service show account from number Account
    @Override
    public Account findByNumberAccount(String numberAccount) {
        logger.info("MSAccounts RestTemplated: find account with number account");
        return accountRepository.findByNumberAccount(numberAccount);
    }

    @Override
    public Long findLastUserWithAccount(Long idUser) {
        logger.info("MSAccounts RestTemplated: find user exits to create account");
        Map<String,String> pathVariables = new HashMap<String,String>();
        pathVariables.put("id",idUser.toString());
        User auxUser = clientRest.getForObject("http://localhost:8002/detail/{id}",User.class,pathVariables);
        if(auxUser!=null){
            return auxUser.getId();
        }else{
            return 0L;
        }

    }

    // service show account from cbu
    @Override
    public Account findByCbu(String cbu) {
        logger.info("MSAccounts RestTemplated: find account with cbu");
        return accountRepository.findByCbu(cbu);
    }

    // service to delete account from id
    @Override
    public Boolean deleteById(Long idAccount) {
        logger.info("MSAccounts RestTemplated: delete account with id");
        Optional<Account> accountCheck = accountRepository.findById(idAccount);
        if(accountCheck.isPresent()) {
            if(Utils.verifyBalanceAccount(accountCheck.get())){
                accountRepository.delete(accountCheck.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateBalance(TransactionInfoDTO transactionInfoDTO) {
        Account account1 = accountRepository.findByNumberAccount(transactionInfoDTO.getAccountNumberSender());
        Account account2 = accountRepository.findByNumberAccount(transactionInfoDTO.getAccountNumberReceiver());
        BigDecimal total1 = Utils.updateBalance("DEBIT",account1,transactionInfoDTO.getAmount());
        BigDecimal total2 = Utils.updateBalance("CREDIT",account2,transactionInfoDTO.getAmount());
        account1.setBalance(total1);
        account2.setBalance(total2);
        accountRepository.save(account1);
        accountRepository.save(account2);
        logger.info("MSAccounts RestTemplate: updates balances accounts from transaction");
    }

    @Override
    public Boolean updateBalanceAccountSender(TransactionInfoDTO transactionInfoDTO) {
        Account account = accountRepository.findByNumberAccount(transactionInfoDTO.getAccountNumberSender());
        if (account.getBalance().compareTo(BigDecimal.valueOf(transactionInfoDTO.getAmount())) >= 0) {
            BigDecimal total = Utils.updateBalance("DEBIT", account, transactionInfoDTO.getAmount());
            account.setBalance(total);
            accountRepository.save(account);
            logger.info("MSAccounts RestTemplate: update balance account sender and receiver from special transaction");
            return true;
        }else{
            logger.info("MSAccounts RestTemplate: This transaction can't be processed because today you don't have money.");
            return false;
        }
    }

    @Override
    public void updateBalanceAccountReceiver(TransactionInfoDTO transactionInfoDTO) {
        Account account = accountRepository.findByNumberAccount(transactionInfoDTO.getAccountNumberReceiver());
        BigDecimal total = Utils.updateBalance("CREDIT",account,transactionInfoDTO.getAmount());
        account.setBalance(total);
        accountRepository.save(account);
        logger.info("MSAccounts RestTemplate: update balance account receiver from special transaction");
    }
}

/*
clientRest.postForEntity("http://localhost:8002/api/user/deleteAccountToListUser/",accountCheck,Account.class);

Map <String,String> pathVariables = new HashMap<String,String>();
        pathVariables.put("id",idAccount.toString());
        Account account = clientRest.getForObject("http://localhost:8001/detail/{id}",Account.class,pathVariables);

 */