package com.saisonomni.com.trigger_poc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PublishEventOnUpsert {
    String eventName();
    String path();
    String keyName();
    String[] ref() default {};
}
