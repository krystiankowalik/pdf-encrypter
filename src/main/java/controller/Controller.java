package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import logic.PdfUtilities;
import model.PdfFile;
import model.PdfFiles;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable {

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
    private ListView<Label> fileList;

    public Button getClearButton() {
        return clearButton;
    }

    public ListView<Label> getFileList() {
        return fileList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fileList.setOnDragOver(event -> {
            if (event.getGestureSource() != fileList
                    && event.getDragboard().hasFiles()) {
                /*List<File> paths = event.getDragboard().getFiles();*/
                /*if (paths.stream().allMatch(f->f.getName().trim().endsWith(".pdf"))) {*/
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                /*}*/
            }
            event.consume();
        });

        fileList.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                dragboard.getFiles().stream()
                        .map(file->file.getAbsolutePath())
                        .filter(path -> path.endsWith(".pdf") && !fileListContains(path))
                        .forEach(e -> fileList.getItems().add(new Label(e)));
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });

        fileList.setOnDragEntered(event -> {
            fileList.setStyle("-fx-background-color: lightgray");
        });
        fileList.setOnDragExited(event -> {
            fileList.setStyle("-fx-background-color: white");
        });

        /*Clearing the entire list*/
        clearButton.setOnMouseClicked(event -> fileList.getItems().clear());

        /*Handling multiple selection - activation*/
        fileList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        /*Handling deleting individual items from the list*/
        fileList.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DELETE) {
                fileList.getItems().removeAll(fileList.getSelectionModel().getSelectedItems());

                /*the below is to for the current selection to remain
                * the same after deleting an item - interface issue*/
                int currentlySelectedIndex = fileList.getSelectionModel().getSelectedIndex();
                if (currentlySelectedIndex > 0
                        && currentlySelectedIndex + 1 < fileList.getItems().size()) {
                    fileList.getSelectionModel().clearSelection();
                    fileList.getSelectionModel().select(currentlySelectedIndex + 1);
                }
            }
        });

        encryptButton.setOnMouseClicked(event -> {
            preparePdfUtilities().batchEncrypt();
        });

        decryptButton.setOnMouseClicked(event -> {
            preparePdfUtilities().batchDecrypt();
        });

    }

    private PdfUtilities preparePdfUtilities() {
        PdfFiles pdfSourceFiles = new PdfFiles();
        PdfFiles pdfTargetFiles = new PdfFiles();


        fileList.getItems()
                .stream()
                .map(Labeled::getText)
                .forEach(path -> pdfSourceFiles.add(new PdfFile(path, sourcePassword.getText())));
        fileList.getItems()
                .stream()
                .map(Labeled::getText)
                .forEach(path -> pdfTargetFiles.add(new PdfFile(path, targetPassword.getText())));

        return new PdfUtilities(pdfSourceFiles, pdfTargetFiles);
    }

    private boolean fileListContains(String path) {
        return fileList
                .getItems()
                .stream()
                .anyMatch(e -> e.getText().equals(path));
    }


}
