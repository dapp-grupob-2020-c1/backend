package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.AuthProvider;
import com.unq.dapp0.c1.comprandoencasa.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void givenACorrectSetupthenAnEntityManagerWillBeAvailable() {
        assertNotNull(entityManager);
    }

    @Test
    public void repositoryCanSaveAndReturnAGivenUser(){
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        User user = new User(name, password, email, AuthProvider.google);
        userRepository.save(user);
        Optional<User> result = userRepository.findById(user.getId());
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
    }

    @Test
    public void repositoryCanReturnAListOfUsersThatMatchEitherNameOrEmail(){
        String name1 = "foo";
        String name2 = "faa";
        String email1 = "foo@foo.com";
        String email2 = "faa@foo.com";
        String password = "1234";

        User user1 = new User(name1, password, email1, AuthProvider.google);
        User user2 = new User(name1, password, email2, AuthProvider.google);
        User user3 = new User(name2, password, email1, AuthProvider.google);
        User user4 = new User(name2, password, email2, AuthProvider.google);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        List<User> resultList = userRepository.findByNameOrEmail(name1, email1);

        assertEquals(3, resultList.size());
        assertTrue(resultList.stream().anyMatch(user -> user.getId().equals(user1.getId())));
        assertTrue(resultList.stream().anyMatch(user -> user.getId().equals(user2.getId())));
        assertTrue(resultList.stream().anyMatch(user -> user.getId().equals(user3.getId())));
    }

    @Test
    public void repositoryCanFindAUserByItsEmail(){
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        User user = new User(name, password, email, AuthProvider.google);
        userRepository.save(user);
        Optional<User> result = userRepository.findByEmail(email);
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
    }
}
