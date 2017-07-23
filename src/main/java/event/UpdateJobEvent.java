package event;

import model.pdf.PdfJob;

public class UpdateJobEvent {

    private PdfJob pdfJob;

    public UpdateJobEvent(PdfJob pdfJob) {
        this.pdfJob = pdfJob;
    }

    public PdfJob getPdfJob() {
        return pdfJob;
    }

    public void setPdfJob(PdfJob pdfJob) {
        this.pdfJob = pdfJob;
    }

    @Override
    public String toString() {
        return "UpdateJobEvent{" +
                "pdfJob=" + pdfJob +
                '}';
    }
}
