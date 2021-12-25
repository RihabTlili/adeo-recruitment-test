package adeo.leroymerlin.cdp.service;

import adeo.leroymerlin.cdp.model.Event;
import adeo.leroymerlin.cdp.dao.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static adeo.leroymerlin.cdp.utils.EventUtils.concatStringToNumber;

/**
 * Services related to {@link Event}
 */
@Service
public class EventService {

    private final EventRepository eventRepository;

    /**
     * Constructor
     *
     * @param eventRepository eventRepository
     */
    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Retrieve all events found in the database
     *
     * @return List of events
     */
    @Transactional(readOnly = true)
    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    /**
     * Delete an Event from the input Id.
     *
     * @param id the Id of event to be deleted.
     */
    @Transactional
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    /**
     * Update the comment and the star value of the given Event.
     *
     * @param id the id of the event.
     * @param event the event object.
     */
    @Transactional
    public void updateEvent(Long id, Event event) {
        var eventToUpdateOptional = eventRepository.findById(id);

        if (eventToUpdateOptional.isPresent()) {
            var eventToUpdate = eventToUpdateOptional.get();
            eventToUpdate.setNbStars(event.getNbStars());
            eventToUpdate.setComment(event.getComment());
            eventRepository.save(eventToUpdate);
        } else {
            throw new IllegalArgumentException(String.format("The event %d to be updated can not be found.", id));
        }
    }

    /**
     * Filter the list of events by the provided query.
     * The events returned are only if at least one band has
     * a member with the name matching the given query.
     *
     * @param query the query to filter the events.
     * @return the list of events that matched the query.
     */
    @Transactional(readOnly = true)
    public List<Event> getFilteredEvents(String query) {
        //filtering the event list
        var events = eventRepository.findAllBy().stream()
                .filter(event -> event.getBands()
                        .stream()
                        .flatMap(band -> band.getMembers().stream())
                        .anyMatch(member -> member.getName().contains(query)))
                .collect(Collectors.toUnmodifiableList());

        //Add to the event list (after filtering) the child count
        addCountChildToEvents(events);

        return events;
    }

    /**
     * Enrich the events by adding a count (the number of child items)
     * in the title of the event and the name of each band.
     *
     * @param events the list of event to enrich.
     */
    public void addCountChildToEvents(List<Event> events) {
        events.forEach(event -> {
            //Add count for event title
            event.setTitle(concatStringToNumber(event.getTitle(),
                    event.getBands().size()));

            //Add count for band name
            event.getBands().forEach(band ->
                    band.setName(concatStringToNumber(band.getName(),
                            band.getMembers().size())));
        });
    }
}
