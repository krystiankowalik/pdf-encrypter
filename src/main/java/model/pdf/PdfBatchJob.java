package model.pdf;

import com.google.common.collect.Lists;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public void set(String id, PdfJob pdfJob) {
        logger.debug("Set pdfBatchJob before: " + pdfBatchJob);
        pdfBatchJob
                .filtered(job -> Objects.equals(job.getId(), id))
                .forEach(job -> job = pdfJob);
        logger.debug("Set pdfBatchJob after: " + pdfBatchJob);

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

    public List<PdfBatchJob> split(int partsCount) {
        List<PdfBatchJob> pdfBatchJobs = new ArrayList<>();
        int partitionSize = pdfBatchJob.size() / partsCount;
        if (partsCount > pdfBatchJob.size()) {
            partitionSize = pdfBatchJob.size();
        }
        List<List<PdfJob>> tmpList = Lists.partition(pdfBatchJob, partitionSize);
        tmpList.forEach(e -> pdfBatchJobs.add(new PdfBatchJob(FXCollections.observableArrayList(e))));
        return pdfBatchJobs;
    }

    public void compareSourceAndTargetSizes() {
        if (getSourceFiles().size() != getTargetFiles().size()) {
            logger.warn("Sizes of source file list and target file list are not equal in: " + pdfBatchJob);
        } else {
            logger.info("Sizes of source file list and target file list are equal.");
        }
    }

    public enum Type {
        ENCRYPT, DECRYPT
    }

    @Override
    public String toString() {
        return "PdfBatchJob{" +
                "pdfBatchJob=" + pdfBatchJob +
                '}';
    }
}
