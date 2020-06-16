package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;
import com.unq.dapp0.c1.comprandoencasa.model.Discount;
import com.unq.dapp0.c1.comprandoencasa.model.DiscountBySingle;
import com.unq.dapp0.c1.comprandoencasa.model.DiscountByCategory;
import com.unq.dapp0.c1.comprandoencasa.model.DiscountByMultiple;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
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
    public ArrayList<Product> products;
    @Nullable
    public Product product;

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
            this.products = ((DiscountByMultiple) discount).getProducts();
        } else if (discount instanceof DiscountBySingle){
            this.type = "single";
            this.product = ((DiscountBySingle) discount).getProduct();
        }
    }

    public static List<DiscountDTO> createDiscounts(ArrayList<Discount> discounts) {
        List<DiscountDTO> discountDTOList = new ArrayList<>();
        for (Discount discount : discounts){
            discountDTOList.add(new DiscountDTO(discount));
        }
        return discountDTOList;
    }
}
