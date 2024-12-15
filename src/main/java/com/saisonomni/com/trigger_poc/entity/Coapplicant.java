package com.saisonomni.com.trigger_poc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static java.util.Objects.isNull;

@Entity
@Data
@Table(name = "coapplicant")
public class Coapplicant {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Appform.class)
    @JoinColumn(name = "appform_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Appform appform;

    @OneToOne(mappedBy = "coapplicant", cascade = CascadeType.ALL, targetEntity = BorrowerDetail.class)
    private BorrowerDetail borrowerDetail;

    @Column(name = "name", nullable = false)
    private String name;

    public void setBorrowerDetail(BorrowerDetail borrowerDetail) {
        if (!isNull(borrowerDetail)) {
            borrowerDetail.setCoapplicant(this);
        }
        this.borrowerDetail = borrowerDetail;
    }
}

