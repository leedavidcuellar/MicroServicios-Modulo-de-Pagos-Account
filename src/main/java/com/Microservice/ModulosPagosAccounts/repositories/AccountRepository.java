package com.Microservice.ModulosPagosAccounts.repositories;

import com.Microservice.ModulosPagosAccounts.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findAllByIdUserOrderByIdDesc(Long idUser);

    Optional<Account> findByIdUserOrderByIdUserDesc(Long idUser);

    List<Account> findByIdUser(Long idUser);

    List<Account> findByIdUserAndIsActive(Long idUser, Boolean isActive);

    Account findByNumberAccount(String numberAccount);

    Account findByCbu(String cbu);

}
