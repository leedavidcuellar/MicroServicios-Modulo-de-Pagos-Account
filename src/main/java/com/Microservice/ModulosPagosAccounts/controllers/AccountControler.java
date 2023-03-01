package com.Microservice.ModulosPagosAccounts.controllers;


import com.Microservice.ModulosPagosAccounts.dtos.TransactionInfoDTO;
import com.Microservice.ModulosPagosAccounts.entities.Account;
import com.Microservice.ModulosPagosAccounts.models.User;
import com.Microservice.ModulosPagosAccounts.services.InterfaceAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utils.Utils;


import java.util.List;


@RestController
@RequestMapping("/api/account")
public class AccountControler {

    @Autowired
    @Qualifier("serviceFeignAccount")
    private InterfaceAccountService interfaceAccountService;

    // Endpoint to create account from user
    @PostMapping ("/createdAccount")
    public ResponseEntity<Object> createdAccount(@RequestBody User user){
     try{
         int a = user.getDni().length();
         if (!(a > 6 && a < 9)) {
             return new ResponseEntity<>("DNI field must have 7 or 8 digits",HttpStatus.NOT_ACCEPTABLE);
         }
         if (!Utils.verifyNumber(user.getDni())) {
             return new ResponseEntity<>("Error in DNI field, please check it only numbers.",HttpStatus.NOT_ACCEPTABLE);
         }
         if (user.getName().isEmpty() || user.getLastName().isEmpty() || user.getDni().isEmpty()
                 || user.getMail().isEmpty() || user.getPassword().isEmpty()) {
             return new ResponseEntity<>("Missing data, please check all fields",HttpStatus.NOT_ACCEPTABLE);
         }
         if(!interfaceAccountService.findAll().isEmpty()) {
             if (interfaceAccountService.findLastUserWithAccount(user.getId()) == 0L) {
                 return new ResponseEntity<>("This User NO exist, check data", HttpStatus.NOT_ACCEPTABLE);
             }
         }

         Account accountNew = interfaceAccountService.createdAccount(user);
         return new ResponseEntity<>(accountNew, HttpStatus.CREATED);

        } catch (Exception ex){
            ex.printStackTrace();
            ex.getMessage();
            return new ResponseEntity<>("Unexpected Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Endpoint to lookingfor account from user
    @GetMapping("/lastAccountCreated/{idUser}")
    public Account lastAccountCreated(@PathVariable Long idUser){
        return interfaceAccountService.LastAccountCreated(idUser);
    }

    // Endpoint to list account from idUser
    @GetMapping("/listAccount/{idUser}")
    public List<Account> listAccountIdUser(@PathVariable Long idUser){
        return interfaceAccountService.findByIdUser(idUser);
    }

    // Endpoint to list all account
    @GetMapping("/list")//spring
    public List<Account> getListAccount(){
        return interfaceAccountService.findAll();
    }

    // Endpoint to list account from id account
    @GetMapping("/detail/{id}")
    public Account detail(@PathVariable Long id){
        return interfaceAccountService.findById(id);
    }

    // Endpoint to list account from cbu account
    @GetMapping("/list/cbu/{cbu}")
    public ResponseEntity<Object> getByCbu(@PathVariable String cbu) {
        if (cbu.length() != 22) {
            return new ResponseEntity<>("CBU must have 22 digits", HttpStatus.NOT_FOUND);
        }
        if (Utils.verifyNumber(cbu) == false) {
            return new ResponseEntity<>("CBU only accept digits numbers", HttpStatus.NOT_FOUND);
        }
        Account account = interfaceAccountService.findByCbu(cbu);
        return new ResponseEntity<>(account, HttpStatus.ACCEPTED);
    }

    // Endpoint to show account from its number account
    @GetMapping("/list/number/{number}")
    public ResponseEntity<Object> getByNumberAccount(@PathVariable String number) {
        if (number.length() != 10) {
            return new ResponseEntity<>("Number Account must have 10 digits", HttpStatus.NOT_FOUND);
        }
        if (Utils.verifyNumber(number) == false) {
            return new ResponseEntity<>("Number Account only accept digits numbers", HttpStatus.NOT_FOUND);
        }
        Account account = interfaceAccountService.findByNumberAccount(number);
        return new ResponseEntity<>(account, HttpStatus.ACCEPTED);
    }

    // Endpoint to delete account from idAccount
    @DeleteMapping("/delete/{idAccount}")
    public ResponseEntity<Object> deleteAccount(@PathVariable Long idAccount){
        if(interfaceAccountService.deleteById(idAccount)) {
            return new ResponseEntity<>("Account Deleted Successfully", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Account no exists or Account have money in balance",HttpStatus.NOT_ACCEPTABLE);
        }
    }

    // Endpoint to Update account balance from transactions
    @PostMapping("/updateBalance")
    public void updateBalance(@RequestBody TransactionInfoDTO transactionInfoDTO){
        interfaceAccountService.updateBalance(transactionInfoDTO);
    }

    // Endpoint to Update account Sender balance from special transactions
    @PostMapping("/updateBalanceAccountSender")
    public void updateBalanceAccountSender(@RequestBody TransactionInfoDTO transactionInfoDTO){
        interfaceAccountService.updateBalanceAccountSender(transactionInfoDTO);
    }
}
