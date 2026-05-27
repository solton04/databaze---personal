package cz.vse.java.funwithjpa;

import cz.vse.java.funwithjpa.controller.Controller;
import cz.vse.java.funwithjpa.gui.Gui;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class App extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    private static EntityManagerFactory EMF;

    private Gui gui;

    public static void main(String[] args) {
        LOG.info("Application started.");

        try {
            Properties props = new Properties();
            try (InputStream input = App.class.getClassLoader().getResourceAsStream("database.properties")) {
                if (input == null) {
                    LOG.error("Sorry, unable to find database.properties");
                } else {
                    props.load(input);
                }
            }

            Map<String, String> properties = new HashMap<>();
            for (String key : props.stringPropertyNames()) {
                properties.put(key, props.getProperty(key));
            }

            EMF = Persistence.createEntityManagerFactory("punit", properties);
        } catch (Exception e) {
            LOG.error("Failed to initialize EntityManagerFactory", e);
        }

        Application.launch(args);

        if (EMF != null) {
            EMF.close();
        }
        LOG.info("Application terminated.");
    }

    @Override
    public void start(final Stage stage) throws Exception {
        gui = new Gui(new Controller(EMF));

        Scene scene = new Scene(new Group());
        ((Group) scene.getRoot()).getChildren().addAll(gui.getPane());

        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }
}
