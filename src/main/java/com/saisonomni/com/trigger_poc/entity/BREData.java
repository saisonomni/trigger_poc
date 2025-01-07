package com.saisonomni.com.trigger_poc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.saisonomni.com.trigger_poc.PublishEventOnUpdate;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "BREData")
public class BREData {
    @Id
    private String id;
    @Field("loan_applicant_id")
    @PublishEventOnUpdate(eventName = "cibilFilter",keyName = "appformId")
    String loanApplicantId;
    @Field("success_percentage")
    @PublishEventOnUpdate(eventName = "cibilFilter",keyName = "breMatchPercentage")
    double successPercentage;
}
