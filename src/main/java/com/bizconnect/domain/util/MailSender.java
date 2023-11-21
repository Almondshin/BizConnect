package com.bizconnect.domain.util;

import com.bizconnect.domain.entity.AdditionalBizInfo;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class MailSender implements Consumer<AdditionalBizInfo> {

    @Override
    public void accept(AdditionalBizInfo additionalBizInfo) {
        // 여기에 메일 전송 로직을 구현합니다.
        if (additionalBizInfo != null) {
            System.out.println("Sending email to BizInfo: " + additionalBizInfo.getBizId());
            // 실제 메일 전송 로직은 이곳에 추가됩니다.
        }
    }
}
