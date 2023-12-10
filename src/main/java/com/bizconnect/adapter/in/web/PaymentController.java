package com.bizconnect.adapter.in.web;

import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.SettleManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PaymentController {

    @PostMapping("/paymentRequest")
    public ResponseEntity<?> paymentRequest(@RequestBody RegistrationDTO registrationDTO, HttpServletRequest request){

        Agency agency = registrationDTO.getAgency();
        Client client = registrationDTO.getClient();
        SettleManager settleManager = registrationDTO.getSettleManager();

        System.out.println("agency : " + agency.getAgencyId() + " " + agency.getMallId());
        System.out.println("client : " + client);
        System.out.println("settleManager : " +settleManager);


        return ResponseEntity.ok().build(); // 데이터가 있을 때 200 반환
    }

}
