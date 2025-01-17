package com.saisonomni.com.trigger_poc.config;

import com.google.gson.Gson;
import com.saison.omni.ehs.EhsHelper;
import com.saison.omni.ehs.EventConstants;
import com.saison.omni.ehs.MessageCategory;
import com.saisonomni.com.trigger_poc.PublishEventOnUpsert;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@Component
@Slf4j
public class CustomMongoEventListener extends AbstractMongoEventListener<Object> {

    @Override
    public void onBeforeSave(BeforeSaveEvent<Object> event) {
        Object entity = event.getSource();
        Class<?> entityClass = entity.getClass();
        JSONObject jsonObject = new JSONObject();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(PublishEventOnUpsert.class)) {
                field.setAccessible(true);
                try {
                    PublishEventOnUpsert annotation = field.getAnnotation(PublishEventOnUpsert.class);
                    try {
                        field.setAccessible(true);
                        jsonObject.put(annotation.keyName(), field.get(entity).toString());
                        jsonObject.put("indexName",annotation.eventName());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (annotation.ref().length > 0){
                        for(int i=0;i<annotation.ref().length;i++){
                            String path = annotation.ref()[i];
                            Object tempEntity = entity;
                            StringTokenizer stringTokenizer = new StringTokenizer(path,".");
                            Class returnTypeClass = entityClass;
                            while(stringTokenizer.hasMoreTokens()) {
                                String token = stringTokenizer.nextToken();
                                String methodName = "get" + token.substring(0, 1).toUpperCase() + token.substring(1);
                                Method method = null;
                                try {
                                    method = returnTypeClass.getMethod(methodName);
                                    Object result = method.invoke(tempEntity);
                                    tempEntity = result;
                                } catch (NoSuchMethodException e) {
                                    System.out.println("no such method");
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                returnTypeClass = method.getReturnType();
                            }
                            jsonObject.put("this."+path, tempEntity.toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//        sendEventUtility(jsonObject, MessageCategory.DIRECT,"kuch bhi","searchService.send","internal");
    }
//    public void sendEventUtility(Object object, MessageCategory category, String serviceName,
//                                 String eventType, String destination) {
//        try {
//            Gson gson = new Gson();
//            String eventUrl = "http://localhost:8088";
//            String applicationName = "trigger_poc";
//            EhsHelper ehsHelper = new EhsHelper(eventUrl, applicationName);
//            Map<String, Object> attributes = new HashMap<>(4);
//            attributes.put(EventConstants.EVENT_METADATA_EVENT_TYPE, eventType);
//            attributes.put(EventConstants.EVENT_METADATA_SOURCE,serviceName);
//            attributes.put(EventConstants.REG_METADATA_MESSAGE_TYPE,category);
//            attributes.put(EventConstants.PAYLOAD_METADATA_DESTINATION,destination);
//            log.info("sending event: {}, eventType: {}, destination: {}, eventUrl: {}", object, eventType, destination, eventUrl);
//            ehsHelper.sendEvent(gson.toJson(object),attributes);
//        } catch (RuntimeException e) {
//            log.error(e.getMessage());
//            log.error(Arrays.toString(e.getStackTrace()));
//        }
//    }
}
