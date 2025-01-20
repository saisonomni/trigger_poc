package com.saisonomni.com.trigger_poc.entity;

import com.saisonomni.com.trigger_poc.controller.request.BorrowerDetailRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntityMapper {
    public BorrowerDetail mapToBorrowerDetail(BorrowerDetailRequest borrowerDetailRequest);
}
