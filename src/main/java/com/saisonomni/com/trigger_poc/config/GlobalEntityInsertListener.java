package com.saisonomni.com.trigger_poc.config;

import com.google.gson.Gson;
import com.saison.omni.ehs.EhsHelper;
import com.saison.omni.ehs.EventConstants;
import com.saison.omni.ehs.MessageCategory;
import com.saisonomni.com.trigger_poc.CDCEntity;
import com.saisonomni.com.trigger_poc.PublishEventOnUpsert;
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
        HibernateUpsertUtility.helper(entity);
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