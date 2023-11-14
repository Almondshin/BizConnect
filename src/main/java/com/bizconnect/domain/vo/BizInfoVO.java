package com.bizconnect.domain.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
public class BizInfoVO {

    private String bizId;
    private String mallId;
    private String ceoName;
    private String bizNumber;
    private String email;
    private String phoneNumber;
    private String address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BizInfoVO bizInfoVO = (BizInfoVO) o;
        return Objects.equals(bizId, bizInfoVO.bizId)
                && Objects.equals(mallId, bizInfoVO.mallId)
                && Objects.equals(ceoName, bizInfoVO.ceoName)
                && Objects.equals(bizNumber, bizInfoVO.bizNumber)
                && Objects.equals(email, bizInfoVO.email)
                && Objects.equals(phoneNumber, bizInfoVO.phoneNumber)
                && Objects.equals(address, bizInfoVO.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bizId, mallId, ceoName, bizNumber, email, phoneNumber, address);
    }
}
