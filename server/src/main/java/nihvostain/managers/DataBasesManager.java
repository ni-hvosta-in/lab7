package nihvostain.managers;

import common.model.StudyGroup;

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

    public void insertStudyGroup (String key, StudyGroup studyGroup, String login) throws SQLException {
        String sql = "insert into StudyGroups (key, name, x, y, creationDate, studentsCount," +
                " formOfEducation, semesterEnum, nameP, birthday, passportID, eyeColor, hairColor, login)" +
                "  values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, key);
        statement.setString(2, studyGroup.getName());
        statement.setLong(3, studyGroup.getCoordinates().getX());
        statement.setFloat(4, studyGroup.getCoordinates().getY());
        statement.setTimestamp(5, Timestamp.valueOf(studyGroup.getCreationDate()));
        statement.setLong(6, studyGroup.getStudentsCount());
        statement.setString(7, studyGroup.getFormOfEducation().toString());
        statement.setString(8, studyGroup.getSemesterEnum().toString());
        if (studyGroup.getGroupAdmin() != null) {
            statement.setString(9, studyGroup.getGroupAdmin().getName());
            if (studyGroup.getGroupAdmin().getBirthday() != null) {
                statement.setString(10, studyGroup.getGroupAdmin().getBirthday().toString());
            } else {
                statement.setString(10, null);
            }
            statement.setString(11, studyGroup.getGroupAdmin().getPassportID());
            if (studyGroup.getGroupAdmin().getEyeColor() != null) {
                statement.setString(12, studyGroup.getGroupAdmin().getEyeColor().toString());
            } else {
                statement.setString(12, null);
            }
            statement.setString(13, studyGroup.getGroupAdmin().getHairColor().toString());
        } else {
            statement.setString(9, null);
            statement.setString(10, null);
            statement.setString(11, null);
            statement.setString(12, null);
            statement.setString(13, null);
        }
        statement.setString(14, login);
        statement.executeUpdate();

    }

}
