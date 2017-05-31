package controller;

import event.EventBusProvider;
import event.type.DecryptButtonClickedEvent;
import event.type.EncryptButtonClickedEvent;
import event.type.SourcePasswordPropertyEvent;
import event.type.TargetPasswordPropertyEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ControlPaneController implements Initializable {

    @FXML
    private Button encryptButton;
    @FXML
    private Button decryptButton;
    @FXML
    private TextField targetPasswordField;
    @FXML
    private TextField sourcePasswordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerEventBus();
        postPasswordTextFieldsProperties();
        postEncryptionEvents();

    }

    private void postPasswordTextFieldsProperties() {
        targetPasswordField.setOnKeyTyped(event -> {
            System.out.println("Key typed! " + event.getText());
            TargetPasswordPropertyEvent targetPasswordPropertyEvent =
                    new TargetPasswordPropertyEvent(new SimpleStringProperty(
                            targetPasswordField.textProperty().getValue() + event.getCharacter()));
            EventBusProvider.getInstance().post(targetPasswordPropertyEvent);
        });

        sourcePasswordField.setOnKeyTyped(event -> {
            System.out.println("Key typed! " + event.getText());
            SourcePasswordPropertyEvent sourcePasswordPropertyEvent =
                    new SourcePasswordPropertyEvent(new SimpleStringProperty(
                            sourcePasswordField.textProperty().getValue() + event.getCharacter()));
            EventBusProvider.getInstance().post(sourcePasswordPropertyEvent);
        });
    }

    private void postEncryptionEvents() {
        encryptButton.setOnMouseClicked(event -> {
            System.out.println("Encrypt Button clicked in " + getClass().getSimpleName());
            EventBusProvider.getInstance().post(new EncryptButtonClickedEvent());
        });
        decryptButton.setOnMouseClicked(event -> {
            System.out.println("Decrypt Button clicked in " + getClass().getSimpleName());
            EventBusProvider.getInstance().post(new DecryptButtonClickedEvent());
        });
    }

    private void registerEventBus() {
        System.out.println("Registering EventBut in " + getClass().getSimpleName());
        EventBusProvider.getInstance().register(this);
    }
}
