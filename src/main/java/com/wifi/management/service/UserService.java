package com.wifi.management.service;

import com.wifi.management.database_operation.UserDAO;
import com.wifi.management.model.User;
import com.wifi.management.utils.BD_Number_check;
import com.wifi.management.utils.Strong_password_check;
import com.wifi.management.utils.hash_password;
import java.util.List;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public boolean isVerifiedCustomer(int userId) {
        return userDAO.isExistingCustomer(userId);
    }

    // Handles secure login logic
    public User authenticateUser(String username, String plainPassword) {
        // Hash the input password to match database record
        String hashedInput = hash_password.hashPassword(plainPassword);
        return userDAO.login(username, hashedInput);
    }

    // Handles user registration with multiple validations
    public String registerNewCustomer(User user, String plainPassword) {

        // 1. Validate phone number format
        if (!BD_Number_check.isValidBDNumber(user.getPhone())) {
            return "Invalid Bangladeshi phone number!";
        }

        // 2. Check password strength
        List<String> passwordErrors = Strong_password_check.checkPasswordStrength(plainPassword);
        if (!passwordErrors.isEmpty()) {
            return "Weak Password: " + passwordErrors.get(0);
        }

        // 3. Hash the password before saving
        String hashedPassword = hash_password.hashPassword(plainPassword);
        user.setPasswordHash(hashedPassword);

        // 4. Call DAO to save in database
        boolean success = userDAO.registerUser(user);
        return success ? "Registration Successful" : "Username already exists or database error";
    }

    // Fetches user for admin search using ID
    public User findUserForAdmin(int userId) {
        return userDAO.searchUserById(userId);
    }

    // Retrieves full details for user profile dashboard (View থেকে ডাটা আনে)
    public User getUserProfile(int userId) {
        return userDAO.getUserFullProfile(userId);
    }
}