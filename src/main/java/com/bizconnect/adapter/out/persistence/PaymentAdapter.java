package com.bizconnect.adapter.out.persistence;

import com.bizconnect.adapter.in.model.PaymentHistoryDataModel;
import com.bizconnect.adapter.out.persistence.entity.PaymentJpaEntity;
import com.bizconnect.adapter.out.persistence.repository.PaymentHistoryRepository;
import com.bizconnect.application.domain.enums.EnumPaymentStatus;
import com.bizconnect.application.domain.enums.EnumTradeTrace;
import com.bizconnect.application.domain.model.Agency;
import com.bizconnect.application.domain.model.PaymentHistory;
import com.bizconnect.application.port.out.LoadPaymentDataPort;
import com.bizconnect.application.port.out.SavePaymentDataPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentAdapter implements LoadPaymentDataPort, SavePaymentDataPort {

    private final PaymentHistoryRepository paymentHistoryRepository;

    public PaymentAdapter(PaymentHistoryRepository paymentHistoryRepository) {
        this.paymentHistoryRepository = paymentHistoryRepository;
    }

    @Override
    @Transactional
    public List<PaymentHistoryDataModel> getPaymentHistoryByAgency(Agency agency) {
        List<PaymentJpaEntity> foundPaymentHistory = paymentHistoryRepository.findByAgencyIdAndSiteIdAndTrTrace(agency.getAgencyId(), agency.getSiteId(), EnumTradeTrace.USED.getCode());
        if (foundPaymentHistory == null || foundPaymentHistory.isEmpty()) {
            throw new EntityNotFoundException("Agency siteId : " + agency.getSiteId() + " EnumTradeTrace : "  + EnumTradeTrace.USED.getCode() + " 인 엔터티를 찾을 수 없습니다.");
        } else {
            return foundPaymentHistory.stream()
                    .map(this::convertToDomain)
                    .sorted(Comparator.comparing(PaymentHistoryDataModel::getEndDate).reversed())
                    .collect(Collectors.toList());
        }
    }



    @Override
    @Transactional
    public void insertPayment(PaymentHistory paymentHistory) {
        PaymentJpaEntity entity = convertToEntity(paymentHistory);
        paymentHistoryRepository.save(entity);
    }


    @Override
    @Transactional
    public void updatePayment(PaymentHistory paymentHistory) {
        //persistence context
        Optional<PaymentJpaEntity> optionalEntity = paymentHistoryRepository.findById(paymentHistory.getHfTradeNum());
        if (optionalEntity.isPresent()) {
            updateEntityFields(paymentHistory, optionalEntity.get());
        } else {
            throw new EntityNotFoundException("hfTradeNum : " + paymentHistory.getHfTradeNum() + "인 엔터티를 찾을 수 없습니다.");
        }
    }


    private void updateEntityFields(PaymentHistory paymentHistory, PaymentJpaEntity paymentJpaEntity) {
        paymentJpaEntity.setTradeNum(paymentHistory.getTradeNum());
        paymentJpaEntity.setPgTradeNum(paymentHistory.getHfTradeNum());
        paymentJpaEntity.setAgencyId(paymentHistory.getAgencyId());
        paymentJpaEntity.setSiteId(paymentHistory.getSiteId());
        paymentJpaEntity.setPaymentType(paymentHistory.getPaymentType());
        paymentJpaEntity.setRateSel(paymentHistory.getRateSel());
        paymentJpaEntity.setAmount(paymentHistory.getAmount());
        paymentJpaEntity.setOffer(paymentHistory.getOffer());
        paymentJpaEntity.setPaymentStatus(paymentHistory.getPaymentStatus());
        paymentJpaEntity.setTrDate(paymentHistory.getTrDate());
        paymentJpaEntity.setStartDate(paymentHistory.getStartDate());
        paymentJpaEntity.setEndDate(paymentHistory.getEndDate());
        paymentJpaEntity.setRcptName(paymentHistory.getRcptName());
        paymentJpaEntity.setVbankName(paymentHistory.getVbankName());
        paymentJpaEntity.setVbankAccount(paymentHistory.getVbankAccount());
        paymentJpaEntity.setVbankExpireDate(paymentHistory.getVbankExpireDate());
        paymentJpaEntity.setRegDate(paymentHistory.getRegDate());
        paymentJpaEntity.setModDate(paymentHistory.getModDate());
    }

    private PaymentJpaEntity convertToEntity(PaymentHistory paymentHistory) {
        PaymentJpaEntity entity = new PaymentJpaEntity();
        entity.setTradeNum(paymentHistory.getTradeNum());
        entity.setPgTradeNum(paymentHistory.getHfTradeNum());
        entity.setAgencyId(paymentHistory.getAgencyId());
        entity.setSiteId(paymentHistory.getSiteId());
        entity.setPaymentType(paymentHistory.getPaymentType());
        entity.setRateSel(paymentHistory.getRateSel());
        entity.setAmount(paymentHistory.getAmount());
        entity.setOffer(paymentHistory.getOffer());
        entity.setPaymentStatus(paymentHistory.getPaymentStatus());
        entity.setTrDate(paymentHistory.getTrDate());
        entity.setStartDate(paymentHistory.getStartDate());
        entity.setEndDate(paymentHistory.getEndDate());
        entity.setRcptName(paymentHistory.getRcptName());
        entity.setBillKey(paymentHistory.getBillKey());
        entity.setBillKeyExpireDate(paymentHistory.getBillKeyExpireDate());
        entity.setVbankName(paymentHistory.getVbankName());
        entity.setVbankAccount(paymentHistory.getVbankAccount());
        entity.setVbankExpireDate(paymentHistory.getVbankExpireDate());
        entity.setRegDate(paymentHistory.getRegDate());
        entity.setModDate(paymentHistory.getModDate());
        return entity;
    }


    private PaymentHistoryDataModel convertToDomain(PaymentJpaEntity entity) {
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
        paymentHistoryDataModel.setPaymentStatus(entity.getPaymentStatus());
        paymentHistoryDataModel.setTrDate(entity.getTrDate());
        paymentHistoryDataModel.setStartDate(entity.getStartDate());
        paymentHistoryDataModel.setEndDate(entity.getEndDate());
        paymentHistoryDataModel.setRcptName(entity.getRcptName());
        paymentHistoryDataModel.setVbankName(entity.getVbankName());
        paymentHistoryDataModel.setVbankAccount(entity.getVbankAccount());
        paymentHistoryDataModel.setVbankExpireDate(entity.getVbankExpireDate());
        paymentHistoryDataModel.setRegDate(entity.getRegDate());
        paymentHistoryDataModel.setModDate(entity.getModDate());
        paymentHistoryDataModel.setMemo(entity.getMemo());
        return paymentHistoryDataModel;
    }

}


