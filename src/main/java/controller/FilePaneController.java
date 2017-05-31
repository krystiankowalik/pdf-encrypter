package controller;

import com.google.common.eventbus.Subscribe;
import event.EventBusProvider;
import event.type.DecryptButtonClickedEvent;
import event.type.EncryptButtonClickedEvent;
import event.type.SourcePasswordPropertyEvent;
import event.type.TargetPasswordPropertyEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import model.PdfBatchJob;
import model.PdfFile;
import model.PdfJob;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class FilePaneController implements Initializable {

    private PdfBatchJob pdfBatchJob;

    @FXML
    private TableView<PdfJob> pdfJobTableView;

    @FXML
    private ControlPaneController controlPaneController;

    private TableColumn<PdfJob, String> pdfSourcePathColumn;
    private TableColumn<PdfJob, String> pdfJobStatusColumn;

    public PdfBatchJob getPdfBatchJob() {
        return pdfBatchJob;
    }

    private StringProperty targetPasswordProperty = new SimpleStringProperty();
    private StringProperty sourcePasswordProperty = new SimpleStringProperty();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerEventBus();
        pdfBatchJob = new PdfBatchJob();

        bindColumnsToProperties();
        handleDragDroppedEvent();
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
        //sourcePasswordProperty.bind(controlPaneController.getSourcePasswordField().textProperty());
        //targetPasswordProperty.bind(controlPaneController.getTargetPasswordField().textProperty());

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
            }
        });
    }

    @Subscribe
    public void handleTargetPasswordPropertyChangeEvent(final TargetPasswordPropertyEvent targetPasswordPropertyEvent) {
        System.out.println("Target pass property: " + targetPasswordPropertyEvent.getProperty().getValue());
        targetPasswordProperty.bind(targetPasswordPropertyEvent.getProperty());
    }

    @Subscribe
    public void handleSourcePasswordPropertyChangeEvent(final SourcePasswordPropertyEvent sourcePasswordPropertyEvent) {
        System.out.println("Source pass property: " + sourcePasswordPropertyEvent.getProperty().getValue());
        sourcePasswordProperty.bind(sourcePasswordPropertyEvent.getProperty());

        pdfBatchJob.getPdfBatchJob().forEach(x-> System.out.println(x.getSourcePdfFile().passwordProperty().getValue()));
    }

    @Subscribe
    public void handleEncryptButtonClickedEvent(final EncryptButtonClickedEvent encryptButtonClickedEvent) {
        System.out.println("Encrypt Button clicked in: " + getClass().getSimpleName() + " too");
        // TODO: 31.05.17 To cover encryption logic
    }

    @Subscribe
    public void handleDecryptButtonClickedEvent(final DecryptButtonClickedEvent decryptButtonClickedEvent) {
        System.out.println("Decrypt Button clicked in: " + getClass().getSimpleName() + " too");
        // TODO: 31.05.17 To cover decryption logic
    }


    private void registerEventBus() {
        System.out.println("Registering EventBut in " + getClass().getSimpleName());
        EventBusProvider.getInstance().register(this);
    }



}
