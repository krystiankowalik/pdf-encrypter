package controller;

import com.google.common.eventbus.Subscribe;
import event.type.ApplicationStartEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.pdf.PdfBatchJob;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    final private Logger logger = Logger.getLogger(getClass());

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
