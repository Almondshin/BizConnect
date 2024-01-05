package com.bizconnect.adapter.out.persistence;

import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;
import com.bizconnect.adapter.out.persistence.entity.PaymentJpaEntity;
import com.bizconnect.adapter.out.persistence.repository.PaymentHistoryRepository;
import com.bizconnect.application.domain.model.PaymentHistory;
import com.bizconnect.application.port.out.LoadPaymentDataPort;
import com.bizconnect.application.port.out.SavePaymentDataPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service
public class PaymentAdapter implements LoadPaymentDataPort, SavePaymentDataPort {

    private final PaymentHistoryRepository paymentHistoryRepository;

    public PaymentAdapter(PaymentHistoryRepository paymentHistoryRepository) {
        this.paymentHistoryRepository = paymentHistoryRepository;
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public void insertPayment(PaymentHistory paymentHistory) {
        PaymentJpaEntity entity = paymentHistoryConvertToEntity(paymentHistory);
        paymentHistoryRepository.save(entity);
    }

    @Override
    @Transactional
    public void updatePayment(PaymentHistory paymentHistory) {
        Optional<PaymentJpaEntity> optionalEntity = paymentHistoryRepository.findById(paymentHistory.getHfTradeNum());
        if (optionalEntity.isPresent()) {
            paymentHistoryConvertToEntity(paymentHistory);
            System.out.println("update Payment" + paymentHistoryConvertToEntity(paymentHistory));
        } else {
            throw new EntityNotFoundException("hfTradeNum : " + paymentHistory.getHfTradeNum() + "인 엔터티를 찾을 수 없습니다.");
        }
    }

//    @Override
//    @Transactional
//    public void updatePayment(PaymentHistory paymentHistory) {
//        System.out.println("update paymentHistory : " + paymentHistory);
//        Optional<PaymentJpaEntity> optionalEntity = paymentHistoryRepository.findById(paymentHistory.getHfTradeNum());
//        if (optionalEntity.isPresent()) {
//            PaymentJpaEntity entity = optionalEntity.get();
//            PaymentJpaEntity updatedEntity = paymentHistoryConvertToEntity(paymentHistory);
//            if (!updatedEntity.equals(entity)) {
//                paymentHistoryRepository.save(updatedEntity);
//                System.out.println("update Payment" + updatedEntity);
//            }
//        } else {
//            throw new EntityNotFoundException("hfTradeNum : " + paymentHistory.getHfTradeNum() + " 인 엔터티를 찾을 수 없습니다.");
//        }
//    }


    private PaymentJpaEntity paymentHistoryConvertToEntity(PaymentHistory paymentHistory) {
        PaymentJpaEntity entity = new PaymentJpaEntity();
        entity.setTradeNum(paymentHistory.getTradeNum());
        entity.setPgTradeNum(paymentHistory.getHfTradeNum());
        entity.setAgencyId(paymentHistory.getAgencyId());
        entity.setSiteId(paymentHistory.getSiteId());
        entity.setPaymentType(paymentHistory.getPaymentType());
        entity.setRateSel(paymentHistory.getRateSel());
        entity.setAmount(paymentHistory.getAmount());
        entity.setOffer(paymentHistory.getOffer());
        entity.setTrDate(paymentHistory.getTrDate());
        entity.setStartDate(paymentHistory.getStartDate());
        entity.setEndDate(paymentHistory.getEndDate());
        entity.setRcptName(paymentHistory.getRcptName());
        entity.setPaymentStatus(paymentHistory.getPaymentStatus());
        entity.setVbankName(paymentHistory.getVbankName());
        entity.setVbankAccount(paymentHistory.getVbankAccount());
        entity.setVbankExpireDate(paymentHistory.getVbankExpireDate());
        entity.setRegDate(paymentHistory.getRegDate());
        entity.setModDate(paymentHistory.getModDate());
        return entity;
    }


    private PaymentHistoryDataModel convertToPaymentHistoryDomain(PaymentJpaEntity entity) {
        PaymentHistoryDataModel paymentHistoryDataModel = new PaymentHistoryDataModel();
        paymentHistoryDataModel.setTradeNum(entity.getTradeNum());
        paymentHistoryDataModel.setPgTradeNum(entity.getPgTradeNum());
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


