package com.example.etkinliktakipsystemi.Models;


public class Event {

    private Long eventID;
    private String eventName;
    private String eventPlace;
    private Long eventFinishDate;
    private String eventImage;
    private People people;


    public Event() {
    }

    public Event(Long eventID, String eventName, String eventPlace, Long eventFinishDate, String eventImage, People people) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventPlace = eventPlace;
        this.eventFinishDate = eventFinishDate;
        this.eventImage = eventImage;
        this.people = people;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public Long getEventFinishDate() {
        return eventFinishDate;
    }

    public void setEventFinishDate(Long eventFinishDate) {
        this.eventFinishDate = eventFinishDate;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", eventName='" + eventName + '\'' +
                ", eventPlace='" + eventPlace + '\'' +
                ", eventFinishDate=" + eventFinishDate +
                ", eventImage='" + eventImage + '\'' +
                ", people=" + people +
                '}';
    }
}
