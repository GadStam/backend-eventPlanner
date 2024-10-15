package com.backend.scheduleAPP.controller;

import com.backend.scheduleAPP.dto.DateDto;
import com.backend.scheduleAPP.model.Event;
import com.backend.scheduleAPP.repository.EventRepository;
import com.backend.scheduleAPP.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class ScheduleController {

    @Autowired
    private EventRepository eventRepository;

    private final ScheduleService scheduleService;


public ScheduleController(ScheduleService scheduleService) {
    this.scheduleService = scheduleService;
}

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @PostMapping("/schedule")
    public String schedule(@RequestBody DateDto dateDto) {
        LocalDateTime givenDateTime = dateDto.getDate();
        LocalDateTime now = LocalDateTime.now();

        System.out.println("Fecha recibida: " + givenDateTime);
        System.out.println("Fecha actual: " + now);
        String durationDate = scheduleService.schedule(givenDateTime);
        return durationDate;
    }

    @PostMapping("/createEvent")
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @DeleteMapping("/deleteEvent/{id}")
    public String  deleteEvent(@PathVariable Long id){
    Event event = eventRepository.findById(id).orElseThrow();
    eventRepository.delete(event);
        return "event deleted with id: "+id;
    }

    @GetMapping("/getEvent/{id}")
    public Event getEvent(@PathVariable Long id){
        return eventRepository.findById(id).orElseThrow();
    }

    @GetMapping("/getAllEvents")
    public Iterable<Event> getAllEvents(){
        return eventRepository.findAll();
    }

    @PutMapping("/updateEvent/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Map<String, Object> updates){
        Event eventToUpdate = scheduleService.updateEvent(id, updates);
        return eventRepository.save(eventToUpdate);
    }

    @GetMapping("/getEventDuration/{id}")
    public String getEventDuration(@PathVariable Long id){
        Event event = eventRepository.findById(id).orElseThrow();
        return scheduleService.schedule(event.getEnd_date());
    }

    @GetMapping("/getAllEventDuration")
    public List<String> getAllEventDuration(){
        Iterable<Event> events = eventRepository.findAll();
        return scheduleService.getAllEventDuration(events);
    }


}
