package com.bizconnect.adapter.in.web;

import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.Client;
import com.bizconnect.application.domain.model.RegistrationDTO;
import com.bizconnect.application.domain.model.SettleManager;
import com.bizconnect.application.port.in.AgencyUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AgencyController {

    private final AgencyUseCase agencyUseCase;

    public AgencyController(AgencyUseCase agencyUseCase) {
        this.agencyUseCase = agencyUseCase;
    }

    @PostMapping("/getAgencySiteStatus")
    public ResponseEntity<?> checkAgency(HttpServletRequest request, @RequestBody Agency agency) {
        Agency result = agencyUseCase.checkAgencyId(agency);
        if (result == null) {
            request.getSession().setAttribute("sessionAgencyId", agency.getAgencyId());
            request.getSession().setAttribute("sessionMallId", agency.getMallId());
            return ResponseEntity.noContent().build(); // 데이터가 없을 때 204 반환
        }
        request.getSession().setAttribute("sessionAgencyId", agency.getAgencyId());
        request.getSession().setAttribute("sessionMallId", agency.getMallId());
        return ResponseEntity.ok().body(agency); // 데이터가 있을 때 200 반환
    }

    @PostMapping("/registerMerchant")
    public ResponseEntity<?> registerMerchant(@RequestBody RegistrationDTO registrationDTO, HttpServletRequest request){

        Agency agency = registrationDTO.getAgency();
        Client client = registrationDTO.getClient();
        SettleManager settleManager = registrationDTO.getSettleManager();

        System.out.println("agency : " + agency.getAgencyId() + " " + agency.getMallId());
        System.out.println("client : " + client);
        System.out.println("settleManager : " +settleManager);


        agencyUseCase.registerAgency(registrationDTO);


        return ResponseEntity.ok().build(); // 데이터가 있을 때 200 반환
    }


}
