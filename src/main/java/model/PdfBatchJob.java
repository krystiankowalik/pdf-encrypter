package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PdfBatchJob {

    private ObservableList<PdfJob> pdfBatchJob;

    // TODO: 29.05.17 Some error handling in the event the lists' sizes are not equal


    public PdfBatchJob(ObservableList<PdfJob> pdfBatchJob) {
        this.pdfBatchJob = pdfBatchJob;
    }

    public PdfBatchJob() {
        this.pdfBatchJob = FXCollections.observableArrayList();
    }

    public int size() {
        return pdfBatchJob.size();
    }

    public PdfJob get(int index) {
        return pdfBatchJob.get(index);
    }

    public void set(int index, PdfJob pdfJob){
        pdfBatchJob.set(index,pdfJob);
    }

    public void add(PdfJob pdfJob) {
        pdfBatchJob.add(pdfJob);
    }

    public boolean containsSourceFile(String file) {
        return pdfBatchJob
                .stream()
                .anyMatch(pdfJob -> pdfJob
                        .getSourcePdfFile()
                        .getPathname()
                        .equals(file));
    }

    public ObservableList<PdfJob> getPdfBatchJob() {
        return pdfBatchJob;
    }

    public void clear(){
        pdfBatchJob.clear();
    }

    public void setPdfBatchJob(ObservableList<PdfJob> pdfBatchJob) {
        this.pdfBatchJob = pdfBatchJob;
    }

    @Override
    public String toString() {
        return "PdfBatchJob{" +
                "pdfBatchJob=" + pdfBatchJob +
                '}';
    }
}
