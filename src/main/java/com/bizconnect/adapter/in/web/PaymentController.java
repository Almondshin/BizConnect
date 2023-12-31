package com.bizconnect.adapter.in.web;

import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.application.port.in.PaymentUseCase;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = {"/agency/payment/api", "/payment/api"})
public class PaymentController {
    private final PaymentUseCase paymentUseCase;

    public PaymentController(PaymentUseCase paymentUseCase) {
        this.paymentUseCase = paymentUseCase;
    }

    @PostMapping(value = "/encrypt", produces = "application/json;charset=utf-8")
    public String requestEncryptParameters(@RequestBody PaymentDataModel paymentDataModel) {
        paymentUseCase.checkMchtParams(paymentDataModel);
        JSONObject rsp = new JSONObject();
        rsp.put("hashCipher", paymentUseCase.aes256EncryptEcb(paymentDataModel));
        rsp.put("encParams", paymentUseCase.encodeBase64(paymentDataModel));
        return rsp.toString();
    }


}
