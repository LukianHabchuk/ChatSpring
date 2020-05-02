package com.lukian.ChatSpring.views;

import com.lukian.ChatSpring.views.components.TopBarComponent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route("greet")
public class GreetingComponent extends Div
        implements HasUrlParameter<String> {

    @Override
    public void setParameter(BeforeEvent event,
                             String parameter) {
        add(new TopBarComponent());
        add(new H1("Greeting component"));
        add(new H1("test 2"));
    }
}