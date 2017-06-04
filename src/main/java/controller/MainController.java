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


}
