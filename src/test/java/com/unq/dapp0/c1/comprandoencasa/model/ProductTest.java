package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.unq.dapp0.c1.comprandoencasa.model.ProductBuilder.anyProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ProductTest {

    @Test
    public void aProductCanBeCreatedWithItsAtributes(){
        Shop shop = mock(Shop.class);
        Product aProduct = new Product("ProductName", "ProductBrand", "ProductImage", new BigDecimal(999), shop);
        assertEquals("ProductName", aProduct.getName());
        assertEquals("ProductBrand", aProduct.getBrand());
        assertEquals("ProductImage", aProduct.getImage());
        assertEquals(new BigDecimal(999), aProduct.getPrice());
        assertEquals(shop, aProduct.getShop());
    }

    @Test
    public void aProductCanChangeItsName(){
        Product aProduct = anyProduct().build();

        assertNotEquals("Nombre de Producto", aProduct.getName());
        aProduct.setName("Nombre de Producto");
        assertEquals("Nombre de Producto", aProduct.getName());
    }

    @Test
    public void aProductCanChangeItsBrand(){
        Product aProduct = anyProduct().build();

        assertNotEquals("Marca de Producto", aProduct.getBrand());
        aProduct.setBrand("Marca de Producto");
        assertEquals("Marca de Producto", aProduct.getBrand());
    }

    @Test
    public void aProductCanChangeItsImage(){
        Product aProduct = anyProduct().build();

        assertNotEquals("Imagen de Producto", aProduct.getImage());
        aProduct.setImage("Imagen de Producto");
        assertEquals("Imagen de Producto", aProduct.getImage());
    }

    @Test
    public void aProductCanChangeItsPrice(){
        Product aProduct = anyProduct().build();

        BigDecimal newPrice = new BigDecimal(500);

        assertNotEquals(newPrice, aProduct.getPrice());
        aProduct.setPrice(newPrice);
        assertEquals(newPrice, aProduct.getPrice());
    }

    @Test
    public void aProductCanBeOfAType(){
        Product aProduct = anyProduct().build();
        ProductType aProductType = ProductType.values()[0];

        aProduct.addType(aProductType);

        assertTrue(aProduct.isType(aProductType));
    }

    @Test
    public void aProductCanBeOfMultipleTypes(){
        Product aProduct = anyProduct().build();
        ProductType aProductType = ProductType.values()[0];
        ProductType anotherProductType = ProductType.values()[1];

        aProduct.addType(aProductType);
        aProduct.addType(anotherProductType);

        assertTrue(aProduct.isType(aProductType));
        assertTrue(aProduct.isType(anotherProductType));
    }

    @Test
    public void aProductCanRemoveAType(){
        Product aProduct = anyProduct().build();
        ProductType aProductType = ProductType.values()[0];

        aProduct.addType(aProductType);
        assertTrue(aProduct.isType(aProductType));

        aProduct.removeType(aProductType);
        assertFalse(aProduct.isType(aProductType));
    }

}

class ProductBuilder {
    private String name = "no name";
    private String brand = "no brand";
    private String image = "no image";
    private BigDecimal price = new BigDecimal(999);
    private Shop shop = mock(Shop.class);

    public static ProductBuilder anyProduct() {
        return new ProductBuilder();
    }

    public Product build() {
        return new Product(name, brand, image, price, shop);
    }
}

