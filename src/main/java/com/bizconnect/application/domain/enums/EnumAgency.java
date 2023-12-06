package com.bizconnect.application.domain.enums;

import lombok.Getter;

@Getter
public enum EnumAgency {
    SQUARES("SQUARES", "스퀘어스"),
    CAFE24("CAFE24" , "카페24");

    private final String code;
    private final String value;

    EnumAgency(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
