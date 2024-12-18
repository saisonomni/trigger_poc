package com.saisonomni.com.trigger_poc.config;

import com.saisonomni.com.trigger_poc.PublishEventOnDelete;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class GlobalEntityUpdateListener implements MergeEventListener {
//    @Override
//    public void onPostUpdate(PostUpdateEvent event) {
//        log.info("Entering post delete listener");
//    }
//
//    @Override
//    public boolean requiresPostCommitHanding(EntityPersister persister) {
//        return false;
//    }

    @Override
    public void onMerge(MergeEvent event) throws HibernateException {
        helper(event);
    }

    @Override
    public void onMerge(MergeEvent event, Map copiedAlready) throws HibernateException {
        helper(event);
    }
    private void helper(MergeEvent event){
        Object entity = event.getEntity();
        Class<?> entityClass = entity.getClass();
        log.info("Entering post delete listener");
        /*
        Handle soft deletes
        * */
        List<Field> fieldList = Arrays.stream(entityClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(PublishEventOnDelete.class))
                .collect(Collectors.toList());
        fieldList.stream().filter(field -> {
            field.setAccessible(true);
            try {
                return field.get(entity).toString().compareToIgnoreCase(field.getAnnotation(PublishEventOnDelete.class).deletedValue())==0;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        if(fieldList.size()>0){
            // publish the payload with type DELETE
            //and return
            return;
        }
    }
}
