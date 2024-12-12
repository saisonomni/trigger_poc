package com.saisonomni.com.trigger_poc.config;

import com.saisonomni.com.trigger_poc.controller.request.AppformRequest;
import com.saisonomni.com.trigger_poc.entity.Appform;
import com.saisonomni.com.trigger_poc.entity.BorrowerDetail;
import com.saisonomni.com.trigger_poc.entity.Coapplicant;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class AppformMapper {

    public Appform toEntity(AppformRequest request) {
        Appform appform = new Appform();
        appform.setId(request.getId());
        appform.setSanctionedAmount(request.getSanctionedAmount());
        appform.setCoapplicants(request.getCoapplicants().stream().map(this::mapCoapplicant).collect(Collectors.toList()));
        return appform;
    }

    private Coapplicant mapCoapplicant(AppformRequest.CoapplicantRequest request) {
        Coapplicant coapplicant = new Coapplicant();
        coapplicant.setId(request.getId());
        coapplicant.setName(request.getName());

        BorrowerDetail borrowerDetail = new BorrowerDetail();
        borrowerDetail.setId(request.getId()); // Example for unique ID
        borrowerDetail.setCibil(request.getBorrowerDetail().getCibil());

        coapplicant.setBorrowerDetail(borrowerDetail);
        return coapplicant;
    }
}

