package com.saisonomni.com.trigger_poc.config;

import com.google.gson.Gson;
import com.saison.omni.ehs.EhsHelper;
import com.saison.omni.ehs.EventConstants;
import com.saison.omni.ehs.MessageCategory;
import com.saisonomni.com.trigger_poc.CDCEntity;
import com.saisonomni.com.trigger_poc.PublishEventOnDelete;
import com.saisonomni.com.trigger_poc.entity.UpsertValueDTO;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class GlobalEntityUpdateListener implements MergeEventListener {

    @Override
    public void onMerge(MergeEvent event) throws HibernateException {
        try {
            helper(event);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMerge(MergeEvent event, Map copiedAlready) throws HibernateException {
        try {
            helper(event);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    private void helper(MergeEvent event) throws IllegalAccessException, NoSuchFieldException {
        Object entity = event.getEntity();
        if(!entity.getClass().isAnnotationPresent(CDCEntity.class)){
            return;
        }
        Class<?> entityClass = entity.getClass();
        log.info("Entering post delete listener");
        JSONObject jsonObject = new JSONObject();

        /*
        Check if the entity is being soft deleted
        * */
        List<Field> fieldList = Arrays.stream(entityClass.getDeclaredFields()).filter(field ->
                        field.isAnnotationPresent(PublishEventOnDelete.class))
                .collect(Collectors.toList());
        fieldList.stream().filter(field -> {
            field.setAccessible(true);
            try {
                return field.get(entity).toString().compareToIgnoreCase(field.getAnnotation(PublishEventOnDelete.class).deletedValue())==0;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        if(fieldList.size()==1){
            // publish the payload with type DELETE
            //and return
            PublishEventOnDelete annotation = fieldList.get(0).getAnnotation(PublishEventOnDelete.class);
            jsonObject.put("searchIndex", annotation.eventName());
            List<UpsertValueDTO> upsertValueDTOList = new ArrayList<>();
            UpsertValueDTO upsertValueDTO = UpsertValueDTO.builder()
                    .build();
            if (annotation.ref().length > 0){
                List<String> refIdList = new ArrayList<>();
                for(int i=0;i<annotation.ref().length;i++){
                    String path = annotation.ref()[i];
                    if(path.compareToIgnoreCase("#")==0){
                        Field idKey = entityClass.getDeclaredField(annotation.primaryKeyName());
                        refIdList.add(idKey.get(entity).toString());
                        upsertValueDTO.setRef(refIdList);
                        upsertValueDTO.setPath(annotation.path());
                        continue;
                    }
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
                    refIdList.add(tempEntity.toString());
                }
                Collections.reverse(refIdList);
                upsertValueDTO.setRef(refIdList);
                upsertValueDTO.setPath(annotation.path());
            }
            upsertValueDTOList.add(upsertValueDTO);
            jsonObject.put("operation","DELETE");
            jsonObject.put("value",upsertValueDTOList);
            sendEventUtility(jsonObject, MessageCategory.DIRECT,"kuch bhi","searchService.send","internal");
        }
        else{
            jsonObject.put("operation","UPDATE");
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
}
