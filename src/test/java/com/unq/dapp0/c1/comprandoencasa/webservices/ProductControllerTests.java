package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.*;
import com.unq.dapp0.c1.comprandoencasa.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTests extends AbstractRestTest {

    @Autowired
    private ProductController controller;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void endpointSearchReturnsAListOfProductsWithAGivenKeywordAndOrCategory() throws Exception{
        String keyword = "foo";
        List<ProductType> categories = new ArrayList<>();
        categories.add(ProductType.Bazaar);

        Shop shop = mock(Shop.class);
        when(shop.getId()).thenReturn(2L);
        when(shop.getName()).thenReturn("faa");
        when(shop.getShopCategories()).thenReturn(new ArrayList<>());
        when(shop.getLocation()).thenReturn(new Location("test", 1.0, 1.0));
        when(shop.getDays()).thenReturn(new ArrayList<>());
        when(shop.getOpeningHour()).thenReturn(LocalTime.of(1, 0));
        when(shop.getClosingHour()).thenReturn(LocalTime.of(2, 0));
        when(shop.getDeliveryRadius()).thenReturn(1);

        Product product = mock(Product.class);
        when(product.getId()).thenReturn(1L);
        when(product.getName()).thenReturn("foo");
        when(product.getImage()).thenReturn("any");
        when(product.getPrice()).thenReturn(BigDecimal.ONE);
        when(product.getTypes()).thenReturn(categories);
        when(product.getShop()).thenReturn(shop);

        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(product);

        when(service.searchBy(keyword, categories)).thenReturn(expectedProducts);

        String expected= mapToJson(ProductController.parseProducts(expectedProducts));

        System.out.print(expected);

        this.mockMvc.perform(
                get("/api/search")
                        .param("keyword", keyword)
                        .param("categories", categories.get(0).toString())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }
}
