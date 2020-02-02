package com.example.etkinliktakipsystemi.Utils;

import com.example.etkinliktakipsystemi.Models.Event;

public class EventBusDataEvents {

    public static class SendEvent{
        private Event event;

        public SendEvent(Event event) {
            this.event = event;
        }

        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }
    }
}
