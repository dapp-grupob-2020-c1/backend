package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.PaymentMethod;
import com.unq.dapp0.c1.comprandoencasa.model.ShopCategory;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class ShopCreationDTO {

    @NotNull
    public String name;
    @NotNull
    public List<ShopCategory> categories;
    @NotNull
    public Location location;
    @NotNull
    public List<DayOfWeek> days;
    @NotNull
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    public LocalTime openingHour;
    @NotNull
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    public LocalTime closingHour;
    @NotNull
    public List<PaymentMethod> paymentMethods;
    @NotNull
    public Integer deliveryRadius;

}
