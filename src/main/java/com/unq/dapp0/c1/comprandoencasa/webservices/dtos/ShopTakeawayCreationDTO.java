package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;

public class ShopTakeawayCreationDTO {
    @NotBlank
    public Long shopId;
    @NotBlank
    public Collection<Long> shoppingEntryIds;
    @NotBlank
    public LocalDateTime turn;
}
