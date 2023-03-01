package com.Microservice.ModulosPagosAccounts.services;

import com.Microservice.ModulosPagosAccounts.repositories.AccountRepository;
import com.Microservice.ModulosPagosAccounts.clients.UserClientsRest;
import com.Microservice.ModulosPagosAccounts.dtos.TransactionInfoDTO;
import com.Microservice.ModulosPagosAccounts.entities.Account;
import com.Microservice.ModulosPagosAccounts.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service ("serviceFeignAccount")
@Primary
public class AccountServiceFeign implements InterfaceAccountService{
    @Autowired
    private UserClientsRest userClientsRestFeign;
    @Autowired
    private AccountRepository accountRepository;
    private Logger logger = LoggerFactory.getLogger(AccountServiceFeign.class);

    // Service feign to create account from user connect to microservice
    @Override
    public Account createdAccount(User user) {
        Account accountNew;
        do {
            accountNew = new Account(user);
        } while (accountRepository.findByNumberAccount(accountNew.getNumberAccount()) != null
                || accountRepository.findByCbu(accountNew.getCbu()) != null);
        accountRepository.save(accountNew);
        logger.info("MSAccounts Feign: Account Created");
        return accountNew;
    }

    // Service feign to looking for last account created from idUser
    @Override
    public Account LastAccountCreated(Long idUser) {
        logger.info("MSAccounts Feign: last Account created for idUser");
        return accountRepository.findAllByIdUserOrderByIdDesc(idUser)
                .stream().findFirst()
                .orElse(null);
    }

    // service feign show a list of all accounts in repository
    @Override
    public List<Account> findAll() {
        logger.info("MSAccounts Feign: List Account");
        return accountRepository.findAll();
    }

    // service feign show account from its id
    @Override
    public Account findById(Long id) {
        logger.info("MSAccounts Feign: find account with id");
        return accountRepository.findById(id).orElse(null);
    }

    // service feign show a list of accounts from idUser
    @Override
    public List<Account> findByIdUser(Long idUser) {
        logger.info("MSAccounts Feign: find account with idUser");
        return accountRepository.findByIdUserAndIsActive(idUser,Boolean.TRUE);
    }

    // service feign show account from number Account
    @Override
    public Account findByNumberAccount(String numberAccount) {
        logger.info("MSAccounts Feign: find account with number account");
        return accountRepository.findByNumberAccount(numberAccount);
    }

    @Override
    public Long findLastUserWithAccount(Long idUser) {
        logger.info("MSAccounts Feign: find user exists to create account");
        User auxUser = userClientsRestFeign.detail(idUser);
        if(auxUser != null){
            return auxUser.getId();
        }else {
            return 0L;
        }
    }

    // service feign show account from cbu
    @Override
    public Account findByCbu(String cbu) {
        logger.info("MSAccounts Feign: find account with cbu");
        return accountRepository.findByCbu(cbu);
    }

    // service feign to delete account from id
    @Override
    public Boolean deleteById(Long idAccount) {
        Optional<Account> accountCheck = accountRepository.findById(idAccount);
        logger.info("MSAccounts Feign: deleted account by id");
        if(accountCheck.isPresent()) {
            if(Utils.verifyBalanceAccount(accountCheck.get())){
                accountCheck.get().setActive(false);
                accountRepository.save(accountCheck.get());
                return true;
            }
        }
        return false;
    }
    public void updateBalance(TransactionInfoDTO transactionInfoDTO) {
        Account account1 = accountRepository.findByNumberAccount(transactionInfoDTO.getAccountNumberSender());
        Account account2 = accountRepository.findByNumberAccount(transactionInfoDTO.getAccountNumberReceiver());
        BigDecimal total1 = Utils.updateBalance("DEBIT",account1,transactionInfoDTO.getAmount());
        BigDecimal total2 = Utils.updateBalance("CREDIT",account2,transactionInfoDTO.getAmount());
        account1.setBalance(total1);
        account2.setBalance(total2);
        accountRepository.save(account1);
        accountRepository.save(account2);
        logger.info("MSAccounts Feign: updates balances accounts from transaction");
    }

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

    public void updateBalanceAccountReceiver(TransactionInfoDTO transactionInfoDTO) {
        Account account = accountRepository.findByNumberAccount(transactionInfoDTO.getAccountNumberReceiver());
        BigDecimal total = Utils.updateBalance("CREDIT",account,transactionInfoDTO.getAmount());
        account.setBalance(total);
        accountRepository.save(account);
        logger.info("MSAccounts Feign: update balance account receiver from special transaction");
    }
}

    /* List<Account> accountList = new ArrayList<>();
        List<User> userList = userClientsRestFeign.findAll().stream().collect(Collectors.toList());
        for (int i = 0; i < userList.size(); i++) {
            List<Account> optionalAccountList= accountRepository.findByIdUser(userList.get(i).getIdAccount());
            if(!optionalAccountList.isEmpty()) {
                accountList.addAll(optionalAccountList);
            }
        }
        return accountList;
        other example
         userClientsRestFeign.deleteAccountToList(accountCheck.get());
         */