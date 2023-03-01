package com.Microservice.ModulosPagosAccounts.entities;

import com.Microservice.ModulosPagosAccounts.models.User;
import org.hibernate.annotations.GenericGenerator;
import utils.Utils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name="Accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native",strategy = "native")
    private Long id;
    private BigDecimal balance;
    @Column (unique = true)
    private String cbu;
    @Column (unique = true)
    private String numberAccount;
    private LocalDate createDate;
    @Transient
    private User user;
    private Long idUser;
    private Boolean isActive;



// seters and getters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    //constructors

    public Account() {
    }

    public Account(User user) {
        this.balance = new BigDecimal(0.00);
        this.cbu = Utils.generateNumberAleatorio(22);
        this.numberAccount = Utils.generateNumberAleatorio(10);
        this.createDate = LocalDate.now();
        this.user = user;
        this.idUser = user.getId();
        this.isActive = true;
    }

}
