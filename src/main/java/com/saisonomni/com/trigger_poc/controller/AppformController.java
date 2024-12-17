package com.saisonomni.com.trigger_poc.controller;

import com.saisonomni.com.trigger_poc.config.AppformMapper;
import com.saisonomni.com.trigger_poc.config.EntityMapper;
import com.saisonomni.com.trigger_poc.controller.request.AppformRequest;
import com.saisonomni.com.trigger_poc.entity.Appform;
import com.saisonomni.com.trigger_poc.entity.BREData;
import com.saisonomni.com.trigger_poc.service.AppformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppformController {
    @Autowired
    private AppformService appFormService;
    @Autowired
    private AppformMapper appformMapper;
    @PostMapping("/api/appforms")
    public ResponseEntity<Appform> createAppForm(@RequestBody AppformRequest appForm) {
        Appform savedAppForm = appFormService.createAppForm(appformMapper.toEntity(appForm));
        return ResponseEntity.ok(savedAppForm);
    }
    @PostMapping("/api/breData")
    public ResponseEntity<BREData> createAppForm(@RequestBody BREData breData) {
        BREData breDataResponse = appFormService.createBREData(breData);
        return ResponseEntity.ok(breDataResponse);
    }
}

/*
curl -X POST http://localhost:8080/api/appforms \
-H "Content-Type: application/json" \
-d '{
  "id": "appform123",
  "coapplicants": [
    {
      "id": "coapplicant1",
      "name": "John Doe",
      "borrowerDetail": {
        "id": "borrower1",
        "cibil": 750
      }
    },
    {
      "id": "coapplicant2",
      "name": "Jane Smith",
      "borrowerDetail": {
        "id": "borrower2",
        "cibil": 680
      }
    }
  ],
  "sanctionedAmount": 100000
}'
* */