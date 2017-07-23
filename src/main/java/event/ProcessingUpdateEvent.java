package event;

import model.application.ApplicationStatus;

public class ProcessingUpdateEvent {

    private ApplicationStatus applicationStatus;

    public ProcessingUpdateEvent(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }
}
