package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.objects.ProductType;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public class ThresholdSetDTO {
    @NotEmpty
    public ProductType type;
    @NotEmpty
    public BigDecimal amount;
}
