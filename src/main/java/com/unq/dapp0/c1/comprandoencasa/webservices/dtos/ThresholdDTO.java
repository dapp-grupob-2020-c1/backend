package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.objects.ProductType;

import java.math.BigDecimal;

public class ThresholdDTO {
    public ProductType type;
    public BigDecimal threshold;
    public BigDecimal totalForType;

    public ThresholdDTO(){}

    public ThresholdDTO(ProductType productType, BigDecimal threshold, BigDecimal totalFor) {
        this.type = productType;
        this.threshold = threshold;
        this.totalForType = totalFor;
    }
}
