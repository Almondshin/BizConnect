package com.bizconnect.adapter.in.web;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.domain.enums.EnumResultCode;
import com.bizconnect.application.domain.enums.EnumSiteStatus;
import com.bizconnect.application.domain.exceptions.ResponseMessage;
import com.bizconnect.application.port.in.AgencyUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(name = "/agency")
public class AgencyController {

    private final AgencyUseCase agencyUseCase;

    public AgencyController(AgencyUseCase agencyUseCase) {
        this.agencyUseCase = agencyUseCase;
    }

    @PostMapping("/status")
    public ResponseEntity<?> checkAgency(@RequestBody ClientDataModel clientDataModel) {
        agencyUseCase.checkAgencyId(clientDataModel);
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.SUCCESS.getCode(), "Success", EnumSiteStatus.UNREGISTERED.getCode());
        return ResponseEntity.ok(responseMessage);
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerAgency(@RequestBody ClientDataModel clientDataModel, HttpServletRequest request){
        agencyUseCase.registerAgency(clientDataModel);
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.SUCCESS.getCode(), "Success", EnumSiteStatus.PENDING.getCode());
        return ResponseEntity.ok(responseMessage);
    }

}
