package model.pdf;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

import java.util.stream.Collectors;

public class PdfBatchJob {

    private ObservableList<PdfJob> pdfBatchJob;
    final private Logger logger = Logger.getLogger(getClass());

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

    private PdfFiles getSourceFiles() {
        return new PdfFiles(
                pdfBatchJob
                        .stream()
                        .map(PdfJob::getSourcePdfFile)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));

    }

    private PdfFiles getTargetFiles() {
        return new PdfFiles(
                pdfBatchJob
                        .stream()
                        .map(PdfJob::getTargetPdfFile)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));

    }

    public void set(int index, PdfJob pdfJob) {
        pdfBatchJob.set(index, pdfJob);
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

    public void clear() {
        pdfBatchJob.clear();
    }

    public void setPdfBatchJob(ObservableList<PdfJob> pdfBatchJob) {
        this.pdfBatchJob = pdfBatchJob;
    }

    public void compareSourceAndTargetSizes() {
        if (getSourceFiles().size() != getTargetFiles().size()) {
            logger.warn("Sizes of source file list and target file list are not equal in: " + pdfBatchJob);
        } else{
            logger.info("Sizes of source file list and target file list are equal.");
        }
    }

    @Override
    public String toString() {
        return "PdfBatchJob{" +
                "pdfBatchJob=" + pdfBatchJob +
                '}';
    }
}
