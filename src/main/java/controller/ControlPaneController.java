package controller;

import com.google.common.eventbus.Subscribe;
import event.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.application.ApplicationStatus;
import org.apache.log4j.Logger;
import util.EventBusProvider;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControlPaneController implements Initializable {

    final private Logger logger = Logger.getLogger(getClass());
    @FXML
    private Label applicationStatusIndicatorText;

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
            TargetPasswordPropertyEvent targetPasswordPropertyEvent =
                    new TargetPasswordPropertyEvent(new SimpleStringProperty(newValue));
            EventBusProvider.getInstance().post(targetPasswordPropertyEvent);
        });

        sourcePasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            SourcePasswordPropertyEvent sourcePasswordPropertyEvent =
                    new SourcePasswordPropertyEvent(new SimpleStringProperty(newValue));
            EventBusProvider.getInstance().post(sourcePasswordPropertyEvent);
        });

    }

    private void postButtonEvents() {
        encryptButton.setOnMouseClicked(event -> {
            logger.debug("Encrypt Button clicked in " + getClass().getSimpleName());
            applicationStatusIndicatorText.setText(ApplicationStatus.PROCESSING.getDescription());
            EventBusProvider.getInstance().post(new EncryptButtonClickedEvent());
        });
        decryptButton.setOnMouseClicked(event -> {
            logger.debug("Decrypt Button clicked in " + getClass().getSimpleName());
            applicationStatusIndicatorText.setText(ApplicationStatus.PROCESSING.getDescription());
            EventBusProvider.getInstance().post(new DecryptButtonClickedEvent());
        });
        clearButton.setOnMouseClicked(event -> {
            logger.debug("Clear Button clicked in " + getClass().getSimpleName());
            EventBusProvider.getInstance().post(new ClearButtonClickedEvent());
        });
    }

    @Subscribe
    public void setApplicationStatus(final ApplicationStatus applicationStatus) {
            String statusDescription = applicationStatus.getDescription();
            applicationStatusIndicatorText.setText(statusDescription);
            logger.info(statusDescription);
    }

    private void registerEventBus() {
        EventBusProvider.getInstance().register(this);
    }
}

