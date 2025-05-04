package nihvostain.managers;

import java.sql.*;

public class DataBasesManager {
    private final Connection connection;
    public DataBasesManager(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    public boolean CheckLogin(String login) throws SQLException {
        String sql = "select login from PasswordUsers where login = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public boolean CheckPassword(String login, String password) throws SQLException {
        String sql = "select password from PasswordUsers where login = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("password").equals(password);
        } else {
            return false;
        }
    }

    public void insertUser (String login, String password) throws SQLException {
        String sql = "insert into PasswordUsers (login, password) values (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, password);
        statement.executeUpdate();
    }


}
