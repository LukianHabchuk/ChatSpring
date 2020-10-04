package com.lukian.chatspring.views;

import com.lukian.chatspring.views.components.TopBarComponent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("greet")
public class GreetingComponent extends VerticalLayout {

    GreetingComponent() {
        add(new TopBarComponent());
        add(new H1("Greeting component"));
        add(new H1("test 2"));
    }
}