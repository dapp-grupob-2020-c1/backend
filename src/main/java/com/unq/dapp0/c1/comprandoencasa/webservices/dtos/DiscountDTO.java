package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Product;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ProductType;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Discount;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DiscountBySingle;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DiscountByCategory;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DiscountByMultiple;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DiscountDTO {
    public Long id;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate startingDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    public LocalDate endingDate;
    public double percentage;
    public String type;
    @Nullable
    public ProductType productType;
    @Nullable
    public Collection<Long> productsIds;
    @Nullable
    public Long productId;

    public DiscountDTO(){}

    public DiscountDTO(Discount discount) {
        this.id = discount.getId();
        this.startingDate = discount.getStartingDate();
        this.endingDate = discount.getEndingDate();
        this.percentage = discount.getPercentage();
        this.determineType(discount);
    }

    private void determineType(Discount discount) {
        if (discount instanceof DiscountByCategory){
            this.type = "category";
            this.productType = ((DiscountByCategory) discount).getProductType();
        } else if (discount instanceof DiscountByMultiple){
            this.type = "multiple";
            Collection<Product> products = ((DiscountByMultiple) discount).getProducts();
            Collection<Long> ids = new ArrayList<>();
            for (Product product : products){
                ids.add(product.getId());
            }
            this.productsIds = ids;
        } else if (discount instanceof DiscountBySingle){
            this.type = "single";
            this.productId = ((DiscountBySingle) discount).getProduct().getId();
        }
    }

    public static List<DiscountDTO> createDiscounts(List<Discount> discounts) {
        List<DiscountDTO> discountDTOList = new ArrayList<>();
        for (Discount discount : discounts){
            discountDTOList.add(new DiscountDTO(discount));
        }
        return discountDTOList;
    }
}
