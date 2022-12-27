/**
 * An event is made up of an Entity that is taking an
 * Action a specified time.
 */
public final class Event
{
    private Action action;
    private long time;
    private Entity entity;

    public Action getAction(){return this.action;}
    public long getTime(){return this.time;}
    public Entity getEntity(){return this.entity;}
    public Event(Action action, long time, Entity entity) {
        this.action = action;
        this.time = time;
        this.entity = entity;
    }
}
