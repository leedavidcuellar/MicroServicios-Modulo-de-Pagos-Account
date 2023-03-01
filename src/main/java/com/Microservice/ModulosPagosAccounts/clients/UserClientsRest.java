package com.Microservice.ModulosPagosAccounts.clients;

import com.Microservice.ModulosPagosAccounts.entities.Account;
import com.Microservice.ModulosPagosAccounts.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name ="service-users", url = "localhost:8010")
public interface UserClientsRest {
    @GetMapping("/api/user/list")//para consumir del otro microservicio
    public List<User> findAll();

    @GetMapping("/api/user/detail2/{id}")
    public User detail(@PathVariable Long id);

    @PostMapping("api/user/addAccountUser")
    public ResponseEntity<Object> addAccountUser(@RequestBody Account account);

    @GetMapping("/list/number/{number}")
    public ResponseEntity<Object> getByNumberAccount(@PathVariable String number);

}
