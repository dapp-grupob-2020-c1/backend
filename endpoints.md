 Endpoints
-

###GET /api/search

#####Query values:

- ``keyword``(Optional): String used to search. Default: Empty string.
- ``categories``(Optional): List of ProductType strings. Can be concatenated like ``categories=v1,v2,v3`` or ``categories=v1&categories=v2&categories=v3``. Default: All categories set.
- ``locationId``: String indicating the unique id of the location used as centerpiece for the search.
- ``page``(Optional): Number identifying the page number for the search. Keep in mind, ``0`` indicates page 1. Default: 0.
- ``size``(Optional): Number identifying the search size for each page. Default: 10
- ``order``(Optional): Identifies the order of the resulting list. Values can be "priceAsc", "priceDesc", "idAsc", (default) "idDesc".

#####Returns:

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
        deliveryRadius
    }
    
}
```

###POST /api/user

###PUT /api/user

###DELETE /api/user

###POST /api/user/location

###GET /api/user/location

###DELETE /api/user/location

###POST /api/manager

###PUT /api/manager

###POST /api/shop

###GET /api/shop

###DELETE /api/shop

###POST /api/product

###GET /api/product

###DELETE /api/shop

###POST /api/discount

###DELETE /api/discount