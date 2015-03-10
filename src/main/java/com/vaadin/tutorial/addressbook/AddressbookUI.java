package com.vaadin.tutorial.addressbook;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.tutorial.addressbook.backend.Contact;
import com.vaadin.tutorial.addressbook.backend.ContactService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import javax.servlet.annotation.WebServlet;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/* 
 * UI class is the starting point for your app. You may deploy it with VaadinServlet
 * or VaadinPortlet by giving your UI class name a parameter. When you browse to your
 * app a web page showing your UI is automatically generated. Or you may choose to 
 * embed your UI to an existing web page. 
 */
@Title("Addressbook")
@Theme("valo")
public class AddressbookUI extends UI implements
        AbstractForm.SavedHandler<Contact>, AbstractForm.ResetHandler<Contact>,
        AbstractForm.DeleteHandler<Contact> {

    /**
     * Vaadin applications are basically just Serlvlets, so lets define one with
     * Servlet 3.0 style. Naturally you can use the plain old web.xml file as
     * well.
     */
    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = AddressbookUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    private ContactService service = ContactService.createDemoService();

    private TextField filter = new MTextField()
            .withInputPrompt("Filter contacts...");

    private Button newContact = new Button("New contact",
            new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    contactForm.setEntity(new Contact());
                    contactForm.focusFirst();
                }
            });

    private MTable<Contact> contactList = new MTable(Contact.class)
            .withProperties("firstName", "lastName", "email")
            .withFullWidth();

    private ContactForm contactForm = new ContactForm();

    @Override
    protected void init(VaadinRequest request) {
        // Configure components
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {

            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                listContacts(event.getText());
            }

        });

        contactForm.setVisible(false);

        contactList.addMValueChangeListener(
                new MValueChangeListener<Contact>() {

                    @Override
                    public void valueChange(MValueChangeEvent<Contact> event) {
                        Contact contact = event.getValue();
                        if (contact != null) {
                            contactForm.setEntity(contact);
                            contactForm.focusFirst();
                        } else {
                            contactForm.setVisible(false);
                        }
                    }
                });
        
        contactForm.setHandler(this);

        // Build main layout
        setContent(
                new HorizontalSplitPanel(
                        new MVerticalLayout()
                        .add(new MHorizontalLayout()
                                .expand(filter)
                                .add(newContact))
                        .expand(contactList),
                        contactForm
                )
        );

        // List initial content from the "backend"
        listContacts();
    }

    private void listContacts() {
        listContacts(filter.getValue());
    }

    private void listContacts(String text) {
        contactList.setBeans(service.findAll(text));
        contactForm.setVisible(false);
    }

    @Override
    public void onSave(Contact contact) {
        service.save(contact);
        listContacts();
    }

    @Override
    public void onReset(Contact entity) {
        contactList.setValue(null);
    }

    @Override
    public void onDelete(Contact entity) {
        service.delete(entity);
        listContacts();
    }

}
