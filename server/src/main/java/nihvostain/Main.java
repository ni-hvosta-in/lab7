package nihvostain;

import common.exceptions.InputFromScriptException;
import common.exceptions.RecursionDepthExceededException;
import nihvostain.managers.CollectionManager;
import nihvostain.managers.Communication;
import nihvostain.managers.Invoker;

import java.io.IOException;
import java.sql.*;

public class Main {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1";
    public static void main(String[] args) throws IOException, ClassNotFoundException, RecursionDepthExceededException, InputFromScriptException {

        byte[] serializeReq = new byte[1024];

        CollectionManager collectionManager = new CollectionManager();
        collectionManager.load(System.getenv("MY_VAR"));
        int serverPort = 9898;
        int bufferCapacity = 10000;
        Communication communication = new Communication(serverPort, bufferCapacity);
        Invoker invoker = new Invoker(collectionManager, communication);
        Class.forName("org.postgresql.Driver");
        invoker.scanning();

         /*


        try {
            // Регистрация драйвера (не обязательно для новых версий JDBC)
            Class.forName("org.postgresql.Driver");

            // Установка соединения
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            if (connection != null) {
                System.out.println("Подключение к базе данных установлено!");

                Statement statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO PasswordUsers(login, password) VALUES ('alpin2', '123456')");
                // Здесь можно выполнять SQL-запросы

                // Не забудьте закрыть соединение
                connection.close();
            } else {
                System.out.println("Не удалось подключиться к базе данных");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC драйвер не найден");
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных");
        }


          */
    }
}