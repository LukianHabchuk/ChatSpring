package com.lukian.chatspring.views.components;

import com.lukian.chatspring.entity.data.User;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UserComponent extends VerticalLayout {

    public UserComponent(User user) {
        setClassName("UserComponent");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(user.getIcon().create());
        horizontalLayout.add(user.getNick());
        add(horizontalLayout);

        horizontalLayout.setHeight("20px");
        setSizeUndefined();
        setWidth("150px");
    }
}
