package com.bizconnect.adapter.in.web;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.enums.EnumSiteStatus;
import com.bizconnect.application.exceptions.exceptions.DuplicateMemberException;
import com.bizconnect.application.exceptions.exceptions.ResponseMessage;
import com.bizconnect.application.port.in.AgencyUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agency")
public class AgencyController {

    private final AgencyUseCase agencyUseCase;

    public AgencyController(AgencyUseCase agencyUseCase) {
        this.agencyUseCase = agencyUseCase;
    }

    /*
    전제 조건: 제휴사는 프로토콜 이용전 제휴사 등록이 완료되어야 함
    제휴사 제공 데이터: 휴대폰본인확인 제휴사 ID, 이용기관 ID 전달
    제휴사 응답 데이터: 휴대폰본인확인 제휴사 등록정보 상태 전달
    */
    @PostMapping("/getSiteStatus")
    public ResponseEntity<?> checkAgency(@RequestBody ClientDataModel clientDataModel) {
        agencyUseCase.checkAgencyId(new ClientDataModel(clientDataModel.getAgencyId(), clientDataModel.getSiteId()));
        ResponseMessage responseMessage = new ResponseMessage(EnumResultCode.SUCCESS.getCode(), "Success", EnumSiteStatus.UNREGISTERED.getCode(), clientDataModel.getSiteId());
        return ResponseEntity.ok(responseMessage);
    }

    /*
    전제 조건: 제휴사는 이용기관 등록정보중 필수정보를 암호화하여 드림시큐리티에 전송
    제공 데이터: 휴대폰본인확인 이용기관 임시등록 확인
    - 제휴사는 사전에 제휴사 등록이 되어 있어야 한다.
    - 휴대폰본인확인 담당자는 제휴사에게 암호키를 생성하여 전달하여야 한다.
    - 제휴사는 siteId가 중복되지 않도록 요청되어야 한다. 필요시 중복이 되지 않도록 제휴사 PREFIX를 추가하여야 한다.
    - 이용기관 ID는 기본적으로 사이트ID와 동일정보이나 사이트 및 이용기관 등록전에 이용기관ID는 변경을 통해 설정할 수 있다.
    */
    @PostMapping("/regSiteInfo")
    public ResponseEntity<?> registerAgency(@RequestBody ClientDataModel clientDataModel) {
        agencyUseCase.registerAgency(clientDataModel);
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("resultCode", EnumResultCode.SUCCESS.getCode());
        responseMessage.put("resultMsg", EnumResultCode.SUCCESS.getValue());
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/payment/getPaymentInfo")
    public ResponseEntity<?> paymentSiteInfo(@RequestBody ClientDataModel clientDataModel) {
        agencyUseCase.checkAgencyId(new ClientDataModel(clientDataModel.getAgencyId(), clientDataModel.getSiteId()));
        System.out.println(agencyUseCase.checkAgencyId(new ClientDataModel(clientDataModel.getAgencyId(), clientDataModel.getSiteId())));

        List<Map<String, String>> enumValues = agencyUseCase.getEnumValues();

        Map<String, Object> response = new HashMap<>();
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("resultCode", EnumResultCode.SUCCESS.getCode());
        responseMessage.put("resultMsg", EnumResultCode.SUCCESS.getValue());
        responseMessage.put("siteId", clientDataModel.getSiteId());
        responseMessage.put("rateSel", "등록된 상품이 있을 경우 상품 정보 전달 필요 (임시테이블 또는 결제내역 조회)");
        responseMessage.put("startDate", "시작일 조회 필요 (임시테이블 또는 결제내역 조회)");
        response.put("responseMessage", responseMessage);
        response.put("listSel", enumValues);
        return ResponseEntity.ok(response);
    }
}
