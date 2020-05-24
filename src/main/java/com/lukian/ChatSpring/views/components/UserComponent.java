package com.lukian.ChatSpring.views.components;

import com.lukian.ChatSpring.entity.User;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UserComponent extends VerticalLayout {

    public UserComponent(User user) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        horizontalLayout.add(user.getIcon().create());
        horizontalLayout.add(user.getNick());

        add(horizontalLayout);

        horizontalLayout.setHeight("20px");
        setSizeUndefined();
        setWidth("150px");
        getStyle().set("border", "1px solid");
        getStyle().set("border-radius","5px");
    }
}
