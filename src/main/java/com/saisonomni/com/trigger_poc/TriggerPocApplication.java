package com.saisonomni.com.trigger_poc;

import com.saisonomni.com.trigger_poc.event.EHSEventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.saison.omni.ehs.EhsClientListener;

@SpringBootApplication
@ComponentScan(basePackages = {"com.saison.omni.ehs","com.saisonomni.com.trigger_poc",
		"com.saison.omni.common",
		"com.saisonomni.searchly_client"})
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
public class TriggerPocApplication {
	@Autowired
	EHSEventDispatcher ehsEventDispatcher;
	@Bean
	EhsClientListener<EHSEventDispatcher> ehsClientListener(){
		return new EhsClientListener<>(ehsEventDispatcher);
	}


	public static void main(String[] args) {
		SpringApplication.run(TriggerPocApplication.class, args);
	}

}
