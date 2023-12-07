package com.bizconnect.paymentmodule.hectoFinancial.restController;

import com.bizconnect.paymentmodule.config.hectofinancial.Constant;
import com.bizconnect.paymentmodule.utils.EncryptUtil;
import com.bizconnect.paymentmodule.utils.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/hectoFinancial/init")
public class HFInitController {
    @Autowired
    Constant constant;

    Logger logger = LoggerFactory.getLogger("HFInitController");

    @PostMapping(value = "/encrypt", produces = "application/json;charset=utf-8")
    public String requestEncryptParameters(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        /** 설정 정보 얻기 */
        String licenseKey = constant.LICENSE_KEY;
        String aesKey = constant.AES256_KEY;

        /** 해쉬 및 aes256암호화 후 리턴 될 json */
        JSONObject rsp = new JSONObject();

        /** SHA256 해쉬 파라미터 */
        String mchtId = StringUtil.isNull(requestMap.get("mchtId"));
        String method = StringUtil.isNull(requestMap.get("method"));
        String mchtTrdNo = StringUtil.isNull(requestMap.get("mchtTrdNo"));
        String trdDt = StringUtil.isNull(requestMap.get("trdDt"));
        String trdTm = StringUtil.isNull(requestMap.get("trdTm"));
        String trdAmt = StringUtil.isNull(requestMap.get("plainTrdAmt"));

        /** AES256 암호화 파라미터 */
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("trdAmt", trdAmt);
        params.put("mchtCustNm", StringUtil.isNull(requestMap.get("plainMchtCustNm")));
        params.put("cphoneNo", StringUtil.isNull(requestMap.get("plainCphoneNo")));
        params.put("email", StringUtil.isNull(requestMap.get("plainEmail")));
        params.put("mchtCustId", StringUtil.isNull(requestMap.get("plainMchtCustId")));
        params.put("taxAmt", StringUtil.isNull(requestMap.get("plainTaxAmt")));
        params.put("vatAmt", StringUtil.isNull(requestMap.get("plainVatAmt")));
        params.put("taxFreeAmt", StringUtil.isNull(requestMap.get("plainTaxFreeAmt")));
        params.put("svcAmt", StringUtil.isNull(requestMap.get("plainSvcAmt")));
        params.put("clipCustNm", StringUtil.isNull(requestMap.get("plainClipCustNm")));
        params.put("clipCustCi", StringUtil.isNull(requestMap.get("plainClipCustCi")));
        params.put("clipCustPhoneNo", StringUtil.isNull(requestMap.get("plainClipCustPhoneNo")));


        /*============================================================================================================================================
         *  SHA256 해쉬 처리
         *조합 필드 : 상점아이디 + 결제수단 + 상점주문번호 + 요청일자 + 요청시간 + 거래금액(평문) + 라이센스키
         *============================================================================================================================================*/
        String hashPlain = String.format("%s%s%s%s%s%s%s", mchtId, method, mchtTrdNo, trdDt, trdTm, trdAmt, licenseKey);
        String hashCipher = "";
        /** SHA256 해쉬 처리 */
        try {
            hashCipher = EncryptUtil.digestSHA256(hashPlain);//해쉬 값
        }
        catch (Exception e) {
            logger.error("[" + mchtTrdNo + "][SHA256 HASHING] Hashing Fail! : " + e.toString());
        }
        finally {
            logger.info("[" + mchtTrdNo + "][SHA256 HASHING] Plain Text[" + hashPlain + "] ---> Cipher Text[" + hashCipher + "]");
            rsp.put("hashCipher", hashCipher); // sha256 해쉬 결과 저장
        }

        /*============================================================================================================================================
         *  AES256 암호화 처리(AES-256-ECB encrypt -> Base64 encoding)
         *============================================================================================================================================ */
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                String aesPlain = params.get(key);
                if (!("".equals(aesPlain))) {
                    byte[] aesCipherRaw = EncryptUtil.aes256EncryptEcb(aesKey, aesPlain);
                    String aesCipher = EncryptUtil.encodeBase64(aesCipherRaw);

                    params.put(key, aesCipher);//암호화된 데이터로 세팅
                    logger.info("[" + mchtTrdNo + "][AES256 Encrypt] " + key + "[" + aesPlain + "] ---> [" + aesCipher + "]");
                }
            }

        }
        catch (Exception e) {
            logger.error("[" + mchtTrdNo + "][AES256 Encrypt] AES256 Fail! : " + e.toString());
        }
        finally {
            JSONObject encParams = JSONObject.fromObject(params); //aes256 암호화 결과 저장
            rsp.put("encParams", encParams);
        }
        return rsp.toString();
    }
}
