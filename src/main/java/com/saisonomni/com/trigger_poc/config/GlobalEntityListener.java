package com.saisonomni.com.trigger_poc.config;

import com.saison.omni.ehs.MessageCategory;
import com.saisonomni.com.trigger_poc.PublishEventOnUpdate;
import com.saisonomni.com.trigger_poc.event.EhsClientUtil;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

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
                    StringBuilder str = new StringBuilder();
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
                            str.append(path+"->"+tempEntity);
                            str.append("\n");
                        }
                    }
                    System.out.println(str);
//                    ehsClientUtil.sendEventUtility(str, MessageCategory.DIRECT,"kuch bhi","searchService.send","internal");
//                    ehsClientUtil.sendEventUtility();
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

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }
}