package com.saisonomni.com.trigger_poc.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saisonomni.com.trigger_poc.entity.Coapplicant;
import lombok.Data;

@Data
public class BorrowerDetailRequest {
    @JsonProperty("id")
    private String id;
    @JsonProperty("coapplicant")
    private Coapplicant coapplicant;
    @JsonProperty("cibil")
    private Integer cibil;
    @JsonProperty("is_deleted")
    private boolean is_deleted;
}
