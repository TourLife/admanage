package com.example.admanage.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

public class PlanType {

    //计划类型code
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String typeCode;

    //计划类型名称
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String typeName;

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
