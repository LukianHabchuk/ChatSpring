package com.lukian.chatspring.views.login;

import com.lukian.chatspring.views.registration.RegistrationView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.util.Collections;


@Route("login")
@PageTitle("Login | Vaadin Chat")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private static final String URL = "/oauth2/authorization/google";

    private LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        Button registration = new Button("registration");
        Anchor googleLoginButton = new Anchor(URL, "signInGoogle");

        login.setAction("login");
        registration.addClickListener(buttonClickEvent -> UI.getCurrent().navigate(RegistrationView.class));
        add(new H1("Vaadin Chat"), login, registration, googleLoginButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .getOrDefault("error", Collections.emptyList())
                .isEmpty()) {
            login.setError(true);
        }
    }
}
