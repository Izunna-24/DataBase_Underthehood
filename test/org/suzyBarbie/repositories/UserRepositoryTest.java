package org.suzyBarbie.repositories;


import org.junit.jupiter.api.Test;
import org.suzyBarbie.models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
private final UserRepository userRepository = new UserRepository();
    @Test
    public void testDataBaseConnection() {
        try (Connection connection = UserRepository.connect()) {
            assertNotNull(connection);
            System.out.println(("connection -> " + connection));
        } catch (SQLException err) {
            assertNotNull(err);
            err.printStackTrace();
        }

    }

    @Test
    void saveUserTest() {
        User user = new User();
//        user.setWalletId(1L);
        User savedUser = userRepository.saveUser(user);
        assertNotNull(savedUser);
    }

    @Test
    public void testUpdateUser(){
        Long userId = 4L;
        Long walletId = 200L;

        User user = userRepository.upadateUser(userId, walletId);
        assertNotNull(user);
        assertEquals(200L,user.getWalletId());
    }

    @Test
    public void testFindUserById(){
       User user =  userRepository.findUserById(2L).orElseThrow();
       assertNotNull(user);
       assertEquals(2L, user.getUserId());

    }

    @Test
    public void testDeleteUserById(){
        userRepository.deleteUserById(10L);
        Optional<User> user = userRepository.findUserById(10L);
        assertTrue(user.isEmpty());
    }
}