package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingList;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Configuration
@Repository
public interface ShoppingListRepository extends CrudRepository<ShoppingList, Long> {
}
