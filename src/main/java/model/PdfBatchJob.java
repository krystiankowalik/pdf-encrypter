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

   /* private void  areSizesEqual() throws UnexpectedException {
        if(pdfSourceFiles==null || pdfTargetFiles==null){
            return;
        }
        if( pdfSourceFiles.size() != pdfTargetFiles.size()){
            throw new UnexpectedException("Sizes of source files and target files are not equal");

        }

    }*/

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

    public void setPdfBatchJob(ObservableList<PdfJob> pdfBatchJob) {
        this.pdfBatchJob = pdfBatchJob;
    }
}
