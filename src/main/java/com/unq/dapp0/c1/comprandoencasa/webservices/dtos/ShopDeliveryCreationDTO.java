package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Turn;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class ShopDeliveryCreationDTO {
    @NotBlank
    public Long shopId;
    @NotBlank
    public List<Long> products;
    @Nullable
    public Location location;
    @Nullable
    public Turn turn;
}
