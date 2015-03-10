package com.vaadin.tutorial.addressbook;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;

/**
 * The part of the UI modifying a Contact instance.
 */
public class ContactForm extends AbstractForm {

    /* Vaadin contains an efficient data binding mechanism. DTO fields are bound
     to similarly named UI fields by naming convention, using @PropertyId annotation
     or manually. You can also autogenerate the fields, but typically this is not as 
     flexible as defining then in your UI code. */
    private TextField firstName = new MTextField("First name");
    private TextField lastName = new MTextField("Last name");
    private TextField phone = new MTextField("Phone");
    private TextField email = new MTextField("Email");
    private DateField birthDate = new DateField("Birth date");

    @Override
    protected Component createContent() {
        return new MFormLayout(getToolbar(), firstName, lastName, phone, email)
                .withMargin(true);
    }

}
