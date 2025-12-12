package org.example.smartguard.Services;

import org.example.smartguard.Model.User;
import org.example.smartguard.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        // Aici ar putea fi adaugata logica de validare sau hash-uirea parolei (daca nu e in AuthService)
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        // Aici ar putea fi adaugata logica de verificare a permisiunilor
        userRepository.deleteById(id);
    }
}