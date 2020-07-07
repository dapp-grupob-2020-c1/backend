package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.User;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.repositories.UserRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.LocationRepository;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
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
}
