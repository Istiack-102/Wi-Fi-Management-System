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

    // ================= AUTHENTICATION =================
    public User authenticateUser(String username, String plainPassword) {
        String hashedInput = hash_password.hashPassword(plainPassword);
        return userDAO.login(username, hashedInput);
    }

    // ================= REGISTER USER =================
    public String registerNewCustomer(User user, String plainPassword) {

        if (!BD_Number_check.isValidBDNumber(user.getPhone())) {
            return "Invalid Bangladeshi phone number!";
        }

        List<String> passwordErrors = Strong_password_check.checkPasswordStrength(plainPassword);
        if (!passwordErrors.isEmpty()) {
            return "Weak Password: " + passwordErrors.get(0);
        }

        String hashedPassword = hash_password.hashPassword(plainPassword);
        user.setPasswordHash(hashedPassword);

        boolean success = userDAO.registerUser(user);
        return success ? "Registration Successful" : "Username already exists or DB error";
    }

    // ================= SINGLE USER SEARCH (ADMIN) =================
    public User findUserForAdmin(int userId) {
        return userDAO.searchUserById(userId);
    }

    // ================= FULL PROFILE =================
    public User getUserProfile(int userId) {
        return userDAO.getUserFullProfile(userId);
    }

    // ================= CHECK CUSTOMER STATUS =================
    public boolean isVerifiedCustomer(int userId) {
        return userDAO.isExistingCustomer(userId);
    }

    // ================= 🔥 NEW: MULTI USER SEARCH =================
    public List<User> searchUsers(String keyword) {
        return userDAO.searchUsers(keyword);
    }
}