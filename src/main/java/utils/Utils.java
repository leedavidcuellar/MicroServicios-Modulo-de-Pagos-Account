package utils;

import com.Microservice.ModulosPagosAccounts.entities.Account;

import java.math.BigDecimal;

public final class Utils {

    public Utils() {
    }

    public static String generateNumberAleatorio(int cant){
            String generateNumber = "";
            for(int i = 0; i < cant; i++) {
                int newNumber = (int) (Math.random() * 10);
                generateNumber += String.valueOf(newNumber);
            }
            return generateNumber;
        }

    public static Boolean verifyNumber(String number){
        try {
            Double.parseDouble(number);
            return true;
        }catch (NumberFormatException e){
            e.getMessage();
            return false;
        }
    }

    public static Boolean verifyBalanceAccount(Account account){
        Boolean flag = false;
        if(account.getBalance().doubleValue() == 0.00){
                flag = true;
        }
        return flag;
    }

    public static BigDecimal updateBalance(String type,Account account, Double amount){
        BigDecimal total;

        total = account.getBalance();
        switch (type) {
            case "CREDIT":
                total = total.add(BigDecimal.valueOf(amount));
                break;
            case "DEBIT":
                total =  total.subtract(BigDecimal.valueOf(amount));
                break;
        }
        return total;
    }
}


