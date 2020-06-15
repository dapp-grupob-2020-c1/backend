package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidDiscountDatesException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.MultipleDiscountWithSingleItemException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiscountTests {
    @Test
    public void aDiscountHasAnID(){
        Discount discount = DiscountBuilder.anyDiscount().build();
        discount.setId(1L);

        assertEquals(1L, discount.getId());
    }

    @Test
    public void aDiscountCanHaveADiscountPercentage(){
        Discount discount = DiscountBuilder.anyDiscount().withPercentage(1.0).build();

        assertEquals(1.0, discount.getPercentage());
    }

    @Test
    public void aDiscountCanChangeItsDiscountPercentage(){
        Discount discount = DiscountBuilder.anyDiscount().build();

        discount.setPercentage(2.0);

        assertEquals(2.0, discount.getPercentage());
    }

    @Test
    public void aDiscountHasAStartingDate(){
        LocalDate startingDate = LocalDate.now();

        Discount discount = DiscountBuilder.anyDiscount().setStartingDate(startingDate).build();

        assertEquals(startingDate, discount.getStartingDate());
    }

    @Test
    public void aDiscountHasAnEndingDate(){
        LocalDate endingDate = LocalDate.now();

        Discount discount = DiscountBuilder.anyDiscount().setEndingDate(endingDate).build();

        assertEquals(endingDate, discount.getEndingDate());
    }

    @Test
    public void aDiscountCannotHaveAnEndingDateThatIsBeforeTheStartingDateAndViceversa(){
        LocalDate startingDate = LocalDate.of(2020, 5, 2);
        LocalDate endingDate = LocalDate.of(2020, 5, 1);

        Throwable exception = assertThrows(InvalidDiscountDatesException.class, ()->DiscountBuilder.anyDiscount()
                .setStartingDate(startingDate)
                .setEndingDate(endingDate)
                .build());

        assertEquals("The dates given for the discount are invalid. Starting date is "+startingDate.toString()+" and ending date is "+endingDate.toString(),
                exception.getMessage());
    }

    @Test
    public void aDiscountCanChangeItsStartingDate(){
        Discount discount = DiscountBuilder.anyDiscount().build();

        LocalDate newStartingDate = LocalDate.of(2020, 1, 1);

        discount.setStartingDate(newStartingDate);

        assertEquals(newStartingDate, discount.getStartingDate());
    }

    @Test
    public void aDiscountCannotChangeItsStartingDateToADateAfterItsEndingDate(){
        Discount discount = DiscountBuilder.anyDiscount().build();

        LocalDate newStartingDate = LocalDate.of(2021, 1, 1);

        Throwable exception = assertThrows(InvalidDiscountDatesException.class, ()->
                discount.setStartingDate(newStartingDate));

        assertEquals("The dates given for the discount are invalid. Starting date is "+newStartingDate.toString()+" and ending date is "+LocalDate.now().toString(),
                exception.getMessage());
    }

    @Test
    public void aDiscountCanChangeItsEndingDate(){
        Discount discount = DiscountBuilder.anyDiscount().build();

        LocalDate newEndingDate = LocalDate.of(2021, 1, 1);

        discount.setEndingDate(newEndingDate);

        assertEquals(newEndingDate, discount.getEndingDate());
    }

    @Test
    public void aDiscountCannotChangeItsEndingDateToADateBeforeTheStartingDate(){
        Discount discount = DiscountBuilder.anyDiscount().build();

        LocalDate newEndingDate = LocalDate.of(2020, 1, 1);

        Throwable exception = assertThrows(InvalidDiscountDatesException.class, ()->
                discount.setEndingDate(newEndingDate));

        assertEquals("The dates given for the discount are invalid. Starting date is "+LocalDate.now().toString()+" and ending date is "+newEndingDate.toString(),
                exception.getMessage());
    }

    @Test
    public void aDiscountKnowsToWhichShopItBelongs(){
        Shop shop = mock(Shop.class);

        Discount discount = DiscountBuilder.anyDiscount()
                .withShop(shop)
                .build();

        assertEquals(shop, discount.getShop());
    }

    @Test
    public void aDiscountKnowsIfItsActiveBasedOnItsDatesAndTheCurrentDate(){
        Discount activeDiscount = DiscountBuilder.anyDiscount()
                .setStartingDate(LocalDate.now())
                .setEndingDate(LocalDate.now())
                .build();

        assertTrue(activeDiscount.isActive());

        LocalDate startingDate = LocalDate.of(2021, 5, 1);
        LocalDate endingDate = LocalDate.of(2021, 5, 30);

        Discount inactiveDiscount = DiscountBuilder.anyDiscount()
                .setStartingDate(startingDate)
                .setEndingDate(endingDate)
                .build();

        assertFalse(inactiveDiscount.isActive());
    }

    @Test
    public void aDiscountCanBeForAProductType(){
        Product validProduct = mock(Product.class);
        when(validProduct.isType(ProductType.Bazaar)).thenReturn(true);

        Product invalidProduct = mock(Product.class);
        when(invalidProduct.isType(ProductType.Bazaar)).thenReturn(false);

        ArrayList<Product> products = new ArrayList<>();
        products.add(validProduct);

        Shop shop = mock(Shop.class);
        when(shop.getProducts()).thenReturn(products);

        DiscountByCategory discount = (DiscountByCategory) DiscountBuilder.anyDiscount()
                .withShop(shop)
                .withTarget(ProductType.Bazaar)
                .build();

        assertTrue(discount.isTypeCategory());
        assertFalse(discount.isTypeSingle());
        assertFalse(discount.isTypeMultiple());
        assertEquals(products, discount.getProducts());
        assertEquals(validProduct, discount.getProducts().get(0));
        assertEquals(ProductType.Bazaar, discount.getProductType());
    }

    @Test
    public void aDiscountForProductTypeCanChangeItsProductType(){
        DiscountByCategory discount = (DiscountByCategory) DiscountBuilder.anyDiscount().build();

        assertEquals(ProductType.Bazaar, discount.getProductType());

        discount.setProductType(ProductType.FoodsAndDrinks);

        assertEquals(ProductType.FoodsAndDrinks, discount.getProductType());
    }

    @Test
    public void aDiscountCanBeForASpecificProduct(){
        Product product = mock(Product.class);

        ArrayList<Product> products = new ArrayList<>();
        products.add(product);

        DiscountBySingle discount = (DiscountBySingle) DiscountBuilder.anyDiscount()
                .withProducts(products)
                .build();

        assertEquals(product, discount.getProduct());
    }

    @Test
    public void aDiscountForASpecificProductCanChangeTheTargetProduct(){
        Product product = mock(Product.class);

        ArrayList<Product> products = new ArrayList<>();
        products.add(product);

        DiscountBySingle discount = (DiscountBySingle) DiscountBuilder.anyDiscount()
                .withProducts(products)
                .build();

        assertEquals(product, discount.getProduct());

        Product newProduct = mock(Product.class);

        discount.setProduct(newProduct);

        assertEquals(newProduct, discount.getProduct());
    }

    @Test
    public void aDiscountCanBeForMultipleProducts(){
        Product product1 = mock(Product.class);
        when(product1.getId()).thenReturn(1L);

        Product product2 = mock(Product.class);
        when(product2.getId()).thenReturn(2L);

        ArrayList<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        DiscountByMultiple discount = (DiscountByMultiple) DiscountBuilder.anyDiscount()
                .withProducts(products)
                .build();

        assertEquals(products, discount.getProducts());
        assertTrue(discount.getProducts().contains(product1));
        assertTrue(discount.getProducts().contains(product2));
    }

    @Test
    public void aDiscountForMultipleProductsCanAddNewProductsToTheList(){
        Product product1 = mock(Product.class);
        when(product1.getId()).thenReturn(1L);

        Product product2 = mock(Product.class);
        when(product2.getId()).thenReturn(2L);

        ArrayList<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        DiscountByMultiple discount = (DiscountByMultiple) DiscountBuilder.anyDiscount()
                .withProducts(products)
                .build();

        assertEquals(products, discount.getProducts());
        assertTrue(discount.getProducts().contains(product1));
        assertTrue(discount.getProducts().contains(product2));

        Product product3 = mock(Product.class);
        when(product3.getId()).thenReturn(3L);

        discount.addProduct(product3);

        assertTrue(discount.getProducts().contains(product3));
    }

    @Test
    public void aDiscountForMultipleProductsCanAddAProductThatItAlreadyContains(){
        Product product = mock(Product.class);
        when(product.getId()).thenReturn(1L);

        ArrayList<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product);

        DiscountByMultiple discount = (DiscountByMultiple) DiscountBuilder.anyDiscount()
                .withProducts(products)
                .build();

        assertTrue(discount.getProducts().contains(product));
        assertEquals(2, discount.getProducts().size());

        discount.addProduct(product);

        assertEquals(3, discount.getProducts().size());
    }

    @Test
    public void aDiscountForMultipleProductsCanRemoveADiscountFromItsList(){
        Product product1 = mock(Product.class);
        when(product1.getId()).thenReturn(1L);

        Product product2 = mock(Product.class);
        when(product2.getId()).thenReturn(2L);

        Product product3 = mock(Product.class);
        when(product3.getId()).thenReturn(3L);

        ArrayList<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);

        DiscountByMultiple discount = (DiscountByMultiple) DiscountBuilder.anyDiscount()
                .withProducts(products)
                .build();

        assertEquals(products, discount.getProducts());
        assertTrue(discount.getProducts().contains(product1));
        assertTrue(discount.getProducts().contains(product2));
        assertTrue(discount.getProducts().contains(product3));

        discount.removeProduct(product3);

        assertFalse(discount.getProducts().contains(product3));
    }

    @Test
    public void aDiscountForMultipleProductsCannotRemoveAProductIfItCausesTheListToHaveASingleItem(){
        Product product1 = mock(Product.class);
        when(product1.getId()).thenReturn(1L);

        Product product2 = mock(Product.class);
        when(product2.getId()).thenReturn(2L);

        ArrayList<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        DiscountByMultiple discount = (DiscountByMultiple) DiscountBuilder.anyDiscount()
                .withProducts(products)
                .build();

        assertEquals(products, discount.getProducts());
        assertTrue(discount.getProducts().contains(product1));
        assertTrue(discount.getProducts().contains(product2));

        Throwable exception = assertThrows(MultipleDiscountWithSingleItemException.class,()->
                discount.removeProduct(product2));

        assertEquals("Removal of the product "+product2.getId()+" will cause the Discount to be composed of a single item. Use the class DiscountBySingle instead for the item that should remain",
                exception.getMessage());
    }

    @Test
    public void aDiscountForProductTypeKnowsHowToCompareItselfToOtherDiscounts(){
        Discount discount = DiscountBuilder.anyDiscount().build();

        Discount discountOfSameTypeAndPercentage = DiscountBuilder.anyDiscount().build();

        assertEquals(0, discount.compare(discountOfSameTypeAndPercentage));

        Discount discountOfSameTypeAndSuperiorPercentage = DiscountBuilder.anyDiscount()
                .withPercentage(2.0).build();

        assertEquals(-1, discount.compare(discountOfSameTypeAndSuperiorPercentage));

        Discount discountOfSameTypeAndInferiorPercentage = DiscountBuilder.anyDiscount()
                .withPercentage(0.5).build();

        assertEquals(1, discount.compare(discountOfSameTypeAndInferiorPercentage));

        ArrayList<Product> productsForSingle = new ArrayList<>();
        productsForSingle.add(mock(Product.class));
        Discount discountOfSingleType = DiscountBuilder.anyDiscount()
                .withProducts(productsForSingle).build();

        assertEquals(-1, discount.compare(discountOfSingleType));

        ArrayList<Product> productsForMultiple = new ArrayList<>();
        productsForMultiple.add(mock(Product.class));
        productsForMultiple.add(mock(Product.class));

        Discount discountOfMultipleType = DiscountBuilder.anyDiscount()
                .withProducts(productsForMultiple).build();

        assertEquals(-1, discount.compare(discountOfMultipleType));
    }

    @Test
    public void aDiscountForSingleProductKnowsHowToCompareItselfToOtherDiscounts(){
        ArrayList<Product> productsForSingle = new ArrayList<>();
        productsForSingle.add(mock(Product.class));

        Discount discount = DiscountBuilder.anyDiscount()
                .withProducts(productsForSingle).build();

        Discount discountOfSameTypeAndPercentage = DiscountBuilder.anyDiscount()
                .withProducts(productsForSingle).build();

        assertEquals(0, discount.compare(discountOfSameTypeAndPercentage));

        Discount discountOfSameTypeAndSuperiorPercentage = DiscountBuilder.anyDiscount()
                .withProducts(productsForSingle)
                .withPercentage(2.0).build();

        assertEquals(-1, discount.compare(discountOfSameTypeAndSuperiorPercentage));

        Discount discountOfSameTypeAndInferiorPercentage = DiscountBuilder.anyDiscount()
                .withProducts(productsForSingle)
                .withPercentage(0.5).build();

        assertEquals(1, discount.compare(discountOfSameTypeAndInferiorPercentage));

        Discount discountOfCategoryType = DiscountBuilder.anyDiscount().build();

        assertEquals(1, discount.compare(discountOfCategoryType));

        ArrayList<Product> productsForMultiple = new ArrayList<>();
        productsForMultiple.add(mock(Product.class));
        productsForMultiple.add(mock(Product.class));

        Discount discountOfMultipleType = DiscountBuilder.anyDiscount()
                .withProducts(productsForMultiple).build();

        assertEquals(-1, discount.compare(discountOfMultipleType));
    }

    @Test
    public void aDiscountForMultipleProductsKnowsHowToCompareItselfToOtherDiscounts(){
        ArrayList<Product> productsForMultiple = new ArrayList<>();
        productsForMultiple.add(mock(Product.class));
        productsForMultiple.add(mock(Product.class));

        Discount discount = DiscountBuilder.anyDiscount()
                .withProducts(productsForMultiple).build();

        Discount discountOfSameTypeAndPercentage = DiscountBuilder.anyDiscount()
                .withProducts(productsForMultiple).build();

        assertEquals(0, discount.compare(discountOfSameTypeAndPercentage));

        Discount discountOfSameTypeAndSuperiorPercentage = DiscountBuilder.anyDiscount()
                .withProducts(productsForMultiple)
                .withPercentage(2.0).build();

        assertEquals(-1, discount.compare(discountOfSameTypeAndSuperiorPercentage));

        Discount discountOfSameTypeAndInferiorPercentage = DiscountBuilder.anyDiscount()
                .withProducts(productsForMultiple)
                .withPercentage(0.5).build();

        assertEquals(1, discount.compare(discountOfSameTypeAndInferiorPercentage));

        Discount discountOfCategoryType = DiscountBuilder.anyDiscount().build();

        assertEquals(1, discount.compare(discountOfCategoryType));

        ArrayList<Product> productsForSingle = new ArrayList<>();
        productsForSingle.add(mock(Product.class));

        Discount discountOfSingleType = DiscountBuilder.anyDiscount()
                .withProducts(productsForSingle).build();

        assertEquals(1, discount.compare(discountOfSingleType));
    }

    @Test
    public void aDiscountForProductTypeCanCalculateTheTotalValueOfTheProductsItReceives(){
        Shop shop = mock(Shop.class);

        DiscountByCategory discount = (DiscountByCategory) DiscountBuilder.anyDiscount()
                .withTarget(ProductType.Bazaar)
                .withShop(shop)
                .build();

        List<ShoppingListEntry> entries = new ArrayList<>();
        Product validProduct = mock(Product.class);
        Product productOfAnotherShop = mock(Product.class);

        when(validProduct.getShop()).thenReturn(shop);
        when(productOfAnotherShop.getShop()).thenReturn(mock(Shop.class));
        when(validProduct.isType(ProductType.Bazaar)).thenReturn(true);
        when(validProduct.getPrice()).thenReturn(BigDecimal.valueOf(5));

        entries.add(new ShoppingListEntry(validProduct, 2));
        entries.add(new ShoppingListEntry(productOfAnotherShop, 1));

        assertEquals(2, entries.size());
        assertEquals(new BigDecimal("9.900"), discount.calculateFor(entries));
        assertEquals(1, entries.size());
    }


    @Test
    public void aDiscountForSingleProductCanCalculateTheTotalValueOfTheProductsItReceives(){
        Product validProduct = mock(Product.class);

        ArrayList<Product> discountProducts = new ArrayList<>();
        discountProducts.add(validProduct);

        DiscountBySingle discount = (DiscountBySingle) DiscountBuilder.anyDiscount()
                .withProducts(discountProducts)
                .build();

        List<ShoppingListEntry> entries = new ArrayList<>();
        Product productOfAnotherShop = mock(Product.class);

        when(validProduct.getId()).thenReturn(1L);
        when(productOfAnotherShop.getId()).thenReturn(2L);
        when(validProduct.getPrice()).thenReturn(BigDecimal.valueOf(5));

        entries.add(new ShoppingListEntry(validProduct, 2));
        entries.add(new ShoppingListEntry(productOfAnotherShop, 1));

        assertEquals(2, entries.size());
        assertEquals(new BigDecimal("9.900"), discount.calculateFor(entries));
        assertEquals(1, entries.size());
    }


    @Test
    public void aDiscountForMultipleProductsCanCalculateTheTotalValueOfTheProductsItReceives(){
        Product validProduct = mock(Product.class);
        Product anotherValidProduct = mock(Product.class);

        ArrayList<Product> discountProducts = new ArrayList<>();
        discountProducts.add(validProduct);
        discountProducts.add(anotherValidProduct);

        DiscountByMultiple discount = (DiscountByMultiple) DiscountBuilder.anyDiscount()
                .withProducts(discountProducts)
                .build();

        List<ShoppingListEntry> entries = new ArrayList<>();
        Product productOfAnotherShop = mock(Product.class);

        when(validProduct.getId()).thenReturn(1L);
        when(productOfAnotherShop.getId()).thenReturn(2L);
        when(anotherValidProduct.getId()).thenReturn(3L);
        when(validProduct.getPrice()).thenReturn(BigDecimal.valueOf(5));
        when(anotherValidProduct.getPrice()).thenReturn(BigDecimal.valueOf(2));

        entries.add(new ShoppingListEntry(validProduct, 2));
        entries.add(new ShoppingListEntry(anotherValidProduct, 1));
        entries.add(new ShoppingListEntry(productOfAnotherShop, 1));

        assertEquals(3, entries.size());
        assertEquals(new BigDecimal("11.880"), discount.calculateFor(entries));
        assertEquals(2, entries.size());
    }
}

class DiscountBuilder{
    private double percentage;
    private LocalDate startingDate;
    private LocalDate endingDate;
    private ProductType targType;
    private ArrayList<Product> targProd;
    private Shop shop;

    public static DiscountBuilder anyDiscount(){
        return new DiscountBuilder();
    }

    private DiscountBuilder(){
        this.percentage = 1.0;
        this.startingDate = LocalDate.now();
        this.endingDate = LocalDate.now();
        this.targType = ProductType.Bazaar;
        this.shop = mock(Shop.class);
    }

    public Discount build(){
        Discount discount = null;
        if (targType != null){
            discount = new DiscountByCategory(percentage, startingDate, endingDate, shop, targType);
        } else if (targProd.size() == 1){
            discount = new DiscountBySingle(percentage, startingDate, endingDate, shop, targProd.get(0));
        } else if (targProd.size() > 1){
            discount = new DiscountByMultiple(percentage, startingDate, endingDate, shop, targProd);
        }
        return discount;
    }

    public DiscountBuilder withPercentage(double percentage) {
        this.percentage = percentage;
        return this;
    }

    public DiscountBuilder setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
        return this;
    }

    public DiscountBuilder setEndingDate(LocalDate endingDate) {
        this.endingDate = endingDate;
        return this;
    }

    public DiscountBuilder withShop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public DiscountBuilder withTarget(ProductType productType) {
        this.targProd = null;
        this.targType = productType;
        return this;
    }

    public DiscountBuilder withProducts(ArrayList<Product> products) {
        this.targType = null;
        this.targProd = products;
        return this;
    }
}