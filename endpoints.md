 Endpoints
-

###GET /api/search

#####Query values:

- ``keyword``(Optional): String used to search. Uses empty string if ommited.
- ``categories``: List of ProductType strings. Can be concatenated like ``categories=v1,v2,v3`` or ``categories=v1&categories=v2&categories=v3``.
- ``locationId``: String indicating the unique id of the location used as centerpiece for the search.
- ``page``: Number identifying the page number for the search. Keep in mind, ``0`` indicates page 1.
- ``size``: Number identifying the search size for each page.

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

