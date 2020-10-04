package com.lukian.chatspring.views.registration;

import com.lukian.chatspring.entity.data.User;
import com.lukian.chatspring.service.UserService;
import com.lukian.chatspring.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route("registration")
public class RegistrationView extends VerticalLayout {
//"user","email","pass",VaadinIcon.HOME

    private final TextField userName = new TextField("Name");
    private final TextField userEmail = new TextField("Email");
    private final PasswordField userPass = new PasswordField("Password");
    private final Button registerNewUser = new Button("registration");
    private final Button backToLogIn = new Button("back to log In");

    public RegistrationView(UserService service) {
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        add(new H1("registration view"));
        add(userName, userEmail, userPass, new HorizontalLayout(registerNewUser, backToLogIn));
        buttonListeners(service);
    }

    private String validation() {
        StringBuilder s = new StringBuilder();
        if (userName.isEmpty())
            s.append("username cant be empty").append("\n");
        if (userEmail.isEmpty())
            s.append("email cant be empty").append("\n");
        if (userPass.isEmpty())
            s.append("password cant be empty").append("\n");
        if (userPass.getValue().length() < 8)
            s.append("password too short").append("\n");
        return s.toString();
    }

    private void buttonListeners(UserService service) {
        backToLogIn.addClickListener(buttonClickEvent -> UI.getCurrent().navigate(LoginView.class));

        registerNewUser.addClickListener(buttonClickEvent -> {
            StringBuilder s = new StringBuilder();
            s.append(validation());
            List<User> userList = new ArrayList<>();
            if (s.toString().equals("")) {
                userList = service.findAllUsers().stream().filter(u ->
                        u.getNick().equals(userName.getValue())
                                || u.getEmail().equals(userEmail.getValue()))
                        .collect(Collectors.toList());

                if (!userList.isEmpty()) {
                    s.append("such user already exists");
                } else {
                    service.addUser(new User(userName.getValue(), userEmail.getValue(), userPass.getValue(), VaadinIcon.USER));
                    s.append("registration successfully");
                }
            }
            Dialog dialog = new Dialog();
            dialog.add(new Label(s.toString()));
            dialog.open();
            if (s.toString().equals("registration successfully")) {
                UI.getCurrent().navigate(LoginView.class);
            }

        });
    }
}
