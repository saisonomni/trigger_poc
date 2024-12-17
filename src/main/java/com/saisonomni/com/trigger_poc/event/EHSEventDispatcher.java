package com.saisonomni.com.trigger_poc.event;

import com.google.gson.Gson;
import com.saison.omni.ehs.dto.EventPayload;
import com.saisonomni.com.trigger_poc.service.AppformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EHSEventDispatcher {
    @Autowired
    Gson gson;
    public void searchServiceSendHandler(EventPayload eventPayload){
        log.info("Inside searchServiceSendHandler with payload : {} ",eventPayload);
        Map<String,String> mp = gson.fromJson(eventPayload.getPayload(),Map.class);
        List<Map.Entry> list =mp.entrySet().parallelStream()
                .filter(stringStringEntry -> stringStringEntry.getKey().contains("this"))
                .collect(Collectors.toList());
        if(list.size()>0){

        }
        else{

        }

    }
}
