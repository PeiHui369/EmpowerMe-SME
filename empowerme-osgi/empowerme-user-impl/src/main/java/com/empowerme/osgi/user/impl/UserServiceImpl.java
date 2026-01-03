package com.empowerme.osgi.user.impl;

import com.empowerme.osgi.user.api.UserService;
import org.osgi.service.component.annotations.Component;

@Component(service = UserService.class)
public class UserServiceImpl implements UserService {

    @Override
    public boolean checkBalance(String userId, double amount) {
        // Mock logic for demo
        System.out.println("OSGi UserService: Checking balance for " + userId);
        return true; 
    }

    @Override
    public void deductBalance(String userId, double amount) {
        System.out.println("OSGi UserService: Deducting $" + amount + " from " + userId);
    }
}
