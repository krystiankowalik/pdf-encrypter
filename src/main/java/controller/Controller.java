package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import logic.PdfEncryptionUtilities;
import model.PdfBatchJob;
import model.PdfFile;
import model.PdfJob;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private PdfEncryptionUtilities pdfEncryptionUtilities;

    @FXML
    private Button encryptButton;
    @FXML
    private Button decryptButton;
    @FXML
    private TextField targetPassword;
    @FXML
    private TextField sourcePassword;
    @FXML
    private Button clearButton;
    @FXML
    private ListView<Label> fileListView;

    public Button getClearButton() {
        return clearButton;
    }

    public ListView<Label> getFileListView() {
        return fileListView;
    }

    private PdfBatchJob pdfBatchJob;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fileListView.setOnDragOver(event -> {
            if (event.getGestureSource() != fileListView
                    && event.getDragboard().hasFiles()) {

                List<File> paths = event.getDragboard().getFiles();

                if (paths.stream().allMatch(f -> f.getName().trim().endsWith(".pdf"))) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
            }
            event.consume();
        });

        fileListView.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasFiles()) {
                PdfBatchJob pdfBatchJob = getBatchJobFromDragboard(dragboard);
                updateList(pdfBatchJob);
                success = true;
            }

            event.setDropCompleted(success);

            event.consume();
        });


        fileListView.setOnDragEntered(event -> {
            fileListView.setStyle("-fx-background-color: lightgray");
        });
        fileListView.setOnDragExited(event -> {
            fileListView.setStyle("-fx-background-color: white");
        });


        /*Clearing the entire list*/
        clearButton.setOnMouseClicked(event -> fileListView.getItems().clear());

        /*Handling multiple selection - activation*/
        fileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        /*Handling deleting individual items from the list*/
        fileListView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE) {
                fileListView.getItems().removeAll(fileListView.getSelectionModel().getSelectedItems());

                /*the below is to for the current selection to remain
                * the same after deleting an item - interface issue*/
                int currentlySelectedIndex = fileListView.getSelectionModel().getSelectedIndex();
                if (currentlySelectedIndex > 0
                        && currentlySelectedIndex + 1 < fileListView.getItems().size()) {
                    fileListView.getSelectionModel().clearSelection();
                    fileListView.getSelectionModel().select(currentlySelectedIndex + 1);
                }
            }
        });

        pdfEncryptionUtilities = new PdfEncryptionUtilities();

        encryptButton.setOnMouseClicked(event -> {
            pdfEncryptionUtilities.encrypt(getBatchJob());
        });

        decryptButton.setOnMouseClicked(event -> {
            pdfEncryptionUtilities.decrypt(getBatchJob());

        });

    }

    private void updateList(PdfBatchJob pdfBatchJob) {
        fileListView.getItems().clear();
        for(int i=0; i<pdfBatchJob.size();++i){
            fileListView.getItems().add(new Label(pdfBatchJob.get(i).getSourcePdfFile().getAbsolutePath()));
        }

    }

    private PdfBatchJob getBatchJobFromDragboard(Dragboard dragboard) {
        PdfBatchJob tmpPdfBatchJob = new PdfBatchJob();
        dragboard
                .getFiles()
                .stream()
                .map(File::getAbsolutePath)
                .peek(System.out::println)
                .filter(path -> path.endsWith(".pdf") && !fileListContains(path))
                .forEach(path -> tmpPdfBatchJob
                        .add(new PdfJob(
                                new PdfFile(path, sourcePassword.toString()),
                                new PdfFile(path, targetPassword.toString()))));
        return tmpPdfBatchJob;
    }

    private PdfBatchJob getBatchJob() {
        PdfBatchJob pdfBatchJob = new PdfBatchJob(FXCollections.observableArrayList());

        fileListView.getItems()
                .stream()
                .map(Labeled::getText)
                .forEach(path -> {
                    pdfBatchJob.add(new PdfJob(
                            new PdfFile(path, sourcePassword.getText()),
                            new PdfFile(path, targetPassword.getText())));

                });

        return pdfBatchJob;
    }

    private boolean fileListContains(String path) {
        return fileListView
                .getItems()
                .stream()
                .anyMatch(e -> e.getText().equals(path));
    }


}
