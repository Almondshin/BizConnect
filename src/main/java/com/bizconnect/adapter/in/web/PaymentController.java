package com.bizconnect.adapter.in.web;

import com.bizconnect.adapter.in.model.PaymentDataModel;
import com.bizconnect.application.port.in.PaymentUseCase;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/hectoFinancial/init")
public class PaymentController {
    private final PaymentUseCase paymentUseCase;

    public PaymentController(PaymentUseCase paymentUseCase) {
        this.paymentUseCase = paymentUseCase;
    }

    @PostMapping(value = "/encrypt", produces = "application/json;charset=utf-8")
    public String requestEncryptParameters(@RequestBody PaymentDataModel paymentDataModel) {
        JSONObject rsp = new JSONObject();
        rsp.put("hashCipher", paymentUseCase.aes256EncryptEcb(paymentDataModel));
        rsp.put("encParams", paymentUseCase.encodeBase64(paymentDataModel));
        return rsp.toString();
    }
}
