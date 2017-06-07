package controller;

import com.google.common.eventbus.Subscribe;
import event.type.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import model.PdfBatchJob;
import model.PdfFile;
import model.PdfJob;
import org.apache.log4j.Logger;
import util.EventBusProvider;
import util.PdfEncryptionHandler;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class FilePaneController implements Initializable {

    final private Logger logger = Logger.getLogger(getClass());

    private PdfBatchJob pdfBatchJob;

    @FXML
    private TableView<PdfJob> pdfJobTableView;

    private StringProperty sourcePasswordProperty;
    private StringProperty targetPasswordProperty;

    private Stage primaryStage;
    private TableColumn<PdfJob, String> pdfJobStatusColumn;
    private TableColumn<PdfJob, String> pdfSourcePathColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pdfBatchJob = new PdfBatchJob();
        sourcePasswordProperty = new SimpleStringProperty();
        targetPasswordProperty = new SimpleStringProperty();

        registerEventBus();
        bindColumnsToProperties();
        colorStatusColumnCellsBasedOnValue();
        handleDragDroppedEvent();
        setDragEffects();
        handleDeleteKey();
    }





    @SuppressWarnings("unchecked")
    private void bindColumnsToProperties() {
        pdfSourcePathColumn = new TableColumn<>("Path");
        pdfJobStatusColumn = new TableColumn<>("Status");

        pdfSourcePathColumn.setCellValueFactory(param -> param.getValue().getSourcePdfFile().pathnameProperty());
        pdfJobStatusColumn.setCellValueFactory(param -> param.getValue().getStatus().descriptionProperty());

        pdfJobTableView.setItems(pdfBatchJob.getPdfBatchJob());
        pdfJobTableView.getColumns().setAll(pdfSourcePathColumn, pdfJobStatusColumn);
        pdfJobStatusColumn.maxWidthProperty().bind(pdfJobTableView.widthProperty().multiply(2.1));

    }

    private void colorStatusColumnCellsBasedOnValue() {
        pdfJobStatusColumn.setCellFactory(column -> new TableCell<PdfJob, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null) {
                            setText("");
                            setBackground(null);

                        } else {
                            PdfJob.Status pdfJobStatus = pdfBatchJob.get(getIndex()).getStatus();
                            //logger.debug("The status is " + pdfJobStatus);
                            setBackground(
                                    new Background(
                                            new BackgroundFill(pdfJobStatus.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
                            setTextFill(Paint.valueOf("#ffffff"));
                            setText(pdfJobStatus.getDescription());

                        }

                    }
                }
        );


    }

    private void handleDragDroppedEvent() {

        pdfJobTableView.setOnDragOver(event -> {
            if (event.getGestureSource() != pdfJobTableView
                    && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        pdfJobTableView.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasFiles()) {
                updatePdfBatchJobFromDragboard(dragboard);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }

    private void setDragEffects() {
        pdfJobTableView.setOnDragEntered(event -> {
            pdfJobTableView.setStyle("-fx-background-color: lightgray");
            pdfJobTableView.setPlaceholder(new Label("Drop them!"));
        });
        pdfJobTableView.setOnDragExited(event -> {
            pdfJobTableView.setStyle("-fx-background-color: white");
            pdfJobTableView.setPlaceholder(new Label("Drag your files here!"));

        });
    }

    private void updatePdfBatchJobFromDragboard(Dragboard dragboard) {

        dragboard
                .getFiles()
                .stream()
                .map(File::getAbsolutePath)
                .filter(path -> path.endsWith(".pdf") && !pdfBatchJob.containsSourceFile(path))
                .forEach(path -> pdfBatchJob.add(new PdfJob(
                        new PdfFile(path, sourcePasswordProperty),
                        new PdfFile(path, targetPasswordProperty))));
    }

    private void handleDeleteKey() {
        pdfJobTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        pdfJobTableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE) {
                pdfJobTableView.getItems().removeAll(pdfJobTableView.getSelectionModel().getSelectedItems());

                int currentlySelectedIndex = pdfJobTableView.getSelectionModel().getSelectedIndex();
                if (currentlySelectedIndex > 0
                        && currentlySelectedIndex + 1 < pdfJobTableView.getItems().size()) {
                    pdfJobTableView.getSelectionModel().clearSelection();
                    pdfJobTableView.getSelectionModel().select(currentlySelectedIndex + 1);
                }
                logger.debug("After delete: " + pdfBatchJob.toString());
            }
        });

    }

    @Subscribe
    public void handleTargetPasswordPropertyChangeEvent(final TargetPasswordPropertyEvent targetPasswordPropertyEvent) {
        logger.debug("Target pass property: " + targetPasswordPropertyEvent.getProperty().getValue());
        targetPasswordProperty.bind(targetPasswordPropertyEvent.getProperty());
    }

    @Subscribe
    public void handleSourcePasswordPropertyChangeEvent(final SourcePasswordPropertyEvent sourcePasswordPropertyEvent) {
        logger.debug("Source pass property: " + sourcePasswordPropertyEvent.getProperty().getValue());
        sourcePasswordProperty.bind(sourcePasswordPropertyEvent.getProperty());
    }

    @Subscribe
    public void handleEncryptButtonClickedEvent(final EncryptButtonClickedEvent encryptButtonClickedEvent) {
        logger.debug("Encrypt Button signal received in: " + getClass().getSimpleName() + " too");
        PdfEncryptionHandler.getInstance().encrypt(pdfBatchJob);
    }

    @Subscribe
    public void handleDecryptButtonClickedEvent(final DecryptButtonClickedEvent decryptButtonClickedEvent) {
        logger.debug("Decrypt Button signal received in: " + getClass().getSimpleName() + " too");
        PdfEncryptionHandler.getInstance().decrypt(pdfBatchJob);
    }

    @Subscribe
    public void handleApplicationStart(final ApplicationStartEvent applicationStartEvent) {
        logger.debug(applicationStartEvent.getClass().getSimpleName() + " received");
        primaryStage = applicationStartEvent.getStage();
    }

    @Subscribe
    public void handleClearButtonClickedEvent(final ClearButtonClickedEvent clearButtonClickedEvent) {
        logger.debug("Clear Button signal received in: " + getClass().getSimpleName() + " too");
        pdfBatchJob.clear();
    }

    private void registerEventBus(){
        EventBusProvider.getInstance().register(this);
    }

}
