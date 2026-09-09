package com.tms.dto;

public class EligibilityCheckRequest {

    private Long trainingId;
    private Long officerId;

    public EligibilityCheckRequest() {}

    public EligibilityCheckRequest(Long trainingId, Long officerId) {
        this.trainingId = trainingId;
        this.officerId = officerId;
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public Long getOfficerId() {
        return officerId;
    }

    public void setOfficerId(Long officerId) {
        this.officerId = officerId;
    }
}
