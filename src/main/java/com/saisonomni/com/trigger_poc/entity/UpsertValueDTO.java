package com.saisonomni.com.trigger_poc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertValueDTO {
    public String path;
    public List<String> ref;
    public Map<String,Object> dataPairs;
}
