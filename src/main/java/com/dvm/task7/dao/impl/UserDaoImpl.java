package com.dvm.task7.dao.impl;

import com.dvm.task7.dao.UserDao;
import com.dvm.task7.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String INSERT_USER= "INSERT INTO user(name, email, address, age, money) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_USER= "UPDATE user SET name = ?, email = ?, address = ?, age = ?,money=? WHERE id = ?";
    private static final String ADD_MONEY_BY_ID = "UPDATE user SET money = money + ? WHERE id = ?";
    private static final String DELETE_USER_BY_ID ="DELETE FROM user WHERE id=?";
    private static final String SORT_USER_BY_NAME= "SELECT * FROM user ORDER BY name ASC";
    private static final String SELECT_USER_BY_ID= "SELECT * FROM user WHERE id=?";
    private static final String SELECT_USER_BY_NAME="SELECT * FROM user WHERE name=?";
    private static final String SELECT_USER_BY_ADDRESS="SELECT * FROM user WHERE address=?";


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setAddress(rs.getString("address"));
            user.setAge(rs.getInt("age"));
            user.setMoney(rs.getLong("money"));
            return user;
        }
    };

    @Override
    public String insertUser(User user) {
        try {
            int result = jdbcTemplate.update(INSERT_USER, user.getName(), user.getEmail(), user.getAddress(), user.getAge(),user.getMoney());
            if(result > 0) {
                LOGGER.info("User inserted successfully");
                return "User inserted successfully";
            } else {
                LOGGER.error("Error in inserting user");
                return "Error in inserting user";
            }
        } catch (Exception e) {
            LOGGER.error("Error in inserting user", e);
            return "Error in inserting user";
        }
    }

    @Override
    public Boolean updateUser(User user, int id) {
        try {
            int result = jdbcTemplate.update(UPDATE_USER, user.getName(), user.getEmail(), user.getAddress(), user.getAge(),user.getMoney(), id);
            if(result > 0) {
                LOGGER.info("User updated successfully");
                return true;
            } else {
                LOGGER.error("Error in updating user");
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("Error in updating user", e);
            return false;
        }
    }

    @Override
    public Boolean deleteUserById(int id) {
        try {
            int result = jdbcTemplate.update(DELETE_USER_BY_ID, id);
            if(result > 0) {
                LOGGER.info("User deleted successfully");
                return true;
            } else {
                LOGGER.error("Error in deleting user");
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("Error in deleting user", e);
            return false;
        }
    }

    @Override
    public User findUserById(int id) {
        try {
            User user = jdbcTemplate.queryForObject(SELECT_USER_BY_ID, new Object[]{id}, userRowMapper);
            if(user != null) {
                LOGGER.info("User found successfully");
                return user;
            } else {
                LOGGER.error("User not found");
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Error in finding user by id", e);
            return null;
        }
    }

    @Override
    public User findUserByName(String name) {
        try {
            User user = jdbcTemplate.queryForObject(SELECT_USER_BY_NAME, new Object[]{name}, userRowMapper);
            if(user != null) {
                LOGGER.info("User found successfully");
                return user;
            } else {
                LOGGER.error("User not found");
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Error in finding user by name", e);
            return null;
        }
    }

    @Override
    public List<User> findUserByAddress(String address) {
        try {
            List<User> users = jdbcTemplate.query(SELECT_USER_BY_ADDRESS, new Object[]{address}, userRowMapper);
            if (users.size() > 0) {
                LOGGER.info("User found successfully");
                return users;
            } else {
                LOGGER.error("User not found");
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Error in finding user by address", e);
            return null;
        }
    }

    @Override
    public List<User> sortUsersByName() {
        try {
            List<User> users = jdbcTemplate.query(SORT_USER_BY_NAME, userRowMapper);
            return users;
        } catch (Exception e) {
            LOGGER.error("Error in sorting users by name", e);
            return null;
        }
    }

    @Override
    public String validateUser(User user) {
        if(user.getAge() > 100 || user.getAge() < 0){
            return "Age should be between 0 and 100";
        }
        if(user.getName().isEmpty()){
            return "Name should not be empty";
        }
        if(user.getAddress().isEmpty()){
            return "Address should not be empty";
        }
        return "true";
    }

    @Override
    public Boolean updateMoney(int id, long money) {
        try {
            int result = jdbcTemplate.update(ADD_MONEY_BY_ID, money, id);
            if(result > 0) {
                LOGGER.info("Money updated successfully");
                return true;
            } else {
                LOGGER.error("Error in updating money");
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("Error in updating money", e);
            return false;
        }
    }

//    @Override
//    public Boolean generateRandomUsers(int n) {
//        try {
//            int batchSize = 500000;
//            int numberOfThreads = 10;
//            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//            List<User> users = new ArrayList<>(batchSize);
//            for (int i = 0; i < n; i++) {
//                User user = new User();
//                user.setName("User" + i);
//                user.setEmail("user" + i + "@example.com");
//                users.add(user);
//                if (users.size() == batchSize) {
//                    List<User> batch = new ArrayList<>(users);
//                    executorService.submit(() -> saveBatch(batch));
//                    users.clear();
//                }
//            }
//            if (!users.isEmpty()) {
//                saveBatch(users);
//            }
//            executorService.shutdown();
//            while (!executorService.isTerminated()) {
//            }
//            return true;
//        } catch (Exception e) {
//            LOGGER.error("Error in generating random users", e);
//            return false;
//        }
//    }

//    private void saveBatch(List<User> users) {
//        String sql = INSERT_USER;
//        List<Object[]> batchArgs = new ArrayList<>();
//        for (User user : users) {
//            batchArgs.add(new Object[] { user.getName(), user.getEmail(), user.getPhone(), user.getAddress(), user.getAge() });
//        }
//        try {
//            jdbcTemplate.batchUpdate(sql, batchArgs);
//        } catch (Exception e) {
//            LOGGER.error("Error in saving batch", e);
//        }
//    }
}
