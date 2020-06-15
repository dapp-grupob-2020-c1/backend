package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class LocationRepositoryTests {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void givenACorrectSetupthenAnEntityManagerWillBeAvailable() {
        assertNotNull(entityManager);
    }

    @Test
    public void repositoryCanSaveAndRetrieveALocation(){
        Location location = new Location("foo", 1.0, 1.0);
        locationRepository.save(location);
        Optional<Location> retrievedLocation = locationRepository.findById(location.getId());
        assertTrue(retrievedLocation.isPresent());
        assertEquals(location.getId(), retrievedLocation.get().getId());
    }
}
