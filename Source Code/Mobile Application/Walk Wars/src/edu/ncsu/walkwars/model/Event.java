package edu.ncsu.walkwars.model;

public class Event
{

    private String eventName;
    private String pointValue;
    private boolean completed;

    public Event(String eventName, String pointValue, boolean completed)
    {
        this.setEvent(eventName);
        this.setPointValue(pointValue);
        this.setCompleted(completed);
    }

    public String getEvent()
    {
        return eventName;
    }

    public void setEvent(String event)
    {
        this.eventName = event;
    }

    public String getPointValue()
    {
        return pointValue;
    }

    public void setPointValue(String pointValue)
    {
        this.pointValue = pointValue;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }

}
