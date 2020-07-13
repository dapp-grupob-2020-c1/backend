package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ProductBatchDTO {
    @NotNull
    public Long shopId;
    @NotNull
    public List<ProductSmallDTO> productList;

}
