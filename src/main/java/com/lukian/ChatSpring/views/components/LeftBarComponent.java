package com.lukian.ChatSpring.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LeftBarComponent extends VerticalLayout {

    private Button homeButton = new Button(VaadinIcon.HOME.create());
    private Button userButton = new Button(VaadinIcon.USER.create());
    private Button commentButton = new Button(VaadinIcon.COMMENT.create());
    private Button starButton = new Button(VaadinIcon.STAR.create());
    private Button cogButton = new Button(VaadinIcon.COG.create());

    public LeftBarComponent() {
        for (Button b : new Button[]{homeButton,userButton,commentButton,starButton,cogButton}) {
            b.setWidth("40px");
            b.setHeight("40px");
            add(b);
        }
        setWidth("70px");
        setHeight("315px");
        getStyle().set("margin-top","80px");
        getStyle().set("margin-left","40px");
        getStyle().set("border", "1px solid");
        getStyle().set("border-radius","5px");
    }

}
