package event;

import javafx.beans.property.StringProperty;

public final class TargetPasswordPropertyEvent {

    private final StringProperty textProperty;

    public TargetPasswordPropertyEvent(StringProperty textProperty) {
        this.textProperty = textProperty;
    }

    public StringProperty getProperty() {
        return this.textProperty;
    }
}
