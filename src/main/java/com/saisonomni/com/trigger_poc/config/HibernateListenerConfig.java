package com.saisonomni.com.trigger_poc.config;

import jakarta.annotation.PostConstruct;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class HibernateListenerConfig {

    public HibernateListenerConfig(EntityManagerFactory entityManagerFactory) {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry()
                .getService(EventListenerRegistry.class);

        // Register the custom listener for PreInsert and PreUpdate events
        GlobalEntityListener globalListener = new GlobalEntityListener();
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(globalListener);
    }
}
