package com.tms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EligibilityResponse {

    private boolean eligible;
    private String message;
    private String reason;

    public EligibilityResponse() {}

    public EligibilityResponse(boolean eligible, String message) {
        this.eligible = eligible;
        this.message = message;
    }

    public EligibilityResponse(boolean eligible, String message, String reason) {
        this.eligible = eligible;
        this.message = message;
        this.reason = reason;
    }

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
