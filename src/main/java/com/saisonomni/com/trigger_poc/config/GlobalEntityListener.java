package com.saisonomni.com.trigger_poc.config;

import com.saisonomni.com.trigger_poc.PublishEventOnUpdate;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

import java.lang.reflect.Field;

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
                    // Extract the field value (if needed, e.g., for comparison)
                    Object value = field.get(entity);
                    //check if there is any ref attached to the annotation (annotation.ref)
                    //if yes , then extract those keys as well and insert in the payload
                    //we need to write a dynamic search query to do so
                    // Publish the event
//                    ehsClientUtil.sendEventUtility(new EntityFieldUpdatedEvent(this, annotation.eventName(), value));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }
}