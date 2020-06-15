package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.Discount;
import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.services.ShopService;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ShopController.class)
public class ShopControllerTests extends AbstractRestTest{

    @Autowired
    private ShopController controller;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopService service;

    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void endpointGETShopReturnsDataOfASingleShop() throws Exception {
        List<Product> products = new ArrayList<>();
        List<Discount> discounts = new ArrayList<>();

        Shop shopMock = mock(Shop.class);
        when(shopMock.getId()).thenReturn(2L);
        when(shopMock.getName()).thenReturn("faa");
        when(shopMock.getShopCategories()).thenReturn(new ArrayList<>());
        when(shopMock.getLocation()).thenReturn(new Location("test", 1.0, 1.0));
        when(shopMock.getDays()).thenReturn(new ArrayList<>());
        when(shopMock.getOpeningHour()).thenReturn(LocalTime.of(1, 0));
        when(shopMock.getClosingHour()).thenReturn(LocalTime.of(2, 0));
        when(shopMock.getDeliveryRadius()).thenReturn(1);
        when(shopMock.getProducts()).thenReturn(products);
        when(shopMock.getActiveDiscounts()).thenReturn(discounts);

        when(service.findShopById(shopMock.getId())).thenReturn(shopMock);

        ShopDTO expectedProduct = new ShopDTO(shopMock);

        MvcResult mvcResult = this.mockMvc.perform(
                get("/api/shop")
                        .param("shopId", String.valueOf(shopMock.getId()))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        ShopDTO productResult = super.mapFromJson(content, ShopDTO.class);
        assertEquals(expectedProduct.id, productResult.id);
    }

    @Test
    public void endpointGETShopReturnsBadRequestIfMissingShopId() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                get("/api/shop")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);

        String errorMessage = mvcResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'shopId' is not present", errorMessage);
    }

    @Test
    public void endpointGETProductReturnsNotFoundIfProductDoesntExist() throws Exception {
        Long id = 0L;

        when(service.findShopById(id)).thenThrow(new ShopDoesntExistException(id));

        MvcResult mvcResult = this.mockMvc.perform(
                get("/api/shop")
                        .param("shopId", String.valueOf(id))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);

        String errorMessage = mvcResult.getResponse().getErrorMessage();
        assertEquals("Shop with id " + id + " does not exist", errorMessage);
    }
}