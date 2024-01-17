package com.bizconnect.adapter.in.web;

import com.bizconnect.adapter.in.model.ClientDataModel;
import com.bizconnect.application.domain.enums.EnumAgency;
import com.bizconnect.application.exceptions.enums.EnumResultCode;
import com.bizconnect.application.port.in.AgencyUseCase;
import com.bizconnect.application.port.in.EncryptUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = {"/agency", "/"})
public class AgencyController {

    private static final int DAYS_BEFORE_EXPIRATION = 15;
    private final AgencyUseCase agencyUseCase;
    private final EncryptUseCase encryptUseCase;


    @Value("${external.url}")
    private String profileSpecificUrl;

    @Value("${external.payment.url}")
    private String profileSpecificPaymentUrl;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AgencyController(AgencyUseCase agencyUseCase, EncryptUseCase encryptUseCase) {
        this.agencyUseCase = agencyUseCase;
        this.encryptUseCase = encryptUseCase;
    }

    /**
     * 이용기관 등록상태 조회요청
     * 전제 조건: 제휴사는 프로토콜 이용전 제휴사 등록이 완료되어야 함
     *
     * @param clientDataModel {agencyId(제휴사 ID), msgType(등록요청 암호화 메세지타입), encryptData(암호화된 JSON타입 데이터), verifyInfo(HMAC 무결성 검증 데이터)} 전달
     * @return resultCode(응답결과코드), resultMsg(응답상태메세지), msgType(등록요청 암호화 메세지타입), encryptData(암호화된 JSON타입 데이터), verifyInfo(HMAC 무결성 검증 데이터) 전달
     */
    @PostMapping("/getSiteStatus")
    public ResponseEntity<?> getSiteStatus(@RequestBody ClientDataModel clientDataModel) throws GeneralSecurityException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] plainBytes = encryptUseCase.decryptData(clientDataModel.getEncryptData());
        ClientDataModel decryptInfo = objectMapper.readValue(new String(plainBytes), ClientDataModel.class);
        Optional<ClientDataModel> info = agencyUseCase.getAgencyInfo(new ClientDataModel(clientDataModel.getAgencyId(), decryptInfo.getSiteId()));
        String resultCode = EnumResultCode.SUCCESS.getCode();
        String resultMsg = EnumResultCode.SUCCESS.getValue();
        Map<String, String> encryptMapData = new HashMap<>();
        Map<String, String> responseMessage = new HashMap<>();

        logger.info("S ------------------------------[AGENCY] - [getSiteStatus] ------------------------------ S");
        logger.info("[agencyId] : [" + clientDataModel.getAgencyId() + "]");
        logger.info("[siteId] : [" + decryptInfo.getSiteId() + "]");
        logger.info("[client msgType] : [" + clientDataModel.getMsgType() + "]");
        logger.info("[client encryptData] : [" + clientDataModel.getEncryptData() + "]");
        logger.info("[client verifyInfo] : [" + clientDataModel.getVerifyInfo() + "]");

        if (info.isPresent()) {
            ClientDataModel clientInfo = info.get();
            encryptMapData.put("siteId", clientInfo.getSiteId());
            encryptMapData.put("siteStatus", clientInfo.getSiteStatus());
        }

        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("siteId", decryptInfo.getSiteId());

        String encryptedHmacValue = clientDataModel.getVerifyInfo();
        String originalMessage = objectMapper.writeValueAsString(jsonData);
        String keyString = clientDataModel.getAgencyId();

        boolean isVerifiedHmac = verifyHmacSHA256(encryptedHmacValue, originalMessage, keyString);
        boolean isVerifiedMsgType = verifyReceivedMessageType("status", clientDataModel.getMsgType(), keyString);

        String originalData = encryptUseCase.mapToJSONString(encryptMapData);

        responseMessage.put("resultCode", resultCode);
        responseMessage.put("resultMsg", resultMsg);
        responseMessage.put("msgType", "SiteInfo");
        responseMessage.put("encryptData", encryptUseCase.encryptData(Objects.requireNonNull(originalData)));
        responseMessage.put("verifyInfo", encryptUseCase.hmacSHA256(originalData, keyString));

        verifiedHmacAndType(responseMessage, isVerifiedHmac, isVerifiedMsgType);

        logger.info("[resultCode] : [" + responseMessage.get("resultCode") + "]");
        logger.info("[resultMsg] : [" + responseMessage.get("resultMsg") + "]");
        logger.info("[msgType] : [" + responseMessage.get("msgType") + "]");
        logger.info("[response encryptData] : [" + responseMessage.get("encryptData") + "]");
        logger.info("[response verifyInfo] : [" + responseMessage.get("verifyInfo") + "]");
        logger.info("E ------------------------------[AGENCY] - [getSiteStatus] ------------------------------ E");
        return ResponseEntity.ok(responseMessage);
    }

    /**
     * 이용기관 정보등록 요청
     *
     * @param clientDataModel { agencyId(제휴사 ID), msgType(등록요청 암호화 메세지타입), encryptData(암호화된 JSON타입 데이터), verifyInfo(HMAC 무결성 검증 데이터) } 전달
     * @return resultCode (응답결과코드), resultMsg(응답상태메세지), msgType(등록요청 암호화 메세지타입), encryptData(암호화된 JSON타입 데이터), verifyInfo(HMAC 무결성 검증 데이터) 전달
     */
    @PostMapping("/regSiteInfo")
    public ResponseEntity<?> regSiteInfo(@RequestBody ClientDataModel clientDataModel) throws GeneralSecurityException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] plainBytes = encryptUseCase.decryptData(clientDataModel.getEncryptData());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ClientDataModel decryptInfo = objectMapper.readValue(new String(plainBytes), ClientDataModel.class);

        String resultCode = EnumResultCode.SUCCESS.getCode();
        String resultMsg = EnumResultCode.SUCCESS.getValue();
        Map<String, String> responseMessage = new HashMap<>();
        String startDate = decryptInfo.getStartDate() == null ? "" : sdf.format(decryptInfo.getStartDate());

        //HMAC 검증
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("agencyId", decryptInfo.getAgencyId());
        jsonData.put("siteId", decryptInfo.getSiteId());
        jsonData.put("siteName", decryptInfo.getSiteName());
        jsonData.put("companyName", decryptInfo.getCompanyName());
        jsonData.put("businessType", decryptInfo.getBusinessType());
        jsonData.put("bizNumber", decryptInfo.getBizNumber());
        jsonData.put("ceoName", decryptInfo.getCeoName());
        jsonData.put("phoneNumber", decryptInfo.getPhoneNumber());
        jsonData.put("address", decryptInfo.getAddress());
        jsonData.put("companySite", decryptInfo.getCompanySite());
        jsonData.put("email", decryptInfo.getEmail());
        jsonData.put("rateSel", decryptInfo.getRateSel());
        jsonData.put("startDate", startDate);


        String encryptedHmacValue = clientDataModel.getVerifyInfo();
        String originalMessage = objectMapper.writeValueAsString(jsonData);
        String keyString = clientDataModel.getAgencyId();

        //HMAC, MsgType 검증
        boolean isVerifiedHmac = verifyHmacSHA256(encryptedHmacValue, originalMessage, keyString);
        boolean isVerifiedMsgType = verifyReceivedMessageType("reg", clientDataModel.getMsgType(), keyString);

        agencyUseCase.registerAgency(decryptInfo);

        logger.info("S ------------------------------[AGENCY] - [regSiteInfo] ------------------------------ S");
        logger.info("[agencyId] : [" + clientDataModel.getAgencyId() + "]");
        logger.info("[siteId] : [" + decryptInfo.getSiteId() + "]");
        logger.info("[client msgType] : [" + clientDataModel.getMsgType() + "]");
        logger.info("[client encryptData] : [" + clientDataModel.getEncryptData() + "]");
        logger.info("[client verifyInfo] : [" + clientDataModel.getVerifyInfo() + "]");

        logger.info("[Received data] : [" + encryptUseCase.mapToJSONString(jsonData) + "]");

        responseMessage.put("resultCode", resultCode);
        responseMessage.put("resultMsg", resultMsg);

        verifiedHmacAndType(responseMessage, isVerifiedHmac, isVerifiedMsgType);

        logger.info("[resultCode] : [" + responseMessage.get("resultCode") + "]");
        logger.info("[resultMsg] : [" + responseMessage.get("resultMsg") + "]");
        logger.info("E ------------------------------[AGENCY] - [regSiteInfo] ------------------------------ E");
        return ResponseEntity.ok(responseMessage);
    }

    /**
     * 이용기관 해지신청 요청
     *
     * @param clientDataModel {agencyId(제휴사 ID), msgType(등록요청 암호화 메세지타입), encryptData(암호화된 JSON타입 데이터), verifyInfo(HMAC 무결성 검증 데이터)} 전달
     * @return resultCode(응답결과코드), resultMsg(응답상태메세지), msgType(등록요청 암호화 메세지타입), encryptData(암호화된 JSON타입 데이터), verifyInfo(HMAC 무결성 검증 데이터) 전달
     * @throws GeneralSecurityException 보안 관련 예외 발생 시
     * @throws IOException              입출력 관련 예외 발생 시
     */
    @PostMapping("/cancelSiteInfo")
    public ResponseEntity<?> cancelSiteInfo(@RequestBody ClientDataModel clientDataModel) throws GeneralSecurityException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> responseMessage = new HashMap<>();

        byte[] plainBytes = encryptUseCase.decryptData(clientDataModel.getEncryptData());
        ClientDataModel decryptInfo = objectMapper.readValue(new String(plainBytes), ClientDataModel.class);

        agencyUseCase.getAgencyInfo(new ClientDataModel(clientDataModel.getAgencyId(), decryptInfo.getSiteId()));

        logger.info("S ------------------------------[AGENCY] - [cancelSiteInfo] ------------------------------ S");
        logger.info("[agencyId] : [" + clientDataModel.getAgencyId() + "]");
        logger.info("[siteId] : [" + decryptInfo.getSiteId() + "]");
        logger.info("[client msgType] : [" + clientDataModel.getMsgType() + "]");
        logger.info("[client encryptData] : [" + clientDataModel.getEncryptData() + "]");
        logger.info("[client verifyInfo] : [" + clientDataModel.getVerifyInfo() + "]");

        String encryptedHmacValue = clientDataModel.getVerifyInfo();
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("siteId", decryptInfo.getSiteId());

        String originalMessage = objectMapper.writeValueAsString(jsonData);
        String hmacKeyString = clientDataModel.getAgencyId();
        //HMAC, MsgType 검증
        boolean isVerifiedHmac = verifyHmacSHA256(encryptedHmacValue, originalMessage, hmacKeyString);
        boolean isVerifiedMsgType = verifyReceivedMessageType("cancel", clientDataModel.getMsgType(), clientDataModel.getAgencyId());


        //TODO
        // 웹 관리도구로  해당 가맹점을 전달하여, 해지한 가맹점 정보를 Mail로 전달할 수 있도록 요청하는 기능 추가

        responseMessage.put("resultCode", EnumResultCode.SUCCESS.getCode());
        responseMessage.put("resultMsg", EnumResultCode.SUCCESS.getValue());
        verifiedHmacAndType(responseMessage, isVerifiedHmac, isVerifiedMsgType);

        logger.info("[resultCode] : [" + responseMessage.get("resultCode") + "]");
        logger.info("[resultMsg] : [" + responseMessage.get("resultMsg") + "]");
        logger.info("E ------------------------------[AGENCY] - [cancelSiteInfo] ------------------------------ E");
        return ResponseEntity.ok(responseMessage);
    }

    private boolean verifiedHmacAndType(Map<String, String> responseMessage, boolean isVerifiedHmac, boolean isVerifiedMsgType) {

        if (!isVerifiedHmac) {
            System.out.println("HMAC 검증에 실패하였습니다.");
            responseMessage.put("resultMsg", "HMAC 검증에 실패하였습니다.");
            responseMessage.put("resultCode", "9999");
            return true;
        }
        if (!isVerifiedMsgType) {
            System.out.println("MsgType 검증이 실패하였습니다.");
            responseMessage.put("resultMsg", "MsgType 검증이 실패하였습니다.");
            responseMessage.put("resultCode", "9999");
            return true;
        }
        return false;
    }

    /**
     * Hmac SHA256 인증을 검증합니다.
     *
     * @param receivedHmac    수신된 hmac
     * @param originalMessage 원본 메시지
     * @param keyString       hmac 키 문자열
     * @return 인증이 유효한 경우 true, 그렇지 않은 경우 false를 반환
     */
    public boolean verifyHmacSHA256(String receivedHmac, String originalMessage, String keyString) {
        try {
            String calculatedHmac = encryptUseCase.hmacSHA256(originalMessage, keyString);
            return receivedHmac.equals(calculatedHmac);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 수신된 메시지 타입을 제휴사별 메세지 타입과 비교확인합니다.
     *
     * @param messageType     예상 메시지 타입
     * @param receivedMsgType 전달 받은 메시지 유형
     * @param keyString       키값 (제휴사 id)
     * @return 받은 메시지 유형이 예상된 것과 일치하면 true, 그렇지 않으면 false
     */
    public static boolean verifyReceivedMessageType(String messageType, String receivedMsgType, String keyString) {
        try {
            boolean isCancelType = messageType.equals("cancel");
            boolean isRegType = messageType.equals("reg");
            boolean isGetType = messageType.equals("status");
            boolean isSquaresAgency = keyString.equals(EnumAgency.SQUARES.getCode());

            if (isCancelType && isSquaresAgency) {
                return EnumAgency.SQUARES.getCancelMsg().equals(receivedMsgType);
            } else if (isRegType && isSquaresAgency) {
                return EnumAgency.SQUARES.getRegMsg().equals(receivedMsgType);
            } else if (isGetType && isSquaresAgency) {
                return EnumAgency.SQUARES.getStatusMsg().equals(receivedMsgType);
            }

        } catch (Exception e) {
            // 이 오류를 로거를 사용하여 로깅하는 것이 더 좋습니다.
            // logger.error("Error while verifying received message type", e);
            return false;
        }
        return false;
    }




}
