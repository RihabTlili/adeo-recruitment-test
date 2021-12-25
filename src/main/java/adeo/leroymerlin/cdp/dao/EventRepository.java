package adeo.leroymerlin.cdp.dao;

import adeo.leroymerlin.cdp.model.Event;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Event Repository for {@link Event}
 */
public interface EventRepository extends Repository<Event, Long> {

    /**
     * Delete an Event by Id.
     *
     * @param eventId the event Id
     */
    void deleteById(Long eventId);

    /**
     * Find all events.
     *
     * @return Event list
     */
    List<Event> findAllBy();

    /**
     * Find an Event by Id.
     *
     * @param eventId the event Id.
     * @return
     */
    Optional<Event> findById(Long eventId);

    /**
     * Save an Event.
     *
     * @param event The event to be saved.
     */
    void save(Event event);
}
