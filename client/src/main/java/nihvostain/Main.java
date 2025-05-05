package nihvostain;

import common.exceptions.*;
import common.model.*;
import common.utility.*;
import nihvostain.managers.Communication;
import nihvostain.managers.Invoker;
import nihvostain.managers.Registration;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        System.out.println("Добро пожаловать в консоль для управления коллекцией StudyGroup");
        System.out.println("Название файла для чтения и записи в него должно передаваться при помощи переменной окружения MY_VAR");

        Coordinates.setFields(new FieldsCoordinate[]{FieldsCoordinate.X, FieldsCoordinate.Y});

        Person.setFields(new FieldsPerson[]{FieldsPerson.NAME, FieldsPerson.BIRTHDAY,
                FieldsPerson.PassportID, FieldsPerson.EyeCOLOR, FieldsPerson.HairCOLOR});

        StudyGroup.setFields(new FieldsStudyGroup[]{FieldsStudyGroup.NAME,
                FieldsStudyGroup.COORDINATES, FieldsStudyGroup.StudentsCount,
                FieldsStudyGroup.FormOFEducation, FieldsStudyGroup.SEMESTER, FieldsStudyGroup.GroupADMIN});


        Scanner sc = new Scanner(System.in);
        int timeout = 3000;
        int serverPort = 9898;  // Порт сервера (куда отправляем)
        int clientPort = 8888;
        int bufferCapacity = 10000;
        Communication communication = new Communication(serverPort, clientPort, bufferCapacity, timeout);
        RegistrationMessage message = null;

        //УБРАТЬ АВТОРИЗАЦИЮ В КЛАСС
        //СДЕЛАТЬ ВЫБОР СИМПОТИЧНЕЕ
        ArrayList<String> actions = new ArrayList<>();
        actions.add("1");
        actions.add("2");

        Map<String, TypeAuthentication> map = new HashMap<>();
        map.put(actions.get(0), TypeAuthentication.AUTHORIZATION);
        map.put(actions.get(1), TypeAuthentication.REGISTRATION);

        String login = "";
        String password = "";
        while (message != RegistrationMessage.AUTHORIZATION_SUCCESS && message != RegistrationMessage.REGISTRATION_SUCCESS) {

            TypeAuthentication action = null;
            while (true) {
                System.out.println("Выберите действие из списка:");
                map.forEach((key, value) -> System.out.println(key + ": " + value.getMessage()));
                System.out.print("Выберите действие ");
                String key = sc.nextLine().trim();
                if (!actions.contains(key)) {
                    System.out.println("Такого действия нет");
                } else {
                    action = map.get(key);
                    break;
                }

            }

            System.out.print("введите логин ");
            login = sc.nextLine().trim();

            System.out.print("введите пароль ");
            password = sc.nextLine().trim();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            password = hash.toString();
            Registration registration = new Registration(login, password, communication, action);
            try {
                message = registration.register();
                System.out.println(message);
            } catch (TimeoutException e) {
                System.out.println("сервер временно не доступен");
            } catch (ClassNotFoundException e) {
                System.out.println("ошибка передачи данных, попробуйте снова");
            }
        }

        Invoker invoker = new Invoker(sc, communication, login, password);
        try {
            System.out.print("~ ");
            invoker.scanning();
        } catch (InputFromScriptException | RecursionDepthExceededException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}