package com.smartgrocery.demo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // =========================
    // GET ALL
    // =========================

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // =========================
    // GET BY ID
    // =========================

    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id)
                );
    }

    // =========================
    // CREATE
    // =========================

    public User createUser(User user) {

        return userRepository.save(user);
    }

    // =========================
    // UPDATE
    // =========================

    public User updateUser(Long id, User request) {

        User user = getUserById(id);

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());

        return userRepository.save(user);
    }

    // =========================
    // DELETE
    // =========================

    public void deleteUser(Long id) {

        User user = getUserById(id);

        userRepository.delete(user);
    }
}
