package event.type;

import javafx.beans.property.StringProperty;

public final class SourcePasswordPropertyEvent {

    private final StringProperty textProperty;

    public SourcePasswordPropertyEvent(StringProperty textProperty) {

        this.textProperty = textProperty;
    }

    public StringProperty getProperty() {
        return this.textProperty;
    }
}
