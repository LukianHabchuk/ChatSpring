package com.lukian.chatspring.views.components;

import com.lukian.chatspring.views.GreetingComponent;
import com.lukian.chatspring.views.MainView;
import com.lukian.chatspring.views.chat.ChatView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class TopBarComponent extends HorizontalLayout {

    public TopBarComponent() {
        setSizeFull();
        setWidth("100%");
        setHeight("10%");
        add(routerLink());
        Button logout = new Button("Log out");

        logout.addClickListener(buttonClickEvent -> logout());

        add(logout);
    }

    private void logout() {
        // Close the VaadinServiceSession
        UI.getCurrent().getSession().close();
        // Redirect to avoid keeping the removed UI open in the browser
        UI.getCurrent().getPage().setLocation("/logout");
    }

    private Div routerLink() {
        Div menu = new Div();
        Button mainView = new Button("MainView");
        Button chatView = new Button("ChatView");
        Button greetingView = new Button("Greeting");
        mainView.addClickListener(c -> UI.getCurrent().navigate(MainView.class));
        chatView.addClickListener(buttonClickEvent -> UI.getCurrent().navigate(ChatView.class));
        greetingView.addClickListener(buttonClickEvent -> UI.getCurrent().navigate(GreetingComponent.class));

        menu.add(mainView, chatView, greetingView);
        return menu;
    }

}
