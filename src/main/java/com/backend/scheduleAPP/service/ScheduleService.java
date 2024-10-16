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

    public Duration schedule(LocalDateTime givenDateTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(currentTime, givenDateTime);

        return duration;
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

    public List<Duration> getAllEventDuration(Iterable<Event> events) {
        List<Duration> durations = new ArrayList<>();

        for (Event event : events) {
            LocalDateTime endDate = event.getEnd_date();
            Duration duration = schedule(endDate);
            durations.add(duration);  // Añadimos esta línea
        }

        return durations;
    }


}




