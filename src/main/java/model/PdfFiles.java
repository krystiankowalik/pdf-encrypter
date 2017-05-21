package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wd40 on 21.05.17.
 */
public class PdfFiles  {

    private List<PdfFile> pdfFiles = new ArrayList<>();

    public List<PdfFile> getPdfFiles() {
        return pdfFiles;
    }

    public void setPdfFiles(List<PdfFile> pdfFiles) {
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
