package com.Microservice.ModulosPagosAccounts.dtos;

import com.Microservice.ModulosPagosAccounts.entities.Account;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountDTO {

    private BigDecimal balance;
    private String cbu;
    private String numberAccount;

    private LocalDate createDate;


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public String getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public AccountDTO (Account account){
        this.balance = account.getBalance();
        this.cbu = account.getCbu();
        this.numberAccount = account.getNumberAccount();
        this.createDate = account.getCreateDate();
    }
}
