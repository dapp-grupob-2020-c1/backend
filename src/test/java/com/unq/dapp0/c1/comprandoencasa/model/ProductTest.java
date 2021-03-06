package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Product;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ProductType;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.unq.dapp0.c1.comprandoencasa.model.ProductBuilder.aProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ProductTest {

    @Test
    public void aProductCanBeCreatedWithItsAtributes(){
        Shop shop = mock(Shop.class);
        Product aProduct = new Product("ProductName", "ProductBrand", "ProductImage", new BigDecimal(999), shop, new ArrayList<>());
        assertEquals("ProductName", aProduct.getName());
        assertEquals("ProductBrand", aProduct.getBrand());
        assertEquals("ProductImage", aProduct.getImage());
        assertEquals(new BigDecimal(999), aProduct.getPrice());
        assertEquals(shop, aProduct.getShop());
        assertTrue(aProduct.getTypes().isEmpty());
    }

    @Test
    public void aProductCanChangeItsName(){
        Product aProduct = aProduct().build();

        assertNotEquals("Nombre de Producto", aProduct.getName());
        aProduct.setName("Nombre de Producto");
        assertEquals("Nombre de Producto", aProduct.getName());
    }

    @Test
    public void aProductCanChangeItsBrand(){
        Product aProduct = aProduct().build();

        assertNotEquals("Marca de Producto", aProduct.getBrand());
        aProduct.setBrand("Marca de Producto");
        assertEquals("Marca de Producto", aProduct.getBrand());
    }

    @Test
    public void aProductCanChangeItsImage(){
        Product aProduct = aProduct().build();

        assertNotEquals("Imagen de Producto", aProduct.getImage());
        aProduct.setImage("Imagen de Producto");
        assertEquals("Imagen de Producto", aProduct.getImage());
    }

    @Test
    public void aProductCanChangeItsPrice(){
        Product aProduct = aProduct().build();

        BigDecimal newPrice = new BigDecimal(500);

        assertNotEquals(newPrice, aProduct.getPrice());
        aProduct.setPrice(newPrice);
        assertEquals(newPrice, aProduct.getPrice());
    }

    @Test
    public void aProductCanBeOfAType(){
        Product aProduct = aProduct().build();
        ProductType aProductType = ProductType.values()[0];

        aProduct.addType(aProductType);

        assertTrue(aProduct.isType(aProductType));
    }

    @Test
    public void aProductCanBeOfMultipleTypes(){
        Product aProduct = aProduct().build();
        ProductType aProductType = ProductType.values()[0];
        ProductType anotherProductType = ProductType.values()[1];

        aProduct.addType(aProductType);
        aProduct.addType(anotherProductType);

        assertTrue(aProduct.isType(aProductType));
        assertTrue(aProduct.isType(anotherProductType));
    }

    @Test
    public void aProductCanRemoveAType(){
        Product aProduct = aProduct().build();
        ProductType aProductType = ProductType.values()[0];

        aProduct.addType(aProductType);
        assertTrue(aProduct.isType(aProductType));

        aProduct.removeType(aProductType);
        assertFalse(aProduct.isType(aProductType));
    }

    @Test
    public void aProductCanReturnItsTypes(){
        List<ProductType> types = new ArrayList<>();
        types.add(ProductType.Bazaar);

        Product aProduct = aProduct().withTypes(types).build();

        assertEquals(types, aProduct.getTypes());
    }

}

