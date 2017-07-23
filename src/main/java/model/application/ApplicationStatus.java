package model.application;

public enum ApplicationStatus {

    IDLE(""),
    PROCESSING("Processing"),
    FINISHED("Finished");

    String description;

    ApplicationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
