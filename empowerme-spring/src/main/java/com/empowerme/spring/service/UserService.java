package com.empowerme.spring.service;

import com.empowerme.spring.domain.UserEntity;
import com.empowerme.spring.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity registerUser(UserEntity user) {
        return userRepository.save(user);
    }

    public Optional<UserEntity> getUser(Long id) {
        return userRepository.findById(id);
    }

    public boolean checkBalance(Long userId, Double amount) {
        Optional<UserEntity> user = userRepository.findById(userId);
        return user.map(u -> u.getWalletBalance() >= amount).orElse(false);
    }

    @Transactional
    public void deductBalance(Long userId, Double amount) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getWalletBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }
        
        user.setWalletBalance(user.getWalletBalance() - amount);
        userRepository.save(user);
    }

    public void processMonthlyInvoice() {
        System.out.println("Processing monthly invoice for all users...");
        // Mock logic
        userRepository.findAll().forEach(user -> {
            System.out.println("Calculating invoice for user: " + user.getName());
        });
    }
}
