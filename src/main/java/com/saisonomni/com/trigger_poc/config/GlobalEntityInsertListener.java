package com.saisonomni.com.trigger_poc.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GlobalEntityInsertListener implements PostInsertEventListener {

    @Override
    public void onPostInsert(PostInsertEvent event) {
        Object entity = event.getEntity();
        HibernateOperationsUtility.upsertHelper(entity);
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