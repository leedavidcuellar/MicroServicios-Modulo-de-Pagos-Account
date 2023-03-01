package com.Microservice.ModulosPagosAccounts.services;

import com.Microservice.ModulosPagosAccounts.dtos.TransactionInfoDTO;
import com.Microservice.ModulosPagosAccounts.entities.Account;
import com.Microservice.ModulosPagosAccounts.models.User;

import java.util.List;

public interface InterfaceAccountService {
    public Account createdAccount(User user);

    public Account LastAccountCreated(Long idUser);

    public List<Account> findAll();

    public Account findById(Long id);

    public List<Account> findByIdUser(Long idUser);

    public Account findByNumberAccount(String numberAccount);

    public Long findLastUserWithAccount(Long idUser);

    public Account findByCbu(String cbu);

    public Boolean deleteById(Long idAccount);

    public void updateBalance(TransactionInfoDTO transactionInfoDTO);

    public Boolean updateBalanceAccountSender(TransactionInfoDTO transactionInfoDTO);

    public void updateBalanceAccountReceiver(TransactionInfoDTO transactionInfoDTO);
}
