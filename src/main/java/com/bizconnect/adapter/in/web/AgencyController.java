package com.bizconnect.adapter.in.web;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.enums.EnumSiteStatus;
import com.bizconnect.application.exceptions.exceptions.DuplicateMemberException;
import com.bizconnect.application.exceptions.exceptions.IllegalAgencyIdSiteIdException;
import com.bizconnect.application.exceptions.exceptions.NullAgencyIdSiteIdException;
import com.bizconnect.application.exceptions.exceptions.ResponseMessage;
import com.bizconnect.application.port.in.AgencyUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.util.ToStringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public ResponseEntity<?> getSiteStatus(@RequestBody ClientDataModel clientDataModel) {
//        agencyUseCase.checkAgencyId(new ClientDataModel(clientDataModel.getAgencyId(), clientDataModel.getSiteId()));
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
    public ResponseEntity<?> regSiteInfo(@RequestBody ClientDataModel clientDataModel) throws IOException, GeneralSecurityException, DuplicateMemberException, NullAgencyIdSiteIdException, IllegalAgencyIdSiteIdException {

        String AES_CBC_256_KEY = "tmT6HUMU+3FW/RR5fxU05PbaZCrJkZ1wP/k6pfZnSj8=";
        String AES_CBC_256_IV = "/SwvI/9aT7RiMmfm8CfP4g==";

        Map<String, String> responseMessage = new HashMap<>();

        byte[] key = Base64.getDecoder().decode(AES_CBC_256_KEY);
        byte[] iv = Base64.getDecoder().decode(AES_CBC_256_IV);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] plainBytes = cipher.doFinal(Base64.getDecoder().decode(clientDataModel.getEncryptData()));

        ObjectMapper objectMapper = new ObjectMapper();
        ClientDataModel info = objectMapper.readValue(new String(plainBytes), ClientDataModel.class);
        System.out.println(info);
        agencyUseCase.registerAgency(info);
        responseMessage.put("resultCode", EnumResultCode.SUCCESS.getCode());
        responseMessage.put("resultMsg", EnumResultCode.SUCCESS.getValue());
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/getPaymentInfo")
    public ResponseEntity<?> getPaymentInfo(@RequestBody ClientDataModel clientDataModel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(clientDataModel);
        Optional<ClientDataModel> info = agencyUseCase.getAgencyInfo(new ClientDataModel(clientDataModel.getAgencyId(), clientDataModel.getSiteId(), clientDataModel.getRateSel(), clientDataModel.getStartDate()));

        //TODO
        // service로 로직 옮기기

        // EnumValues와 ResponseMessage 초기화
        List<Map<String, String>> enumValues = agencyUseCase.getEnumValues();
        Map<String, Object> response = new HashMap<>();

        // info 객체가 비어있지 않은 경우, rateSel과 startDate 값을 추출하여 responseMessage에 넣음
        if (info.isPresent()) {
            ClientDataModel clientInfo = info.get();
            response.put("rateSel", clientInfo.getRateSel()); // rateSel 값을 설정

            if (clientInfo.getStartDate() == null && clientDataModel.getStartDate() != null) {
                response.put("startDate", sdf.format(clientDataModel.getStartDate()));
            } else if (clientInfo.getStartDate() != null) {
                response.put("startDate", sdf.format(clientInfo.getStartDate())); // startDate 값을 설정
            } else {
                response.put("startDate", null);
            }
        }
        // responseMessage에 나머지 정보 추가
        response.put("resultCode", EnumResultCode.SUCCESS.getCode());
        response.put("resultMsg", EnumResultCode.SUCCESS.getValue());
        response.put("siteId", clientDataModel.getSiteId());
        response.put("listSel", enumValues);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/setPaymentSiteInfo")
    public ResponseEntity<?> setPaymentSiteInfo(@RequestBody ClientDataModel clientDataModel) {

        /*
        {
            "agencyId": "agency1",
            "siteId": "test1234",
            "startDate": "2023-12-01",
            "endDate": "2023-12-31",
            "salesPrice": "10000",
            "rateSel": "lite_1m_200",
            "method": "card"
        }
        */
        agencyUseCase.getPaymentInfo(clientDataModel);

        return ResponseEntity.ok(null);
    }

}
