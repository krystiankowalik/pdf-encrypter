package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import logic.PdfEncryptionUtilities;
import model.PdfBatchJob;
import model.PdfFile;
import model.PdfJob;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private PdfEncryptionUtilities pdfEncryptionUtilities;

    @FXML
    private Button encryptButton;
    @FXML
    private Button decryptButton;
    @FXML
    private TextField targetPasswordField;
    @FXML
    private TextField sourcePasswordField;
    @FXML
    private Button clearButton;
    @FXML
    private BorderPane mainPane;
    private TableView<PdfJob> pdfJobTableView;

    private PdfBatchJob pdfBatchJob;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pdfJobTableView = new TableView<>();
        mainPane.setCenter(pdfJobTableView);

        pdfBatchJob = new PdfBatchJob(FXCollections.observableArrayList());

        handleEncryptionButtons();
        bindColumnsToProperties();

        handleDragDroppedEvent();
        handleClearButton();
        handleDeleteKey();
    }

    private void bindColumnsToProperties() {
        TableColumn<PdfJob, String> pdfSourcePathColumn = new TableColumn<>("Path");
        TableColumn<PdfJob, String> pdfJobStatusColumn = new TableColumn<>("Status");

        pdfSourcePathColumn.setCellValueFactory(param -> param.getValue().getSourcePdfFile().pathnameProperty());
        pdfJobStatusColumn.setCellValueFactory(param -> param.getValue().getStatus().descriptionProperty());

        pdfJobTableView.setItems(pdfBatchJob.getPdfBatchJob());
        pdfJobTableView.getColumns().setAll(pdfSourcePathColumn, pdfJobStatusColumn);
    }

    private void handleEncryptionButtons() {
        pdfEncryptionUtilities = new PdfEncryptionUtilities();

        encryptButton.setOnMouseClicked(event -> {
            pdfBatchJob = pdfEncryptionUtilities.encrypt(pdfBatchJob);
        });

        decryptButton.setOnMouseClicked(event -> {
            pdfEncryptionUtilities.decrypt(pdfBatchJob);

        });
    }

    private void handleClearButton() {
        clearButton.setOnMouseClicked(event -> pdfJobTableView.getItems().clear());
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

    private void updatePdfBatchJobFromDragboard(Dragboard dragboard) {
        StringProperty sourcePassword = new SimpleStringProperty();
        StringProperty targetPassword = new SimpleStringProperty();
        sourcePassword.bind(sourcePasswordField.textProperty());
        targetPassword.bind(targetPasswordField.textProperty());

        dragboard
                .getFiles()
                .stream()
                .map(File::getAbsolutePath)
                .filter(path -> path.endsWith(".pdf") && !pdfBatchJob.containsSourceFile(path))
                .forEach(path -> pdfBatchJob
                        .add(new PdfJob(
                                new PdfFile(path, sourcePassword),
                                new PdfFile(path, targetPassword))));
    }
}
