package com.saisonomni.com.trigger_poc.entity;


import com.saisonomni.searchly_client.cdcConfigs.annotations.CDCEntity;
import com.saisonomni.searchly_client.cdcConfigs.annotations.PublishEventOnDelete;
import com.saisonomni.searchly_client.cdcConfigs.annotations.PublishEventOnUpsert;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

import static java.util.Objects.isNull;

@Entity
@Table(name = "appform")
@Data
@CDCEntity
@PublishEventOnDelete(eventName = "cibilFilter",
        keyName = "isDeleted",
        deletedValue = "true",
        primaryKeyName = "id",
        path = "#",
        ref = {"#"})
public class Appform {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @PublishEventOnUpsert(eventName = "cibilFilter",
            keyName = "appformId",
            path = "#")
    private String id;

    @OneToMany(mappedBy = "appform", cascade = CascadeType.ALL, targetEntity = Coapplicant.class)
    private List<Coapplicant> coapplicants;

    @Column(name = "sanctioned_amount", nullable = false)
    private int sanctionedAmount;

    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted;

    public void setCoapplicants(List<Coapplicant> coapplicants){
        for(Coapplicant coapplicant : coapplicants){
            if (!isNull(coapplicant)) {
                coapplicant.setAppform(this);
            }
        }
        this.coapplicants = coapplicants;
    }

}
