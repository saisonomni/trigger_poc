package com.saisonomni.com.trigger_poc.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class AppformRequest {
    @JsonProperty("id")
    private String id;

    @JsonProperty("coapplicants")
    private List<CoapplicantRequest> coapplicants;

    @JsonProperty("sanctioned_amount")
    private int sanctionedAmount;

    // Getters and Setters
    @Data
    public static class CoapplicantRequest {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("borrower_detail")
        private BorrowerDetailRequest borrowerDetail;

        // Getters and Setters
    }

    @Data
    public static class BorrowerDetailRequest {
        @JsonProperty("cibil")
        private Integer cibil;

        // Getters and Setters
    }
}

