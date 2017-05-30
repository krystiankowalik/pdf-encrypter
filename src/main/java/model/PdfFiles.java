package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by wd40 on 21.05.17.
 */
public class PdfFiles  {

    private ObservableList<PdfFile> pdfFiles = FXCollections.observableArrayList();

    public List<PdfFile> getPdfFiles() {
        return pdfFiles;
    }

    public void setPdfFiles(ObservableList<PdfFile> pdfFiles) {
        this.pdfFiles = pdfFiles;
    }

    public int size(){
       return pdfFiles.size();
    }

    public PdfFile get(int index){
        return pdfFiles.get(index);
    }

    public void add(PdfFile pdfFile){
        pdfFiles.add(pdfFile);
    }
}
