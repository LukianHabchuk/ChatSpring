package com.lukian.chatspring.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LeftBarComponent extends VerticalLayout {

    public LeftBarComponent() {
        setClassName("LeftBarComponent");
        Button homeButton = new Button(VaadinIcon.HOME.create());
        Button userButton = new Button(VaadinIcon.USER.create());
        Button commentButton = new Button(VaadinIcon.COMMENT.create());
        Button starButton = new Button(VaadinIcon.STAR.create());
        Button cogButton = new Button(VaadinIcon.COG.create());
        for (Button b : new Button[]{homeButton, userButton, commentButton, starButton, cogButton}) {
            b.setWidth("40px");
            b.setHeight("40px");
            add(b);
        }
        setWidth("70px");
        setHeight("315px");
    }

}
