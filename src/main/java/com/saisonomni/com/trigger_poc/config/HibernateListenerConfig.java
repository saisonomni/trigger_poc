package com.saisonomni.com.trigger_poc.config;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
public class HibernateListenerConfig {

    public HibernateListenerConfig(EntityManagerFactory entityManagerFactory) {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry()
                .getService(EventListenerRegistry.class);

        // Register the custom listener for PreInsert and PreUpdate events
        GlobalEntityListener globalListener = new GlobalEntityListener();
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(globalListener);
        registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(new GlobalEntityDeleteListener());
        registry.getEventListenerGroup(EventType.MERGE).appendListener(new GlobalEntityUpdateListener());
    }
}
