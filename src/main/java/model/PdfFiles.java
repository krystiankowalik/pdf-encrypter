package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class PdfFiles {

    private ObservableList<PdfFile> pdfFiles = FXCollections.observableArrayList();

    public ObservableList<PdfFile> getPdfFiles() {
        return pdfFiles;
    }

    public void setPdfFiles(ObservableList<PdfFile> pdfFiles) {
        this.pdfFiles = pdfFiles;
    }

    public int size() {
        return pdfFiles.size();
    }

    public PdfFile get(int index) {
        return pdfFiles.get(index);
    }

    public void add(PdfFile pdfFile) {
        pdfFiles.add(pdfFile);
    }

    @Override
    public String toString() {
        return "PdfFiles{" +
                "pdfFiles=" + pdfFiles +
                '}';
    }
}
