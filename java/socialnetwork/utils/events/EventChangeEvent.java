package socialnetwork.utils.events;

import socialnetwork.domain.EventUser;

public class EventChangeEvent implements Event {
    private ChangeEventType type;
    private EventUser data, oldData;

    public EventChangeEvent(ChangeEventType type, EventUser data) {
        this.type = type;
        this.data = data;
    }
    public EventChangeEvent(ChangeEventType type, EventUser data, EventUser oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public EventUser getData() {
        return data;
    }

    public EventUser getOldData() {
        return oldData;
    }

}
