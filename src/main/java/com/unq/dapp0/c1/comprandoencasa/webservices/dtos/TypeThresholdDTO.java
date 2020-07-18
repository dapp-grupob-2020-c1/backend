package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.objects.ProductType;

import java.math.BigDecimal;

public class TypeThresholdDTO {
    public ProductType type;
    public BigDecimal setThreshold;
    public BigDecimal suggestedThreshold;

    public TypeThresholdDTO(){}

    public TypeThresholdDTO(ProductType type, BigDecimal setThreshold, BigDecimal suggestedThreshold) {
        this.type = type;
        this.setThreshold = setThreshold;
        this.suggestedThreshold = suggestedThreshold;
    }
}
