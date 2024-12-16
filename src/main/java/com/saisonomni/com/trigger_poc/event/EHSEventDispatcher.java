package com.saisonomni.com.trigger_poc.event;

import com.saison.omni.ehs.dto.EventPayload;
import com.saisonomni.com.trigger_poc.service.AppformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EHSEventDispatcher {
    public void searchServiceSendHandler(EventPayload eventPayload){
        log.info("Inside searchServiceSendHandler with payload : {} ",eventPayload);
    }
}
