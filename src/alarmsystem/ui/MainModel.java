package alarmsystem.ui;

import alarmsystem.events.Event;

import java.util.ArrayList;
import java.util.List;

public class MainModel {

    private final boolean[] sensors = new boolean[10];
    private final List<Event> events = new ArrayList<>();

    public boolean[] getSensors() {
        return sensors;
    }

    public List<Event> getEvents() {
        return events;
    }
}
