package com.saisonomni.com.trigger_poc.config;

import com.google.gson.Gson;
import com.saison.omni.ehs.EhsHelper;
import com.saison.omni.ehs.EventConstants;
import com.saison.omni.ehs.MessageCategory;
import com.saisonomni.com.trigger_poc.CDCEntity;
import com.saisonomni.com.trigger_poc.PublishEventOnUpdate;
import com.saisonomni.com.trigger_poc.entity.UpsertValueDTO;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Component
public class GlobalEntityInsertListener implements PostInsertEventListener {

    @Override
    public void onPostInsert(PostInsertEvent event) {
        Object entity = event.getEntity();
        if(!entity.getClass().isAnnotationPresent(CDCEntity.class)){
            return;
        }
        Class<?> entityClass = entity.getClass();
        JSONObject jsonObject = new JSONObject();
        boolean annotationPresent = false;
        List<UpsertValueDTO> upsertValueDTOList = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(PublishEventOnUpdate.class)) {
                annotationPresent = true;
                field.setAccessible(true);
                try {
                    UpsertValueDTO upsertValueDTO = UpsertValueDTO.builder()
                            .build();
                    PublishEventOnUpdate annotation = field.getAnnotation(PublishEventOnUpdate.class);
                    try {
                        field.setAccessible(true);
                        Map<String, Object> dataPairMap =  new HashMap<>();
                        dataPairMap.put(annotation.keyName(), field.get(entity).toString());
                        upsertValueDTO.setDataPairs(dataPairMap);
                        jsonObject.put("searchIndex", annotation.eventName());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (annotation.ref().length > 0) {
                        List<String> refIdList = new ArrayList<>();
                        for (int i = 0; i < annotation.ref().length; i++) {
                            String path = annotation.ref()[i];
                            Object tempEntity = entity;
                            StringTokenizer stringTokenizer = new StringTokenizer(path, ".");
                            Class returnTypeClass = entityClass;
                            while (stringTokenizer.hasMoreTokens()) {
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
                            refIdList.add(tempEntity.toString());
                            upsertValueDTO.setRef(refIdList);
                        }
                    }
                    upsertValueDTO.setPath(annotation.path());
                    upsertValueDTOList.add(upsertValueDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (annotationPresent) {
            jsonObject.put("operation", "UPSERT");
            jsonObject.put("value",upsertValueDTOList);
            sendEventUtility(jsonObject, MessageCategory.DIRECT, "kuch bhi", "searchService.send", "internal");
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