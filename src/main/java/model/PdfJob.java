package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by wd40 on 21.05.17.
 */
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

        NOT_STARTED("Not started"),
        INVALID_PASSWORD("Invalid password"),
        NO_SUCH_FILE("There is no such file"),
        READ_ERROR("Unable to read from file"),
        WRITE_ERROR("Unable to write to file"),
        ACCESS_DENIED("No access to the file"),
        NO_DOCUMENT("The document doesn't exist"),
        UNEXPECTED_ERROR("Unexpected error occurred"),
        SUCCESS_DECRYPT("Decrypted successfully"),
        SUCCESS_ENCRYPT("Encrypted successfully");

        private StringProperty description;

        Status(String description) {
            this.description = new SimpleStringProperty(description);
        }

        public String getDescription() {
            return description.get();
        }

        public StringProperty descriptionProperty() {
            return description;
        }
    }

}
