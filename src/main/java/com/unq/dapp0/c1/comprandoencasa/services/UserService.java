package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.AuthProvider;
import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.User;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.repositories.UserRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.LocationRepository;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.LocationDoesNotExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.services.security.TokenProvider;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ShopDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(String name, String email, String password) {
        checkIfExists(name, email);
        User user = new User(name, password, email);
        userRepository.save(user);
        return user;
    }

    private void checkIfExists(String name, String email) {
        List<User> userList = userRepository.findByNameOrEmail(name, email);
        if (!userList.isEmpty()){
            User user = userList.get(0);
            if (user.getName().equals(name)){
                throw new FieldAlreadyExistsException("name");
            } else {
                throw new FieldAlreadyExistsException("email");
            }
        }
    }

    @Transactional
    public User findUserById(Long id) {
        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()){
            return result.get();
        } else {
            throw new UserDoesntExistException(id);
        }
    }

    @Transactional
    public User validateUser(String email, String password) throws Exception {
        checkParameter(email, "email");
        checkParameter(password, "password");
        checkEmailFormat(email);
        Optional<User> result = userRepository.findByEmail(email);
        if (result.isPresent()){
            User user = result.get();
            user.validate(password, email);
            return user;
        } else {
            throw new InvalidUserException();
        }
    }

    private void checkParameter(String field, String fieldName) {
        if (field.isEmpty()){
            throw new EmptyFieldException(fieldName);
        }
    }

    private void checkEmailFormat(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!email.matches(regex)) {
            throw new InvalidEmailFormatException();
        }
    }

    @Transactional
    public List<Location> getLocationsOf(Long userId) {
        User user = findUserById(userId);
        return user.getLocations();
    }

    @Transactional
    public Location addLocationTo(Long customerId, String address, Double latitude, Double longitude) {
        checkParameter(address, "address");
        User user = findUserById(customerId);
        Location location = new Location(address, latitude, longitude);
        user.addLocation(location);
        locationRepository.save(location);
        userRepository.save(user);
        return location;
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public String authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.createToken(authentication);
    }

    @Transactional
    public User registerUser(String email, String name, String password) {
        if(existsByEmail(email)) {
            throw new FieldAlreadyExistsException("Email");
        }

        // Creating user's account
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return save(user);
    }

    @Transactional
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public Location removeLocationOf(Long userId, Long locationId) {
        User user = findUserById(userId);
        Optional<Location> location = user.getLocations().stream().filter(loc -> loc.getId().equals(locationId)).findFirst();
        if (location.isPresent()){
            user.removeLocation(location.get());
            return location.get();
        } else {
            throw new LocationDoesNotExistException(locationId);
        }
    }

    @Transactional
    public List<Shop> getShopsFrom(Long userId) {
        User user = findUserById(userId);
        return user.getShops();
    }

    @Transactional
    public Shop getShopFrom(Long userId, long shopId) {
        List<Shop> shops = getShopsFrom(userId);
        Optional<Shop> result = shops.stream().filter(shop -> shop.getId().equals(shopId)).findFirst();
        if (result.isPresent()){
            return result.get();
        } else {
            throw new ShopDoesntExistException(shopId);
        }
    }
}