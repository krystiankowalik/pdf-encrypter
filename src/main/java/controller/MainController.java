package controller;

import com.google.common.eventbus.Subscribe;
import event.type.ApplicationStartEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.pdf.PdfBatchJob;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    final private Logger logger = Logger.getLogger(getClass());
    @FXML
    private BorderPane mainPane;

    private PdfBatchJob pdfBatchJob;
    private Stage primaryStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Subscribe
    public void handleApplicationStart(final ApplicationStartEvent applicationStartEvent) {
        logger.debug(applicationStartEvent.getClass().getSimpleName() + " received");
        primaryStage = applicationStartEvent.getStage();
    }
}
