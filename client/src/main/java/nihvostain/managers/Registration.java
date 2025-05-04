package nihvostain.managers;

import common.managers.Deserialize;
import common.managers.Request;
import common.managers.ResponseRegistry;
import common.utility.RegistrationMessage;
import common.utility.TypeRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Registration {
    private final String login;
    private final String password;
    private final Communication communication;
    public Registration(String login, String password, Communication communication) {
        this.login = login;
        this.password = password;
        this.communication = communication;
    }

    public RegistrationMessage register() throws IOException, TimeoutException, ClassNotFoundException {
        ArrayList<String> user = new ArrayList<>();
        user.add(login);
        user.add(password);
        Request request = new Request(TypeRequest.REQUEST_REGISTRATION, user);
        communication.send(request.serialize());

        byte[] response = communication.receive();
        return new Deserialize<ResponseRegistry>(response).deserialize().getMessage();
    }
}
