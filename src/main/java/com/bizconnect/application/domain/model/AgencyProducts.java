package com.bizconnect.application.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class AgencyProducts {
  private String rateSel;
  private String name;
  private String price;
  private String offer;
  private String month;
  private String feePerCase;
  private String excessPerCase;
}
