package event;

import com.google.common.eventbus.EventBus;

public class EventBusProvider {

    private static EventBus eventBus;

    public static EventBus getInstance() {

        if (eventBus == null) {
            System.out.println("eventBus" + " is null. I'm creating a new one");
            eventBus = new EventBus();
            return eventBus;
        } else {
            System.out.println("eventBus"+ " exists. I'm returning the existing instance");
            return eventBus;
        }
    }


}
