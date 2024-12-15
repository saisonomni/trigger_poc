package com.saisonomni.com.trigger_poc.event;

import com.google.gson.Gson;
import com.saison.omni.common.utility.logger.OmniLogger;
import com.saison.omni.common.utility.logger.OmniLoggerFactory;
import com.saison.omni.ehs.EhsHelper;
import com.saison.omni.ehs.EventConstants;
import com.saison.omni.ehs.MessageCategory;
import com.saison.omni.ehs.dto.ListenerRegistrationUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class EhsClientUtil {

    //If using omni logger which is part of commons. If not, please add your own logger implementation.
    private static final OmniLogger logger = OmniLoggerFactory.getLogger(EhsClientUtil.class);

    @Value("${service.ehs.url}")
    String eventUrl;

    @Value("${spring.application.name}")
    String applicationName;


    public List<ListenerRegistrationUnit> getListenerRegistrationUnit(){
        List<ListenerRegistrationUnit> registrationUnits = new ArrayList<>();
        EventName.EVENTS.forEach((s, messageCategory) -> {
            ListenerRegistrationUnit unit = new ListenerRegistrationUnit(s,messageCategory, null);
            registrationUnits.add(unit);
        });
        return registrationUnits;
    }

    @PostConstruct
    public void executeRegistration(){
        logger.info("passing the following values to ehsHandler :{},{},{},{}", eventUrl,applicationName);
        try {
            EhsHelper ehsHelper = new EhsHelper(eventUrl, applicationName);
            boolean isRegistered =  ehsHelper.registerConsumer(getListenerRegistrationUnit());
            if(!isRegistered){
                logger.error("Error in registration of service.");
            }
        } catch(Exception e){
            logger.info("e");
            e.printStackTrace();
        }
    }

    public void sendEventUtility(Object object, MessageCategory category, String serviceName, String eventType, String destination) {
        try {
           Gson gson = new Gson();
           EhsHelper ehsHelper = new EhsHelper(eventUrl, applicationName);
           Map<String, Object> attributes = new HashMap<>(4);
           attributes.put(EventConstants.EVENT_METADATA_EVENT_TYPE, eventType);
           attributes.put(EventConstants.EVENT_METADATA_SOURCE,serviceName);
           attributes.put(EventConstants.REG_METADATA_MESSAGE_TYPE,category);
           attributes.put(EventConstants.PAYLOAD_METADATA_DESTINATION,destination);
           logger.info("sending event: {}, eventType: {}, destination: {}, eventUrl: {}", object, eventType, destination, eventUrl);
           ehsHelper.sendEvent(gson.toJson(object),attributes);
       } catch (RuntimeException e) {
           logger.error(e.getMessage());
           logger.error(Arrays.toString(e.getStackTrace()));
       }

    }

    public void sendEventUtility(Object object, MessageCategory category, String serviceName, String eventType, String destination, Long delay) {
        try {
            Gson gson = new Gson();
            EhsHelper ehsHelper = new EhsHelper(eventUrl, applicationName);
            Map<String, Object> attributes = new HashMap<>(4);
            attributes.put(EventConstants.EVENT_METADATA_EVENT_TYPE, eventType);
            attributes.put(EventConstants.EVENT_METADATA_SOURCE, serviceName);
            attributes.put(EventConstants.REG_METADATA_MESSAGE_TYPE, category);
            attributes.put(EventConstants.PAYLOAD_METADATA_DESTINATION, destination);
            if (delay != null) {
                attributes.put(EventConstants.DELAY, delay);
            }
            logger.info("sending event: {}, eventType: {}, destination: {}, eventUrl: {}", object, eventType, destination, eventUrl);
            ehsHelper.sendEvent(gson.toJson(object), attributes);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }
}