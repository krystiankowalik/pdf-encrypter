package model.pdf;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;


public class PdfJob {

    private PdfFile sourcePdfFile;
    private PdfFile targetPdfFile;
    private Status status;

    public PdfJob(PdfFile sourcePdfFile, PdfFile targetPdfFile) {
        this.sourcePdfFile = sourcePdfFile;
        this.targetPdfFile = targetPdfFile;
        status = Status.NOT_STARTED;
    }


    public PdfFile getSourcePdfFile() {
        return sourcePdfFile;
    }

    public PdfFile getTargetPdfFile() {
        return targetPdfFile;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {

        NOT_STARTED("Not started", Color.GRAY),
        INVALID_PASSWORD("Invalid password", Color.RED),
        NO_SUCH_FILE("There is no such file", Color.RED),
        READ_ERROR("Unable to read from file", Color.RED),
        WRITE_ERROR("Unable to write to file", Color.RED),
        ACCESS_DENIED("No access to the file", Color.RED),
        NO_DOCUMENT("The document doesn't exist", Color.RED),
        UNEXPECTED_ERROR("Unexpected error occurred", Color.RED),
        SUCCESS_DECRYPT("Decrypted successfully", Color.GREEN),
        SUCCESS_ENCRYPT("Encrypted successfully", Color.GREEN);

        private StringProperty description;
        private Color color;
        final private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PdfJob.class);

        Status(String description, Color color) {
            this.description = new SimpleStringProperty(description);
            this.color = color;
        }

        public String getDescription() {
            return description.getValueSafe();
        }

        public StringProperty descriptionProperty() {
            return description;
        }

        public Color getColor() {
            return color;
        }
    }

    @Override
    public String toString() {
        return "PdfJob{" +
                "sourcePdfFile=" + sourcePdfFile +
                ", targetPdfFile=" + targetPdfFile +
                '}';
    }
}
