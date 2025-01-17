package com.saisonomni.com.trigger_poc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saisonomni.searchly_client.cdcConfigs.annotations.CDCEntity;
import com.saisonomni.searchly_client.cdcConfigs.annotations.PublishEventOnDelete;
import com.saisonomni.searchly_client.cdcConfigs.annotations.PublishEventOnUpsert;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Data
@Table(name = "borrower_detail")
@CDCEntity
public class BorrowerDetail {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Coapplicant.class)
    @JoinColumn(name = "coapplicant_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Coapplicant coapplicant;

    @Column(name = "cibil", nullable = false)
    @PublishEventOnUpsert(eventName = "field_updated_event",
            keyName = "cibil",
            path = "appform.coapplicants^.borrowerDetail.cibil",//values used here are the field names used in datastore
            ref = {"id","coapplicant.id","coapplicant.appform.id"})//names used in refs are field name used in dto
    private Integer cibil;

    @Column(name = "is_deleted", nullable = false)
    @PublishEventOnDelete(eventName = "field_updated_event",
            keyName = "cibil",
            primaryKeyName = "id",
            ref = {"id","coapplicant.id","coapplicant.appform.id"},//values used here are the field names used in datastore
            path = "appform.coapplicants^.borrowerDetail",//names used in refs are field name used in dto
            deletedValue = "true")
    private boolean is_deleted;

}

