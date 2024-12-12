package com.saisonomni.com.trigger_poc.config;


import com.saisonomni.com.trigger_poc.controller.request.AppformRequest;
import com.saisonomni.com.trigger_poc.entity.Appform;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntityMapper {
    Appform mapToAppform(AppformRequest appformRequest);
}