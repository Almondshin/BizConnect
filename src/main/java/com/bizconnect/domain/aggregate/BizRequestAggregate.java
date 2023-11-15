package com.bizconnect.domain.aggregate;

import com.bizconnect.domain.entity.BizInfo;
import com.bizconnect.domain.util.MailSender;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class BizRequestAggregate {
    private BizInfo bizInfo;

    // 메일 전송 이벤트 핸들러
    private Consumer<BizInfo> mailEventHandler = new MailSender();

    public BizRequestAggregate(Consumer<BizInfo> mailEventHandler) {
        this.mailEventHandler = mailEventHandler;
    }

    public void registerBiz(BizInfo newBizInfo) {
        validateBizInfo(newBizInfo);
        this.bizInfo = newBizInfo;
        // 추가적인 비즈니스 로직

        // 메일 전송 이벤트 발생
        if (mailEventHandler != null) {
            mailEventHandler.accept(newBizInfo);
        }
    }


    public BizInfo selectBizInfo(String mallId, String bizId){

        if (bizId == null || bizId.isEmpty()) {
            throw new IllegalArgumentException("Business ID must not be null or empty.");
        }

        if (mallId == null || mallId.isEmpty()) {
            throw new IllegalArgumentException("Mall ID must not be null or empty.");
        }

        // 조회 로직 구현 (예: 데이터베이스에서 해당 ID로 조회)
        return this.bizInfo;
    }

    private void validateBizInfo(BizInfo bizInfo) {
        if (bizInfo == null || bizInfo.getBizId() == null || bizInfo.getBizId().isEmpty()) {
            throw new IllegalArgumentException("Invalid BizInfo provided.");
        }
    }
}

