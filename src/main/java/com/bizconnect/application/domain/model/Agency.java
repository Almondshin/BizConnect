package com.bizconnect.application.domain.model;
public class Agency {
    /* 제휴사 정보 */
    private String agencyId;
    private String mallId;

    public Agency(String agencyId, String mallId) {
        this.agencyId = agencyId;
        this.mallId = mallId;
    }

    private static boolean isValidAgencyId(String agencyId) {
        String regex = "^[a-zA-Z0-9]+$";
        return agencyId != null && !agencyId.isEmpty() && agencyId.matches(regex);
    }

    public static Agency checkAgency(String agencyId, String mallId){
        if (isValidAgencyId(agencyId)) {
            return new Agency(agencyId, mallId);
        } else {
            throw new IllegalArgumentException("Invalid agencyId: Data format is not correct.");
        }
    }
}
