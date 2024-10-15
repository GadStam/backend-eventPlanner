package com.backend.scheduleAPP.service;

import com.backend.scheduleAPP.model.Event;
import com.backend.scheduleAPP.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service

public class ScheduleService {

    private final EventRepository eventRepository;
    public ScheduleService (EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    public String schedule(LocalDateTime givenDateTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(currentTime, givenDateTime);


        long days = duration.toDaysPart();
        long hours = duration.toHoursPart() % 24;
        long minutes = duration.toMinutesPart() % 60;


        StringBuilder remainingTime = new StringBuilder();


        if (days == 0) {
            remainingTime.append("0 days");
        } else {
            remainingTime.append(days + " day" + (days > 1 ? "s" : ""));
        }


        if (hours > 0 || minutes > 0) {
            remainingTime.append(", ");
        }


        if (hours == 0) {
            remainingTime.append("0 hours");
        } else {
            remainingTime.append(hours + " hour" + (hours > 1 ? "s" : "")); // Pluralize "hour"
        }


        if (minutes > 0) {
            remainingTime.append(", ");
        }


        if (minutes == 0) {
            remainingTime.append("0 minutes");
        } else {
            remainingTime.append(minutes + " minute" + (minutes > 1 ? "s" : "")); // Pluralize "minute"
        }

        return remainingTime.toString();
    }

    public Event updateEvent(Long id, Map<String, Object> updates) {
        Event eventToUpdate = eventRepository.findById(id)
                .orElseThrow();

        updates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    eventToUpdate.setTitle((String) value);
                    break;
                case "description":
                    eventToUpdate.setDescription((String) value);
                    break;
                case "start_date":
                    eventToUpdate.setStart_date(parseDateTime((String) value));
                    break;
                case "end_date":
                    eventToUpdate.setEnd_date(parseDateTime((String) value));
                    break;
                // Puedes añadir más casos según sea necesario
            }
        });

        return eventRepository.save(eventToUpdate);
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + dateTimeString);
        }
    }

    public List<String> getAllEventDuration(Iterable<Event> events) {
        List<String> durations = new ArrayList<>();

        for (Event event : events) {
            LocalDateTime endDate = event.getEnd_date();
            if (endDate != null) {
                String duration = schedule(endDate);
                durations.add(event.getTitle() + ": " + duration);
            } else {
                durations.add(event.getTitle() + ": No end date specified");
            }
        }

        return durations;
    }


}




