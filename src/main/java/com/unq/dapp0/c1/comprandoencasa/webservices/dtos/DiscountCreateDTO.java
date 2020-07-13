package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Collection;

public class DiscountCreateDTO {
    @NotBlank
    public Long shopId;
    @NotBlank
    public Double percentage;
    @NotBlank
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate startingDate;
    @NotBlank
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate endingDate;
    @Nullable
    public ProductType productType;
    @Nullable
    public Collection<Long> productsIds;
    @Nullable
    public Long productId;
}
