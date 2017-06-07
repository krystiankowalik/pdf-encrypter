package model.application;

/**
 * Created by wd40 on 08.06.17.
 */
public enum ApplicationStatus {

    IDLE(""),
    PROCESSING("Processing"),
    FINISHED("Processed successfully");

    String description;

    ApplicationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
