package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.PdfBatchJob;
import model.PdfJob;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private FilePaneController filePaneController;
    @FXML
    private MenuPaneController menuPaneController;


    private PdfBatchJob pdfBatchJob;

    public PdfBatchJob getPdfBatchJob() {
        return pdfBatchJob;
    }

    public void setPdfBatchJob(PdfBatchJob pdfBatchJob) {
        this.pdfBatchJob = pdfBatchJob;
    }

    public void addPdfJob(PdfJob pdfJob) {
        pdfBatchJob.add(pdfJob);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
/*


    private PdfEncryptionHandler pdfEncryptionUtilities;

    private TableColumn<PdfJob, String> pdfSourcePathColumn;
    private TableColumn<PdfJob, String> pdfJobStatusColumn;

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

    @FXML
    private TableView<PdfJob> pdfJobTableView;

    private PdfBatchJob pdfBatchJob;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pdfBatchJob = new PdfBatchJob(FXCollections.observableArrayList());

        bindColumnsToProperties();

        handleDragDroppedEvent();
        handleClearButton();
        handleDeleteKey();
        handleEncryptionButtons();
    }


    @SuppressWarnings("unchecked")
    private void bindColumnsToProperties() {
        pdfSourcePathColumn = new TableColumn<>("Path");
        pdfJobStatusColumn = new TableColumn<>("Status");

        pdfJobStatusColumn.maxWidthProperty().bind(pdfJobTableView.widthProperty().multiply(2.1));

        pdfSourcePathColumn.setCellValueFactory(param -> param.getValue().getSourcePdfFile().pathnameProperty());
        pdfJobStatusColumn.setCellValueFactory(param -> param.getValue().getStatus().descriptionProperty());

        pdfJobTableView.setItems(pdfBatchJob.getPdfBatchJob());
        pdfJobTableView.getColumns().setAll(pdfSourcePathColumn, pdfJobStatusColumn);

    }

    private void handleEncryptionButtons() {
        pdfEncryptionUtilities = new PdfEncryptionHandler();

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
        StringProperty sourcePasswordProperty = new SimpleStringProperty();
        StringProperty targetPasswordProperty = new SimpleStringProperty();
        sourcePasswordProperty.bind(sourcePasswordField.textProperty());
        targetPasswordProperty.bind(targetPasswordField.textProperty());

        dragboard
                .getFiles()
                .stream()
                .map(File::getAbsolutePath)
                .filter(path -> path.endsWith(".pdf") && !pdfBatchJob.containsSourceFile(path))
                .forEach(path -> pdfBatchJob
                        .add(new PdfJob(
                                new PdfFile(path, sourcePasswordProperty),
                                new PdfFile(path, targetPasswordProperty))));
    }

*/

}
