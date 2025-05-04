package nihvostain;

import common.exceptions.*;
import common.model.*;
import common.utility.*;
import nihvostain.managers.Communication;
import nihvostain.managers.Invoker;
import nihvostain.managers.Registration;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException {

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

        while (message != RegistrationMessage.AUTHORIZATION_SUCCESS | message != RegistrationMessage.REGISTRATION_SUCCESS) {

            System.out.print("введите логин ");
            String login = sc.nextLine().trim();

            System.out.print("введите пароль ");
            String password = sc.nextLine().trim();

            Registration registration = new Registration(login, password, communication);
            try {
                message = registration.register();
            } catch (TimeoutException e) {
                System.out.println("сервер временно не доступен");
            } catch (ClassNotFoundException e) {
                System.out.println("ошибка передачи данных, попробуйте снова");
            }
        }

        Invoker invoker = new Invoker(sc, communication);
        try {
            System.out.print("~ ");
            invoker.scanning();
        } catch (InputFromScriptException | RecursionDepthExceededException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}