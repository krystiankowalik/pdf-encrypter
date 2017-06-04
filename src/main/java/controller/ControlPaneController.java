package controller;

import event.type.*;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.apache.log4j.Logger;
import util.EventBusProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ControlPaneController implements Initializable {

    final private Logger logger = Logger.getLogger(getClass());

    @FXML
    private Button encryptButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button decryptButton;
    @FXML
    private TextField targetPasswordField;
    @FXML
    private TextField sourcePasswordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerEventBus();
        postPasswordTextFieldsPropertiesEvents();
        postButtonEvents();

    }

    private void postPasswordTextFieldsPropertiesEvents() {

        targetPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug(targetPasswordField.getClass().getSimpleName() + " has changed");
            TargetPasswordPropertyEvent targetPasswordPropertyEvent =
                    new TargetPasswordPropertyEvent(new SimpleStringProperty(newValue));
            EventBusProvider.getInstance().post(targetPasswordPropertyEvent);
        });

        sourcePasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug(targetPasswordField.getClass().getSimpleName() + " has changed");
            SourcePasswordPropertyEvent sourcePasswordPropertyEvent =
                    new SourcePasswordPropertyEvent(new SimpleStringProperty(newValue));
            EventBusProvider.getInstance().post(sourcePasswordPropertyEvent);
        });

    }

    private void postButtonEvents() {
        encryptButton.setOnMouseClicked(event -> {
            logger.debug("Encrypt Button clicked in " + getClass().getSimpleName());
            EventBusProvider.getInstance().post(new EncryptButtonClickedEvent());
        });
        decryptButton.setOnMouseClicked(event -> {
            logger.debug("Decrypt Button clicked in " + getClass().getSimpleName());
            EventBusProvider.getInstance().post(new DecryptButtonClickedEvent());
        });
        clearButton.setOnMouseClicked(event -> {
            logger.debug("Clear Button clicked in " + getClass().getSimpleName());
            EventBusProvider.getInstance().post(new ClearButtonClickedEvent());
        });
    }

    private void registerEventBus() {
        EventBusProvider.getInstance().register(this);
    }
}
