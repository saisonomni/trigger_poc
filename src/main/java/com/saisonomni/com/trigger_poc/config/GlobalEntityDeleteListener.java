package com.saisonomni.com.trigger_poc.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.persister.entity.EntityPersister;
@Slf4j
public class GlobalEntityDeleteListener implements PostDeleteEventListener {
    @Override
    public void onPostDelete(PostDeleteEvent event) {
        log.info("Entering post delete listener");
        /*
        handling hard deletes
        * */
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }
}
