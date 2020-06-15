 Endpoints
-

###GET /api/search

Used for searching products in the database

#####Query values:

- ``keyword``(Optional): String used to search. Default: Empty string.
- ``categories``(Optional): List of ProductType strings. Can be concatenated like ``categories=v1,v2,v3`` or ``categories=v1&categories=v2&categories=v3``. Default: All categories set.
- ``locationId``: String indicating the unique id of the location used as centerpiece for the search.
- ``page``(Optional): Number identifying the page number for the search. Keep in mind, ``0`` indicates page 1. Default: 0.
- ``size``(Optional): Number identifying the search size for each page. Default: 10
- ``order``(Optional): Identifies the order of the resulting list. Values can be "priceAsc", "priceDesc", "idAsc", (default) "idDesc".

#####Response:

- status: 200
- body:

```
[{
    
    id,
    name,
    image,
    price,
    types,
    shop = {
        id,
        name,
        categories,
        location = {
                id,
                address,
                latitude,
                longitude,
                latitudeRadians,
                longitudeRadians,
            },
        days,
        openingHour,
        closingHour,
        deliveryRadius,
        discounts = [{
                id,
                type,
                startingDate,
                endingDate,
                percentage,
                productType, //Null if type single or multiple
                products, //Null if type single or category
                product //Null if type category or multiple
            }]
    }
    
}]
```

####Errors

- 400 BAD_REQUEST: on category mismatch, or lack of locationId.
- 404 NOT_FOUND: Location id not found.

###POST /api/customer

Used for customer creation

####Query values:

- ``name`` : customer name to be identified with.
- ``email`` : customer email for validation forms.
- ``password`` : customer password for validation forms.

####Response:

- status: 201
- body:

```
{
    id,
    name,
}
```

####Errors

- 400 BAD_REQUEST: On lack of a parameter, on empty fields, or on bad email formatting.
- 403 FORBIDDEN: On email or name already exist.

###GET /api/customer

Used for customer validation

####Query values:

- ``email``: Needed for validation.
- ``password``: Needed for validation.

####Response:

- status: 200
- body:

```
{
    id,
    name
}
```

####Errors:

- 400 BAD_REQUEST: If at least one parameter is missing, empty, or email has wrong formatting.
- 403 FORBIDDEN: If email and/or password are incorrect.

###DELETE /api/customer

Used when trying to delete a customer from the database

####Query values:

- ``email``: Needed for validation.
- ``password``: Needed for validation.

####Response:

- status: 204

####Errors:

- 400 BAD_REQUEST: If at least one parameter is missing, empty, or email has wrong formatting.
- 403 FORBIDDEN: If email and/or password are incorrect.
- 404 NOT_FOUND: If the customer doesn't exist.

###GET /api/customer/locations

Used to return all known locations for the customer

####Query values:

- ``customerId``: customer id for manipulation
- ``password``: customer password for validation

####Response:

- status: 200
- body:

```
{
    customerId,
    locations = [{
            locationId,
            address,
            latitude,
            longitude,
            latitudeRadians,
            longitudeRadians
        }]
}
```

###POST /api/customer/location

Used for creating a new Location for a customer.

####Query values:

- ``customerId``: customer id for manipulation.
- ``address``: Location address as it appears on a map.
- ``latitude``: Latitude as it appears on a map.
- ``longitude``: Longitude as it appears on a map.

####Response:

- status: 201
- body:

```
{
    locationId,
    address,
    latitude,
    longitude,
    latitudeRadians,
    longitudeRadians,
}
```

####Errors:

- 400 BAD_REQUEST: Missing one parameter, or the field is empty.
- 404 NOT_FOUND: customer with id not found.

###DELETE /api/customer/location

Used for deleting a Location for a customer.

####Query values:

- ``customerId``: customer id for verification.
- ``locationId``: Location id for verification.

####Response:

- status: 200
- body:

```
{
    locationId,
    address,
    latitude,
    longitude,
    latitudeRadians,
    longitudeRadians,
}
```

####Errors:

- 400 BAD_REQUEST: Missing one parameter, or field is empty.
- 404 NOT_FOUND: customer or Location with id not found.

###POST /api/product

Used for loading a new single product into the database.

####Query values:

- ``managerId``: Manager id for verification.

####Request body:

```
{
    name,
    brand,
    image,
    price,
    types = [ProductType],
    shopId,
}
```

####Response:

- status: 201
- body:

```
{
    
    id,
    name,
    image,
    price,
    types,
    shop = {
        id,
        name,
        categories,
        location = {
                id,
                address,
                latitude,
                longitude,
                latitudeRadians,
                longitudeRadians,
            },
        days,
        openingHour,
        closingHour,
        deliveryRadius
    }
    
}
```

####Errors:

- 400 BAD_REQUEST: Missing one parameter, or field is empty.
- 404 NOT_FOUND: Manager or Shop with id not found.

###GET /api/product

Used for requesting a single product from the database.

####Query values:

- ``productId``: Product id for searching.

####Response:

- status: 200
- body:

```
{
    
    id,
    name,
    image,
    price,
    types,
    shop = {
        id,
        name,
        categories,
        location = {
                id,
                address,
                latitude,
                longitude,
                latitudeRadians,
                longitudeRadians,
            },
        days,
        openingHour,
        closingHour,
        deliveryRadius,
        discounts = [{
                id,
                type,
                startingDate,
                endingDate,
                percentage,
                productType, //Null if type single or multiple
                products, //Null if type single or category
                product //Null if type category or multiple
            }]
    }
    
}
```

####Errors:

- 400 BAD_REQUEST: Missing id parameter or field is empty.
- 404 NOT_FOUND: Product with id not found.

###DELETE /api/product

Used for deleting a single product from the database.

####Query values:

- ``managerId``: Manager id for validation.
- ``shopId``: Shop id for validation.
- ``productId``: Product id for searching.

####Response:

- status: 200
- body:

```
{
    
    id,
    name,
    image,
    price,
    types,
    shop = {
        id,
        name,
        categories,
        location = {
                id,
                address,
                latitude,
                longitude,
                latitudeRadians,
                longitudeRadians,
            },
        days,
        openingHour,
        closingHour,
        deliveryRadius,
        discounts = [{
                id,
                type,
                startingDate,
                endingDate,
                percentage,
                productType, //Null if type single or multiple
                products, //Null if type single or category
                product //Null if type category or multiple
            }]
    }
    
}
```

####Errors:

- 400 BAD_REQUEST: Missing id parameters or fields are empty.
- 403 FORBIDDEN: Shop and/or Manager ids not found.
- 404 NOT_FOUND: Product with id not found.

###GET /api/shop

Used for requesting a single shop from the database.

####Query values:

- ``shopId``: Shop id for searching.

####Response:

- status: 200
- body:

```
{
    id,
    name,
    categories,
    location = {
            id,
            address,
            latitude,
            longitude,
            latitudeRadians,
            longitudeRadians,
        },
    days,
    openingHour,
    closingHour,
    deliveryRadius,
    products = [{
            id,
            name,
            image,
            price,
            types,
        }],
    discounts = [{
            id,
            type,
            startingDate,
            endingDate,
            percentage,
            productType, //Null if type single or multiple
            products, //Null if type single or category
            product //Null if type category or multiple
        }]
}
```

####Errors:

- 400 BAD_REQUEST: Missing shopId parameter or field is empty.
- 404 NOT_FOUND: Shop with id not found.

###POST /api/shop

###DELETE /api/shop

###POST /api/manager

###PUT /api/manager

###POST /api/discount

###DELETE /api/discount