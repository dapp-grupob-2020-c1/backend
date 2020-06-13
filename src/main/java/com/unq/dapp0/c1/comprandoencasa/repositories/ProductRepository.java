package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Configuration
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query(value = "SELECT DISTINCT p FROM Product p" +
                " JOIN LEFT p.types t" +
                " JOIN LEFT p.shop s" +
                " WHERE p.name LIKE CONCAT('%', :keyword, '%')" +
                " AND t in :categories" +
                " AND SQRT(Power( :latitude - s.location.latitude , 2)" + //Optimize search with Pythagorean teorem
                " + Power( :longitude - s.location.longitude , 2)) < 30" +
                " AND (( 6371 * ACOS(" + //Nail down exact locations with circular math
                " SIN( s.location.latitudeRadians ) * SIN( RADIANS( :latitude ) )" +
                " + COS( s.location.latitudeRadians ) * COS( RADIANS( :latitude ) )" +
                " * COS( RADIANS( :longitude ) - s.location.longitudeRadians )" +
                " )) <= p.shop.deliveryRadius )",
            countQuery = "SELECT count(p) FROM Product p",
            nativeQuery = true)
    List<Product> searchBy(@Param("keyword") String keyword,
                           @Param("categories") Collection<String> categories,
                           @Param("latitude") Double latitude,
                           @Param("longitude") Double longitude,
                           Pageable pageable);


    Optional<Product> findById(Long id);

    List<Product> findAll();
}
