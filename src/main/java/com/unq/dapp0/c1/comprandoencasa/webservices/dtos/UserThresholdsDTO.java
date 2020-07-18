package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.objects.ProductType;
import com.unq.dapp0.c1.comprandoencasa.model.objects.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UserThresholdsDTO {
    public Long userId;
    public BigDecimal totalThreshold;
    public Collection<TypeThresholdDTO> typeThresholds;

    public UserThresholdsDTO(){}

    public UserThresholdsDTO(User user) {
        this.userId = user.getId();
        this.totalThreshold = user.getTotalThreshold();
        this.typeThresholds = createThresholdsDTOs(user.getTypeThresholds(), user.getSuggestedTypeThresholds());
    }

    private List<TypeThresholdDTO> createThresholdsDTOs(Map<ProductType, BigDecimal> typeThresholds, Map<ProductType, BigDecimal> suggestedTypeThresholds) {
        List<TypeThresholdDTO> returnList = new ArrayList<>();
        for (ProductType type : typeThresholds.keySet()){
            returnList.add(new TypeThresholdDTO(type, typeThresholds.get(type), suggestedTypeThresholds.get(type)));
        }
        return returnList;
    }
}
