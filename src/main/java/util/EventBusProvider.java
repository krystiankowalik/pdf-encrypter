package util;

import com.google.common.eventbus.EventBus;

public class EventBusProvider {

    private static EventBus eventBus;

    public static EventBus getInstance() {

        if (eventBus == null) {
            eventBus = new EventBus();
            return eventBus;
        } else {
            return eventBus;
        }
    }


}
