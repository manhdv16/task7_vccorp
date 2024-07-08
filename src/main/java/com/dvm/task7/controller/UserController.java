package com.dvm.task7.controller;

import com.dvm.task7.dao.UserDao;
import com.dvm.task7.dao.impl.UserDaoImpl;
import com.dvm.task7.model.User;
import com.dvm.task7.response.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class UserController {
    private final UserDao userDao;

    @Autowired
    public UserController(UserDaoImpl userDAO) {
        this.userDao = userDAO;
    }
    private Logger LOGGER = LogManager.getLogger(UserController.class);

    @GetMapping("users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        User u = userDao.findUserById(id);
        if(u == null) {
            LOGGER.error("User not found");
            ApiResponse<String> response = new ApiResponse<>(0, 404, "User not found", null);
            return ResponseEntity.ok(response);
        }
        ApiResponse<User> response = new ApiResponse<>(1, 200, "User found", u);
        LOGGER.info("User found", u);
        return ResponseEntity.ok(response);
    }
    @GetMapping("users/name")
    public ResponseEntity<?> getUserByName(@RequestParam String name) {
        User u = userDao.findUserByName(name);
        if(u == null) {
            LOGGER.error("User not found");
            ApiResponse<String> response = new ApiResponse<>(0, 404, "User not found", null);
            return ResponseEntity.ok(response);
        }
        ApiResponse<User> response = new ApiResponse<>(1, 200, "User found", u);
        LOGGER.info("User found", u);
        return ResponseEntity.ok(response);
    }
    @GetMapping("users/address")
    public ResponseEntity<?> getUserByAddress(@RequestParam String address) {
        List<User> list = userDao.findUserByAddress(address);
        if(list.size() == 0) {
            LOGGER.error("User not found");
            ApiResponse<String> response = new ApiResponse<>(0, 404, "User not found", null);
            return ResponseEntity.ok(response);
        }
        ApiResponse<List<User>> response = new ApiResponse<>(1, 200, "User found", list);
        return ResponseEntity.ok(response);
    }
    @GetMapping("users/sort")
    public ResponseEntity<?> sortUsersByName() {
        List<User> users = userDao.sortUsersByName();
        if(users.size() == 0) {
            LOGGER.error("No users found");
            ApiResponse<String> response = new ApiResponse<>(0, 404, "User not found", null);
            return ResponseEntity.ok(response);
        }
        ApiResponse<List<User>> response = new ApiResponse<>(1, 200, "Users sorted by name", users);
        return ResponseEntity.ok(response);
    }
    @PostMapping("users")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        String mess = userDao.validateUser(user);
        if(!"true".equals(mess)){
            LOGGER.error(mess);
            ApiResponse<String> response = new ApiResponse<>(0, 400, mess, null);
            return ResponseEntity.ok(response);
        }
        String message = userDao.insertUser(user);
        ApiResponse<String> response = new ApiResponse<>(1, 200, message, null);
        return ResponseEntity.ok(response);
    }
    @PutMapping("users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User user) {
        Boolean isUpdateSuccess = userDao.updateUser(user, id);
        if (!isUpdateSuccess) {
            LOGGER.error("User not found");
            ApiResponse<String> response = new ApiResponse<>(0, 404, "User not found", null);
            return ResponseEntity.ok(response);
        }
        ApiResponse<String> response = new ApiResponse<>(1, 200, "User updated successfully", null);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        Boolean isDelSuccess = userDao.deleteUserById(id);
        if (!isDelSuccess) {
            LOGGER.error("User not found");
            ApiResponse<String> response = new ApiResponse<>(0, 404, "User not found", null);
            return ResponseEntity.ok(response);
        }
        ApiResponse<String> response = new ApiResponse<>(1, 200, "User deleted successfully", null);
        return ResponseEntity.ok(response);
    }
    @PatchMapping ("users/{id}/addMoney")
    public ResponseEntity<?> addMoney(@PathVariable int id, @RequestParam long money) {
        userDao.updateMoney(id,money);
        ApiResponse<String> response = new ApiResponse<>(1, 200, "Money added successfully", null);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("users/transfer")
    public ResponseEntity<?> transferMoney(@RequestParam(name = "fromId") Integer fromUserId,
                                           @RequestParam(name = "toId") Integer toUserId,
                                           @RequestParam long money) {
        User fromUser = userDao.findUserById(fromUserId);
        User toUser = userDao.findUserById(toUserId);
        if(fromUser == null || toUser == null) {
            LOGGER.error("User not found");
            ApiResponse<String> response = new ApiResponse<>(0, 404, "User not found", null);
            return ResponseEntity.ok(response);
        }else if(fromUser.getMoney() < money) {
            LOGGER.error("Not enough money");
            ApiResponse<String> response = new ApiResponse<>(0, 400, "Not enough money", null);
            return ResponseEntity.ok(response);
        }else{
            userDao.updateMoney(fromUserId, -money);
            userDao.updateMoney(toUserId, money);
            ApiResponse<String> response = new ApiResponse<>(1, 200, "Money transferred successfully", null);
            return ResponseEntity.ok(response);
        }

    }
}
