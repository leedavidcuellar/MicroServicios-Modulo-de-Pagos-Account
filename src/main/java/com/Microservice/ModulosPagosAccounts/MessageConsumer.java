package com.Microservice.ModulosPagosAccounts;

import com.Microservice.ModulosPagosAccounts.dtos.TransactionInfoDTO;
import com.Microservice.ModulosPagosAccounts.services.AccountService;
import com.Microservice.ModulosPagosAccounts.services.InterfaceAccountService;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageConsumer {

    private Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    private TransactionInfoDTO transactionInfoDTO;

    private List<TransactionInfoDTO> transactionInfoDTOList = new ArrayList<>();
    @Autowired
    private InterfaceAccountService interfaceAccountService;

    @JmsListener(destination = "ProcessTransaction")
    public void receiveMessage(String message) {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

                return LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")); }

        }).create();
        transactionInfoDTO = gson.fromJson(message, TransactionInfoDTO.class);
        System.out.println(transactionInfoDTO.getAccountNumberSender());
        System.out.println(transactionInfoDTO.getAmount());
        System.out.println(transactionInfoDTO.getAccountNumberReceiver());
        System.out.println(transactionInfoDTO.getScheduleDate());
        logger.info("MSAccounts: receiver special transaction to process in 2 minutes... ");
        if (interfaceAccountService.updateBalanceAccountSender(transactionInfoDTO)) {
            interfaceAccountService.updateBalanceAccountReceiver(transactionInfoDTO);
            logger.info("The transaction was done");
        } else {
            logger.warn("The transaction was processed");
        }
    }
}



//@Scheduled(cron = "0 */1 * ? * *")
    /*public void checkPaymentType() {
            logger.info("MSAccounts: receiver special transaction to process in 1 minutes... ");
            interfaceAccountService.updateBalanceAccountReceiver(transactionInfoDTO);
        }

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {        return new JsonPrimitive(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(src));    }}).create();String message = gson.toJson(transactionDTO);
    */




