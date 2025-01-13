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
        fieldList = fieldList.stream().filter(field -> {
            field.setAccessible(true);
            try {
                return field.get(entity).toString().compareToIgnoreCase(field.getAnnotation(PublishEventOnDelete.class)
                        .deletedValue())==0;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        if(fieldList.size()==1){
            // publish the payload with type DELETE
            //and return
            HibernateOperationsUtility.deleteHelper(entity,fieldList);
            }
        else{
            HibernateOperationsUtility.upsertHelper(entity);
        }
    }
}
