package com.saisonomni.com.trigger_poc.event;

import com.google.common.collect.ImmutableMap;
import com.saison.omni.ehs.MessageCategory;
import lombok.Data;

import java.util.Map;


@Data
public final class EventName {
    public static final Map<String, MessageCategory> EVENTS = ImmutableMap.<String,MessageCategory>builder()
            .put("searchService.send",MessageCategory.DIRECT)
            .build();
}