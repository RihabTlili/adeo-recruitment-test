package adeo.leroymerlin.cdp.controller;

import adeo.leroymerlin.cdp.model.Event;
import adeo.leroymerlin.cdp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Event Controller
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    /**
     * Constructor.
     *
     * @param eventService eventService
     */
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Retrieve all events found in the database.
     *
     * @return Event list
     */
    @GetMapping("/")
    public List<Event> findEvents() {
        return eventService.getEvents();
    }

    /**
     * Search an event from a given text,
     * the search is based on the member names that contains the input text.
     *
     * @param query the query to filter the events.
     * @return the list of events that matched the query.
     */
    @GetMapping("/search/{query}")
    public List<Event> findEvents(@PathVariable String query) {
        return eventService.getFilteredEvents(query);
    }

    /**
     * Delete an event from the Id.
     *
     * @param id the Id of the event to be deleted.
     */
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
    }

    /**
     * Update an event by providing the Id and the event with the new data.
     *
     * @param id the Id of the event to be updated.
     * @param event the event with the new values.
     */
    @PutMapping(value = "/{id}")
    public void updateEvent(@PathVariable Long id, @RequestBody Event event) {
        eventService.updateEvent(id, event);
    }
}
