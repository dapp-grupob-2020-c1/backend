package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;
import com.unq.dapp0.c1.comprandoencasa.services.ProductService;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
    public void endpointGETSearchReturnsAListOfProductsWithAGivenKeywordAndOrCategory() throws Exception{
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

        Product product1 = mock(Product.class);
        when(product1.getId()).thenReturn(1L);
        when(product1.getName()).thenReturn("foo");
        when(product1.getImage()).thenReturn("any");
        when(product1.getPrice()).thenReturn(BigDecimal.ONE);
        when(product1.getTypes()).thenReturn(categories);
        when(product1.getShop()).thenReturn(shop);

        Product product2 = mock(Product.class);
        when(product2.getId()).thenReturn(3L);
        when(product2.getName()).thenReturn("fee");
        when(product2.getImage()).thenReturn("anyone");
        when(product2.getPrice()).thenReturn(BigDecimal.TEN);
        when(product2.getTypes()).thenReturn(categories);
        when(product2.getShop()).thenReturn(shop);

        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(product1);
        expectedProducts.add(product2);

        Long locationId = 10L;
        Integer page = 1;
        Integer size = 2;

        when(service.searchBy(keyword, categories, locationId, page, size)).thenReturn(expectedProducts);

        List<ProductDTO> expected = ProductController.parseProducts(expectedProducts);

        MvcResult mvcResult = this.mockMvc.perform(
                get("/api/search")
                        .param("keyword", keyword)
                        .param("categories", categories.get(0).toString())
                        .param("locationId", String.valueOf(locationId))

                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        List<ProductDTO> productlist = Arrays.stream(super.mapFromJson(content, ProductDTO[].class)).collect(Collectors.toList());
        assertEquals(expected.size(), productlist.size());

        boolean result = false;
        for (ProductDTO prod : productlist){
            result = expected.stream().anyMatch(productDTO -> productDTO.id.equals(prod.id));
        }
        assertTrue(result);
    }

    @Test
    public void endpointGETSearchReturnsBadRequestIfTheCategoryIsMissingOrIfItContainsInvalidCategories() throws Exception {
        MvcResult noCatResult = this.mockMvc.perform(
                get("/api/search").param("keyword", "foo"))
                .andReturn();

        int noCatStatus = noCatResult.getResponse().getStatus();
        assertEquals(400, noCatStatus);

        String noCatError = noCatResult.getResponse().getErrorMessage();
        assertEquals("Required List parameter 'categories' is not present", noCatError);

        MvcResult invalidCatResult = this.mockMvc.perform(
                get("/api/search").param("categories", "foo"))
                .andReturn();

        int invCatStatus = invalidCatResult.getResponse().getStatus();
        assertEquals(400, invCatStatus);

        String invCatError = invalidCatResult.getResponse().getErrorMessage();
        assertEquals("Product type category not found", invCatError);

        MvcResult noKeyResult = this.mockMvc.perform(
                get("/api/search").param("categories", ProductType.Bazaar.toString()))
                .andReturn();

        int noKeyStatus = noKeyResult.getResponse().getStatus();
        assertEquals(200, noKeyStatus);
    }
}
