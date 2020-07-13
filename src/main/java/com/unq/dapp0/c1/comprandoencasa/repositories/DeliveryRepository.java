package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.objects.ShopDelivery;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Repository
public interface DeliveryRepository extends CrudRepository<ShopDelivery, Long> {

    @Query(value = "SELECT DISTINCT d FROM User u" +
            " JOIN TREAT(u.activeDeliveries as DeliveryAtShop) d" +
            " WHERE d.turn.time BETWEEN :startDate AND :endDate")
    List<ShopDelivery> getAllActiveScheduledDeliveriesBetween(@Param("startDate") LocalDateTime startDate,
                                                              @Param("endDate") LocalDateTime endDate);
}
