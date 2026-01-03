package com.empowerme.osgi.user.api;

public interface UserService {
    boolean checkBalance(String userId, double amount);
    void deductBalance(String userId, double amount);
    // Keeping it simple for OSGi demo, usually would return objects
}
