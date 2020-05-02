package com.lukian.ChatSpring.views;

import com.lukian.ChatSpring.views.components.TopBarComponent;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
@CssImport("./styles/shared-styles.css")
public class MainView extends VerticalLayout {

        MainView() {
                setSizeFull();
                setClassName("mainview");
                add(new TopBarComponent());
                H1 h1 = new H1("main view");
                h1.setClassName("h1");
                add(h1);
                add(new H1("just for test..."));
        }
}
