package com.saisonomni.com.trigger_poc.entity;

import com.saisonomni.com.trigger_poc.PublishEventOnUpdate;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

import static java.util.Objects.isNull;

@Entity
@Table(name = "appform")
@Data
public class Appform {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @PublishEventOnUpdate(eventName = "field_updated_event")
    private String id;

    @OneToMany(mappedBy = "appform", cascade = CascadeType.ALL, targetEntity = Coapplicant.class)
    private List<Coapplicant> coapplicants;

    @Column(name = "sanctioned_amount", nullable = false)
    private int sanctionedAmount;

    public void setCoapplicants(List<Coapplicant> coapplicants){
        for(Coapplicant coapplicant : coapplicants){
            if (!isNull(coapplicant)) {
                coapplicant.setAppform(this);
            }
        }
        this.coapplicants = coapplicants;
    }

}
