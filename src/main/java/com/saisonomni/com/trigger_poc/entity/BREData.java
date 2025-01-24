package com.saisonomni.com.trigger_poc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.saisonomni.searchly_client.cdcConfigs.annotations.CDCEntity;
import com.saisonomni.searchly_client.cdcConfigs.annotations.PublishEventOnDelete;
import com.saisonomni.searchly_client.cdcConfigs.annotations.PublishEventOnUpsert;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "BREData")
@CDCEntity
@PublishEventOnDelete(eventName = "cibilFilter",
        keyName = "is_deleted",
        deletedValue = "true",
        primaryKeyName = "id",
        path = "#",
        ref = {"#"})
public class BREData {
    @Id
    private String id;
    @Field("loan_applicant_id")
    @PublishEventOnUpsert(eventName = "cibilFilter",
            keyName = "loanApplicantId",
            path = "#")
    String loanApplicantId;
    @Field("success_percentage")
    @PublishEventOnUpsert(eventName = "cibilFilter",keyName = "successPercentage",path = "#")
    double successPercentage;
    @Field("is_deleted")
    boolean is_deleted;
}
