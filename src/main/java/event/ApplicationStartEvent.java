package event;

import javafx.stage.Stage;

public final class ApplicationStartEvent {

    private Stage stage;

    public ApplicationStartEvent(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}
