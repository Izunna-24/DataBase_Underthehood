package org.suzyBarbie.repositories;

import exceptions.UserUpdateFailedException;
import org.suzyBarbie.models.User;

import java.sql.*;
import java.util.Optional;

public class UserRepository {

    public static Connection connect() {
        String url = "jdbc:postgresql://localhost:5432/suzies_store";
        //mysql -> jdbc:mysql:localhost:3306, postgres -> jdbc:postgresql://localhost:5432
        String username = "postgres";
        String password = "lifeiseasy>>";
        //?createDatabaseIfNotExist=true
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public User saveUser(User user) {

        String sql = "insert into users (user_id,wallet_id) values (?,?)";
        try(Connection connection = connect()){

          var  preparedStatement = connection.prepareStatement(sql);
          Long id = generateId();
            preparedStatement.setLong(1, id);
            preparedStatement.setObject(2, user.getWalletId());
            preparedStatement.execute();

            return getUserById(id);
        }catch (SQLException e){
            System.err.println("Error" + e.getMessage());
            throw new RuntimeException("failed to connect to database");
        }
    }

    private Long generateId() {
        try(Connection connection = connect()){
            String sql = "SELECT max(user_id) FROM users";
            var statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long lastidGenerated = resultSet.getLong(1);
            return lastidGenerated + 1;
        }catch (SQLException exception){
            throw new RuntimeException(exception.getMessage());
        }
    }

    private static User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id=?";
        try(Connection connection = connect()){
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long userId = resultSet.getLong(1);
            Long walletId = resultSet.getLong(2);
            User user = new User();
            user.setUserId(userId);
            user.setWalletId(walletId);
            return user;
        }catch (SQLException exception){
            return null;
//            System.err.println("SQLException: " + exception.getMessage());
//            //throw new RuntimeException(exception);
        }

    }


    public User upadateUser(Long userId, Long walletId) {
        try (Connection connection = connect()) {
            String sql = "UPDATE users SET wallet_id= ? WHERE user_id =?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, walletId);
            statement.setLong(2, userId);
            statement.executeUpdate();
            return getUserById(userId);
        } catch (SQLException exception) {
            throw new UserUpdateFailedException(exception.getMessage());
        }
    }

    public void deleteUserById(Long id) {
        try(Connection connection = connect()){
            String sql = "DELETE FROM users WHERE user_id=?";
            var statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
        }catch (SQLException exception){
            throw new RuntimeException("failed to delete user");
        }


    }

    public Optional<User> findUserById(Long id) {
        User user = getUserById(id);
        if (user != null) return Optional.of(user);
        return Optional.empty();
    }
}
