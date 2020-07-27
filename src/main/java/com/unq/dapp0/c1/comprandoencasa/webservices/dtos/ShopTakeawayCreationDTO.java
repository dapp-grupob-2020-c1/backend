package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;

public class ShopTakeawayCreationDTO {
    @NotBlank
    public Long shopId;
    @NotBlank
    public Collection<Long> shoppingEntryIds;
    @NotBlank
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime turn;
}
