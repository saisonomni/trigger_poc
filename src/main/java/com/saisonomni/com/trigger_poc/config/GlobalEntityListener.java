package com.saisonomni.com.trigger_poc.config;

import com.google.gson.Gson;
import com.saison.omni.ehs.EhsHelper;
import com.saison.omni.ehs.EventConstants;
import com.saison.omni.ehs.MessageCategory;
import com.saisonomni.com.trigger_poc.PublishEventOnUpdate;
import com.saisonomni.com.trigger_poc.event.EhsClientUtil;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class GlobalEntityListener implements PostInsertEventListener {

    @Override
    public void onPostInsert(PostInsertEvent event) {
        Object entity = event.getEntity();
        Class<?> entityClass = entity.getClass();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(PublishEventOnUpdate.class)) {
                field.setAccessible(true);
                try {
                    PublishEventOnUpdate annotation = field.getAnnotation(PublishEventOnUpdate.class);
                    ArrayList< Pair<String,String>> keyValuePairs = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        field.setAccessible(true);
                        jsonObject.put(annotation.keyName(), field.get(entity).toString());
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


//                    int s = keyValuePairs.size();
//                    for(int i=1;i<s;i++){
////                        if(i==0){
////                            JSONObject jsonObject1 = new JSONObject();
////                            jsonObject1.put(keyValuePairs.get(i).getFirst(),keyValuePairs.get(i).getSecond());
////                            jsonObject.put(field.getName(), jsonObject1);
////                        }
//
//
//                        }
//
//                    }
//                    for(int i=s-1;i>=0;i--){
//                        Pair<String,String> p = keyValuePairs.get(i);
//
//                    }


                    sendEventUtility(jsonObject, MessageCategory.DIRECT,"kuch bhi","searchService.send","internal");
                    //ehsClientUtil.sendEventUtility();
                    // Extract the field value (if needed, e.g., for comparison)
                    //check if there is any ref attached to the annotation (annotation.ref)
                    //if yes , then extract those keys as well and insert in the payload
                    //we need to write a dynamic search query to do so
                    // Publish the event
                    //ehsClientUtil.sendEventUtility(new EntityFieldUpdatedEvent(this, annotation.eventName(), value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void sendEventUtility(Object object, MessageCategory category, String serviceName,
                                 String eventType, String destination) {
        try {
            Gson gson = new Gson();
            String eventUrl = "http://localhost:8088";
            String applicationName = "trigger_poc";
            EhsHelper ehsHelper = new EhsHelper(eventUrl, applicationName);
            Map<String, Object> attributes = new HashMap<>(4);
            attributes.put(EventConstants.EVENT_METADATA_EVENT_TYPE, eventType);
            attributes.put(EventConstants.EVENT_METADATA_SOURCE,serviceName);
            attributes.put(EventConstants.REG_METADATA_MESSAGE_TYPE,category);
            attributes.put(EventConstants.PAYLOAD_METADATA_DESTINATION,destination);
            log.info("sending event: {}, eventType: {}, destination: {}, eventUrl: {}", object, eventType, destination, eventUrl);
            ehsHelper.sendEvent(gson.toJson(object),attributes);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }
}