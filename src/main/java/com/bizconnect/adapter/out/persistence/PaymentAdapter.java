package com.bizconnect.adapter.out.persistence;

import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;
import com.bizconnect.adapter.out.persistence.entity.PaymentJpaEntity;
import com.bizconnect.adapter.out.persistence.repository.PaymentHistoryRepository;
import com.bizconnect.application.domain.model.PaymentHistory;
import com.bizconnect.application.port.out.LoadPaymentDataPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentAdapter implements LoadPaymentDataPort {

    private final PaymentHistoryRepository paymentHistoryRepository;

    public PaymentAdapter(PaymentHistoryRepository paymentHistoryRepository) {
        this.paymentHistoryRepository = paymentHistoryRepository;
    }

    @Override
    public Optional<PaymentHistoryDataModel> getPaymentHistory(PaymentHistory paymentHistory) {
        PaymentJpaEntity entity = paymentHistoryConvertToEntity(paymentHistory);
        Optional<PaymentJpaEntity> foundPaymentHistory = paymentHistoryRepository.findByAgencyIdAndSiteIdAndStartDateAndEndDateAndRateSelAndPaymentType(
                entity.getAgencyId(),
                entity.getSiteId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getRateSel(),
                entity.getPaymentType()
        );

        return foundPaymentHistory.map(this::convertToPaymentHistoryDomain);
    }

    private PaymentJpaEntity paymentHistoryConvertToEntity(PaymentHistory paymentHistory) {
        PaymentJpaEntity entity = new PaymentJpaEntity();
        entity.setAgencyId(paymentHistory.getAgencyId());
        entity.setSiteId(paymentHistory.getSiteId());
        entity.setStartDate(paymentHistory.getStartDate());
        entity.setEndDate(paymentHistory.getEndDate());
        entity.setRateSel(paymentHistory.getRateSel());
        entity.setPaymentType(paymentHistory.getPaymentType());
        return entity;
    }


    private PaymentHistoryDataModel convertToPaymentHistoryDomain(PaymentJpaEntity entity) {
        PaymentHistoryDataModel paymentHistoryDataModel = new PaymentHistoryDataModel();
        paymentHistoryDataModel.setTradeNum(entity.getTradeNum());
        paymentHistoryDataModel.setAgencyId(entity.getAgencyId());
        paymentHistoryDataModel.setSiteId(entity.getSiteId());
        paymentHistoryDataModel.setPaymentType(entity.getPaymentType());
        paymentHistoryDataModel.setRateSel(entity.getRateSel());
        paymentHistoryDataModel.setAmount(entity.getAmount());
        paymentHistoryDataModel.setOffer(entity.getOffer());
        paymentHistoryDataModel.setUseCount(entity.getUseCount());
        paymentHistoryDataModel.setTrDate(entity.getTrDate());
        paymentHistoryDataModel.setStartDate(entity.getStartDate());
        paymentHistoryDataModel.setEndDate(entity.getEndDate());
        paymentHistoryDataModel.setRcptName(entity.getRcptName());
        paymentHistoryDataModel.setPaymentStatus(entity.getPaymentStatus());
        paymentHistoryDataModel.setVbankName(entity.getVbankName());
        paymentHistoryDataModel.setVbankAccount(entity.getVbankAccount());
        paymentHistoryDataModel.setVbankExpireDate(entity.getVbankExpireDate());
        paymentHistoryDataModel.setRegDate(entity.getRegDate());
        paymentHistoryDataModel.setModDate(entity.getModDate());
        paymentHistoryDataModel.setMemo(entity.getMemo());
        return paymentHistoryDataModel;
    }

}
