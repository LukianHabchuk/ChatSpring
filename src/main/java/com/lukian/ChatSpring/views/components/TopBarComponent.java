package com.lukian.ChatSpring.views.components;

import com.lukian.ChatSpring.views.GreetingComponent;
import com.lukian.ChatSpring.views.MainView;
import com.lukian.ChatSpring.views.chat.ChatView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

public class TopBarComponent extends HorizontalLayout {

    @Autowired
    public TopBarComponent() {
        setSizeFull();
//        getStyle().set("background-color","#03A9F4");
//        getStyle().set("text-color","white");
        setWidth("100%");
        setHeight("10%");
        add(routerLink());
        add(new Anchor("/logout","Log out"));
    }

    Div routerLink() {
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
