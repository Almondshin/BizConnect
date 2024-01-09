package com.bizconnect.adapter.in.web;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.domain.enums.EnumAgency;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.exceptions.enums.EnumSiteStatus;
import com.bizconnect.application.exceptions.exceptions.DuplicateMemberException;
import com.bizconnect.application.exceptions.exceptions.IllegalAgencyIdSiteIdException;
import com.bizconnect.application.exceptions.exceptions.NullAgencyIdSiteIdException;
import com.bizconnect.application.exceptions.exceptions.handler.ResponseMessage;
import com.bizconnect.application.port.in.AgencyUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = {"/agency", "/"})
public class AgencyController {

    private final AgencyUseCase agencyUseCase;

    @Value("${external.url}")
    private String profileSpecificUrl;

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
        Optional<ClientDataModel> info = agencyUseCase.getAgencyInfo(new ClientDataModel(clientDataModel.getAgencyId(), clientDataModel.getSiteId()));

        String siteStatus = EnumSiteStatus.UNREGISTERED.getCode();
        EnumResultCode resultCode = EnumResultCode.SUCCESS;

        if (info.isPresent()) {
            ClientDataModel clientInfo = info.get();
            siteStatus = clientInfo.getSiteStatus();
            System.out.println("check : " + siteStatus);

            if (Arrays.asList(EnumSiteStatus.PENDING.getCode(),
                            EnumSiteStatus.TELCO_PENDING.getCode(),
                            EnumSiteStatus.SUSPENDED.getCode())
                    .contains(siteStatus)) {
                return ResponseEntity.ok(getResponseMessage(clientInfo));
            }
        }

        ResponseMessage responseMessage = new ResponseMessage(resultCode.getCode(), resultCode.getValue(), siteStatus);
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
    public ResponseEntity<?> regSiteInfo(@RequestBody ClientDataModel clientDataModel) throws GeneralSecurityException, JsonProcessingException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> responseMessage = new HashMap<>();

        byte[] plainBytes = decryptData(clientDataModel.getEncryptData());

        ObjectMapper objectMapper = new ObjectMapper();
        ClientDataModel info = objectMapper.readValue(new String(plainBytes), ClientDataModel.class);

        //HMAC 검증
        String encryptedHmacValue = clientDataModel.getVerifyInfo();
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("agencyId", info.getAgencyId());
        jsonData.put("siteId", info.getSiteId());
        jsonData.put("siteName", info.getSiteName());
        jsonData.put("companyName", info.getCompanyName());
        jsonData.put("businessType", info.getBusinessType());
        jsonData.put("bizNumber", info.getBizNumber());
        jsonData.put("ceoName", info.getCeoName());
        jsonData.put("phoneNumber", info.getPhoneNumber());
        jsonData.put("address", info.getAddress());
        jsonData.put("companySite", info.getCompanySite());
        jsonData.put("email", info.getEmail());
        jsonData.put("rateSel", info.getRateSel());
        String startDate = info.getStartDate() == null ? "" : sdf.format(info.getStartDate());
        jsonData.put("startDate", startDate);

        String originalMessage = objectMapper.writeValueAsString(jsonData);
        String hmacKeyString = clientDataModel.getAgencyId();

        //HMAC, MsgType 검증
        boolean isVerified = verifyHmacSHA256(encryptedHmacValue, originalMessage, hmacKeyString);
        boolean isVerifiedMsgType = verifyMsgType("reg", clientDataModel.getMsgType(), clientDataModel.getAgencyId());

        if (verifiedHmacAndType(responseMessage, isVerified, isVerifiedMsgType)) {
            return ResponseEntity.ok(responseMessage);
        }

        agencyUseCase.registerAgency(info);
        responseMessage.put("resultCode", EnumResultCode.SUCCESS.getCode());
        responseMessage.put("resultMsg", EnumResultCode.SUCCESS.getValue());
        return ResponseEntity.ok(responseMessage);
    }


    /**
     * 결제 정보 요청
     *
     * @param clientDataModel 필수 값 : AgencyId, SiteId , 옵션 값 : RateSel, StartDate
     * @return resultCode, resultMsg, siteId, RateSel list,
     */
    @PostMapping("/getPaymentInfo")
    public ResponseEntity<?> getPaymentInfo(@RequestBody ClientDataModel clientDataModel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Optional<ClientDataModel> optClientInfo = agencyUseCase.getAgencyInfo(new ClientDataModel(clientDataModel.getAgencyId(), clientDataModel.getSiteId(), clientDataModel.getRateSel(), clientDataModel.getStartDate()));
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> productTypes = agencyUseCase.getProductTypes(clientDataModel.getAgencyId());

        if (optClientInfo.isPresent()) {
            ClientDataModel clientInfo = optClientInfo.get();
            String siteStatus = clientInfo.getSiteStatus();

            if (Arrays.asList(EnumSiteStatus.PENDING.getCode(),
                            EnumSiteStatus.TELCO_PENDING.getCode(),
                            EnumSiteStatus.SUSPENDED.getCode())
                    .contains(siteStatus)) {
                return ResponseEntity.ok(getResponseMessage(clientInfo));
            }

            String rateSel = decideRateSel(clientInfo, clientDataModel);
            String startDate = decideStartDate(sdf, clientInfo, clientDataModel);

            response.put("rateSel", rateSel);
            response.put("startDate", startDate);
        }

        response.put("resultCode", EnumResultCode.SUCCESS.getCode());
        response.put("resultMsg", EnumResultCode.SUCCESS.getValue());
        response.put("profileUrl", profileSpecificUrl);
        response.put("siteId", clientDataModel.getSiteId());
        response.put("listSel", productTypes);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancelSiteInfo")
    public ResponseEntity<?> cancelSiteInfo(@RequestBody ClientDataModel clientDataModel) throws GeneralSecurityException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> responseMessage = new HashMap<>();

        byte[] plainBytes = decryptData(clientDataModel.getEncryptSiteId());
        ClientDataModel info = objectMapper.readValue(new String(plainBytes), ClientDataModel.class);
        clientDataModel.setSiteId(info.getSiteId());

        String encryptedHmacValue = clientDataModel.getVerifyInfo();
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("siteId", clientDataModel.getSiteId());

        String originalMessage = objectMapper.writeValueAsString(jsonData);
        String hmacKeyString = clientDataModel.getAgencyId();
        //HMAC, MsgType 검증
        boolean isVerified = verifyHmacSHA256(encryptedHmacValue, originalMessage, hmacKeyString);
        boolean isVerifiedMsgType = verifyMsgType("cancel", clientDataModel.getMsgType(), clientDataModel.getAgencyId());

        if (verifiedHmacAndType(responseMessage, isVerified, isVerifiedMsgType)) {
            return ResponseEntity.ok(responseMessage);
        }

        //웹 관리도구로  해당 가맹점을 전달하여, 해지한 가맹점 정보를 Mail로 전달할 수 있도록 요청하는 기능 추가
        responseMessage.put("resultCode", EnumResultCode.SUCCESS.getCode());
        responseMessage.put("resultMsg", EnumResultCode.SUCCESS.getValue());
        return ResponseEntity.ok(responseMessage);
    }

    private ResponseMessage getResponseMessage(ClientDataModel clientInfo) {
        EnumResultCode resultCode = EnumResultCode.SUCCESS; // 기본값 설정
        if(clientInfo.getSiteStatus().equals(EnumSiteStatus.TELCO_PENDING.getCode())){
            resultCode = EnumResultCode.PendingTelcoApprovalStatus;
        }
        if (clientInfo.getSiteStatus().equals(EnumSiteStatus.PENDING.getCode())){
            resultCode = EnumResultCode.PendingApprovalStatus;
        }
        if (clientInfo.getSiteStatus().equals(EnumSiteStatus.SUSPENDED.getCode())){
            resultCode = EnumResultCode.SuspendedSiteId;
        }
        return new ResponseMessage(resultCode.getCode(), resultCode.getValue(), clientInfo.getSiteStatus());
    }


    private String decideRateSel(ClientDataModel clientInfo, ClientDataModel clientDataModel) {
        return clientDataModel.getRateSel() != null && !clientDataModel.getRateSel().isEmpty() ? clientDataModel.getRateSel() :
                clientInfo.getRateSel() != null ? clientInfo.getRateSel() : null;
    }

    private String decideStartDate(SimpleDateFormat sdf, ClientDataModel clientInfo, ClientDataModel clientDataModel) {
        Date startDateClient = clientDataModel.getStartDate();
        Date startDateInfo = clientInfo.getStartDate();
        if (startDateClient != null && (startDateInfo == null || startDateClient.before(clientInfo.getEndDate()))) {
            return sdf.format(startDateClient);
        } else if (startDateInfo != null) {
            return sdf.format(startDateInfo);
        } else {
            return null;
        }
    }


    private boolean verifiedHmacAndType(Map<String, String> responseMessage, boolean isVerified, boolean isVerifiedMsgType) {
        if (!isVerifiedMsgType) {
            System.out.println("MsgType 검증이 실패하였습니다.");
            responseMessage.put("resultMsg", "MsgType 검증이 실패하였습니다.");
            return true;
        }
        if (!isVerified) {
            System.out.println("HMAC 검증에 실패하였습니다.");
            responseMessage.put("resultMsg", "HMAC 검증에 실패하였습니다.");
            return true;
        }
        return false;
    }

    public static String hmacSHA256(String target, String hmacKeyString) {
        try {
            byte[] hmacKey = hmacKeyString.getBytes(StandardCharsets.UTF_8);

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(hmacKey, "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hashed = sha256_HMAC.doFinal(target.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyHmacSHA256(String receivedHmac, String originalMessage, String hmacKeyString) {
        try {
            String calculatedHmac = hmacSHA256(originalMessage, hmacKeyString);
            return receivedHmac.equals(calculatedHmac);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyMsgType(String type, String receivedMsgType, String agencyId) {
        try {
            if (type.equals("cancel")) {
                if (agencyId.equals(EnumAgency.SQUARES.getCode())) {
                    return EnumAgency.SQUARES.getCancelMsg().equals(receivedMsgType);
                }
            } else if (type.equals("reg")) {
                if (agencyId.equals(EnumAgency.SQUARES.getCode())) {
                    return EnumAgency.SQUARES.getRegMsg().equals(receivedMsgType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    private static byte[] decryptData(String targetDecode) throws GeneralSecurityException, DuplicateMemberException, NullAgencyIdSiteIdException, IllegalAgencyIdSiteIdException {
        String AES_CBC_256_KEY = "tmT6HUMU+3FW/RR5fxU05PbaZCrJkZ1wP/k6pfZnSj8=";
        String AES_CBC_256_IV = "/SwvI/9aT7RiMmfm8CfP4g==";

        byte[] key = Base64.getDecoder().decode(AES_CBC_256_KEY);
        byte[] iv = Base64.getDecoder().decode(AES_CBC_256_IV);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        return cipher.doFinal(Base64.getDecoder().decode(targetDecode));
    }

}
