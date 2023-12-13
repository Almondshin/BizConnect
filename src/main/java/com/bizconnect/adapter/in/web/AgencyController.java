package com.bizconnect.adapter.in.web;

import com.bizconnect.adapter.in.enums.EnumResultCode;
import com.bizconnect.adapter.in.enums.EnumSiteStatus;
import com.bizconnect.adapter.in.exceptions.ClientResponseMessage;
import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.port.in.AgencyUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/agency")
public class AgencyController {

    private final AgencyUseCase agencyUseCase;

    public AgencyController(AgencyUseCase agencyUseCase) {
        this.agencyUseCase = agencyUseCase;
    }

    @PostMapping("/status")
    public ResponseEntity<?> checkAgency(@RequestBody ClientDataModel clientDataModel) {
        agencyUseCase.checkAgencyId(new ClientDataModel(clientDataModel.getAgencyId(), clientDataModel.getMallId()));
        ClientResponseMessage responseMessage = new ClientResponseMessage(EnumResultCode.SUCCESS.getCode(), "Success", EnumSiteStatus.UNREGISTERED.getCode(), clientDataModel.getMallId());
        return ResponseEntity.ok(responseMessage);
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerAgency(@RequestBody ClientDataModel clientDataModel){
        System.out.println("client side : " + clientDataModel);
        agencyUseCase.registerAgency(clientDataModel);
        ClientResponseMessage responseMessage = new ClientResponseMessage(EnumResultCode.SUCCESS.getCode(), "Success", EnumSiteStatus.PENDING.getCode(), clientDataModel.getMallId());
        return ResponseEntity.ok(responseMessage);
    }

}
