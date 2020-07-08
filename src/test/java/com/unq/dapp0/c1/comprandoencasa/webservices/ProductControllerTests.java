package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.LocationDoesNotExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.ProductService;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests extends AbstractRestTest {

    @Autowired
    private ProductController controller;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @WithMockUser("spring")
    @Test
    public void endpointGETProductReturnsDataOfASingleProduct() throws Exception {
        List<ProductType> categories = new ArrayList<>();
        categories.add(ProductType.Bazaar);

        Shop shopMock = mock(Shop.class);
        when(shopMock.getId()).thenReturn(2L);
        when(shopMock.getName()).thenReturn("faa");
        when(shopMock.getShopCategories()).thenReturn(new ArrayList<>());
        when(shopMock.getLocation()).thenReturn(new Location("test", 1.0, 1.0));
        when(shopMock.getDays()).thenReturn(new ArrayList<>());
        when(shopMock.getOpeningHour()).thenReturn(LocalTime.of(1, 0));
        when(shopMock.getClosingHour()).thenReturn(LocalTime.of(2, 0));
        when(shopMock.getDeliveryRadius()).thenReturn(1);

        Product productMock = mock(Product.class);
        when(productMock.getId()).thenReturn(1L);
        when(productMock.getName()).thenReturn("foo");
        when(productMock.getImage()).thenReturn("any");
        when(productMock.getPrice()).thenReturn(BigDecimal.ONE);
        when(productMock.getTypes()).thenReturn(categories);
        when(productMock.getShop()).thenReturn(shopMock);

        when(service.findProductById(productMock.getId())).thenReturn(productMock);

        ProductDTO expectedProduct = new ProductDTO(productMock);

        MvcResult mvcResult = this.mockMvc.perform(
                get("/product")
                        .param("productId", String.valueOf(productMock.getId()))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        ProductDTO productResult = super.mapFromJson(content, ProductDTO.class);
        assertEquals(expectedProduct.id, productResult.id);
    }

    @WithMockUser("spring")
    @Test
    public void endpointGETProductReturnsBadRequestIfMissingProductId() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                get("/product")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);

        String errorMessage = mvcResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'productId' is not present", errorMessage);
    }

    @WithMockUser("spring")
    @Test
    public void endpointGETProductReturnsNotFoundIfProductDoesntExist() throws Exception {
        Long id = 0L;

        when(service.findProductById(id)).thenThrow(new ProductDoesntExistException(id));

        MvcResult mvcResult = this.mockMvc.perform(
                get("/product")
                        .param("productId", String.valueOf(id))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);

        String errorMessage = mvcResult.getResponse().getErrorMessage();
        assertEquals("Product with id " + id + " does not exist", errorMessage);
    }

    @WithMockUser("spring")
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
        Integer page = 0;
        Integer size = 5;
        String order = "priceAsc";

        when(service.searchBy(keyword, categories, locationId, page, size, order)).thenReturn(expectedProducts);

        List<ProductDTO> expected = ProductController.parseProducts(expectedProducts);

        MvcResult mvcResult = this.mockMvc.perform(
                get("/product/search")
                        .param("keyword", keyword)
                        .param("categories", categories.get(0).toString())
                        .param("locationId", String.valueOf(locationId))
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("order", order)
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

    @WithMockUser("spring")
    @Test
    public void endpointGETSearchReturnsBadRequestIfLocationIdIsMissingOrIfItContainsInvalidCategories() throws Exception {
        MvcResult noLocResult = this.mockMvc.perform(
                get("/product/search"))
                .andReturn();

        int noLocStatus = noLocResult.getResponse().getStatus();
        assertEquals(400, noLocStatus);

        String noLocError = noLocResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'locationId' is not present", noLocError);

        MvcResult invalidCatResult = this.mockMvc.perform(
                get("/product/search")
                        .param("categories", "foo")
                        .param("locationId", "1"))
                .andReturn();

        int invCatStatus = invalidCatResult.getResponse().getStatus();
        assertEquals(400, invCatStatus);

        String invCatError = invalidCatResult.getResponse().getErrorMessage();
        assertEquals("Product type category not found", invCatError);

        MvcResult correctCatResult = this.mockMvc.perform(
                get("/product/search")
                        .param("categories", ProductType.Bazaar.toString())
                        .param("locationId", "1"))
                .andReturn();

        int correctCatStatus = correctCatResult.getResponse().getStatus();
        assertEquals(200, correctCatStatus);

        MvcResult onlyLocResult = this.mockMvc.perform(
                get("/product/search")
                        .param("locationId", "1"))
                .andReturn();

        int onlyLocStatus = onlyLocResult.getResponse().getStatus();
        assertEquals(200, onlyLocStatus);
    }

    @WithMockUser("spring")
    @Test
    public void endpointGETSearchReturnsNotFoundIfLocationDoesNotExist() throws Exception {
        Long id = 0L;

        when(service.searchBy("",
                ProductController.parseToTypes(new ArrayList<>()),
                Long.valueOf(id),
                0,
                10,
                "idDesc")).thenThrow(new LocationDoesNotExistException(id));

        MvcResult mvcResult = this.mockMvc.perform(
                get("/product/search")
                        .param("locationId", String.valueOf(id))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);

        String errorMessage = mvcResult.getResponse().getErrorMessage();
        assertEquals("Location with id " + id + " does not exist", errorMessage);
    }
}
