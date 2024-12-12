package com.saisonomni.com.trigger_poc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saisonomni.com.trigger_poc.PublishEventOnUpdate;
import com.saisonomni.com.trigger_poc.config.EntityUpdateListener;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Table(name = "borrower_detail")
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
    @PublishEventOnUpdate(eventName = "field_updated_event",ref = {"BorrowerDetail.id","Coapplicant.id","Appform.id"})
    private Integer cibil;

}

