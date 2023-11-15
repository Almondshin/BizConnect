package com.bizconnect.domain.util;

import com.bizconnect.domain.entity.BizInfo;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class MailSender implements Consumer<BizInfo> {

    @Override
    public void accept(BizInfo bizInfo) {
        // 여기에 메일 전송 로직을 구현합니다.
        if (bizInfo != null) {
            System.out.println("Sending email to BizInfo: " + bizInfo.getBizId());
            // 실제 메일 전송 로직은 이곳에 추가됩니다.
        }
    }
}
