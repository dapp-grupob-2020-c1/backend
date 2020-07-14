package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingListEntry;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Configuration
@Repository
public interface ShoppingListEntryRepository extends CrudRepository<ShoppingListEntry, Long> {
}
