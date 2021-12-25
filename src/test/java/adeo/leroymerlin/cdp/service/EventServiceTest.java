package adeo.leroymerlin.cdp.service;

import adeo.leroymerlin.cdp.dao.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static adeo.leroymerlin.cdp.Helper.createBand;
import static adeo.leroymerlin.cdp.Helper.createEvent;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link EventService}
 */
@SpringBootTest(classes = EventService.class)
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @MockBean
    private EventRepository eventRepository;

    /**
     * Test getEvents
     */
    @Test
    void testGetEvents_shouldReturnAllEventsFound() {
        //GIVEN
        final var bandRamones = createBand("The Ramones", "Queen Ava Dunlap", "Queen Haleema Poole");
        final var bandSum41 = createBand("Sum41", "Queen Jamie Petty", "Queen Charlie Wolf");
        final var eventGrasPop = createEvent("GrasPop Metal Meeting", bandRamones);
        final var eventMotoCultor = createEvent("Motocultor", bandSum41);
        final var events = Arrays.asList(eventGrasPop, eventMotoCultor);

        when(eventRepository.findAllBy()).thenReturn(events);

        //WHEN
        final var expectedEvents = eventService.getEvents();

        //THEN
        assertThat(events)
                .isEqualTo(expectedEvents)
                .hasSize(2);
    }

    /**
     * test delete
     */
    @Test
    void testDeleteEventFromId_shouldDeleteTheEvent() {
        //GIVEN
        final var eventGrasPop = createEvent("GrasPop Metal Meeting",
                createBand("The Ramones", "Queen Ava Dunlap", "Queen Haleema Poole"));
        eventGrasPop.setId(2L);

        //WHEN
        eventService.delete(2L);

        //THEN
        verify(eventRepository).deleteById(2L);
    }

    /**
     * Test updateEvent
     */
    @Test
    void testUpdateEventWithExistingEvent_shouldUpdateTheEvent() {
        //GIVEN
        final var bandPinkFloyd = createBand("Pink Floyd", "Queen Frankie Gross (Fania)", "Queen Genevieve Clark");
        final var eventAlcatraz = createEvent("Alcatraz Fest", bandPinkFloyd);
        eventAlcatraz.setId(1L);

        given(eventRepository.findById(1L)).willReturn(Optional.of(eventAlcatraz));

        final var newEventAlcatraz = createEvent("Alcatraz Fest", bandPinkFloyd);
        newEventAlcatraz.setId(1L);
        newEventAlcatraz.setNbStars(3);
        newEventAlcatraz.setComment("new comment");

        //WHEN
        eventService.updateEvent(newEventAlcatraz.getId(), newEventAlcatraz);

        //THEN
        verify(eventRepository).save(eventAlcatraz);
        verify(eventRepository).findById(eventAlcatraz.getId());
    }

    /**
     * Test updateEvent when event not found
     */
    @Test
    void testUpdateEventWithNonExistingEvent_shouldThrowException() {
        //GIVEN
        final var bandPinkFloyd = createBand("Pink Floyd", "Queen Frankie Gross (Fania)", "Queen Genevieve Clark");
        final var eventAlcatraz = createEvent("Alcatraz Fest", bandPinkFloyd);
        eventAlcatraz.setId(1L);
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //WHEN - THEN
        assertThrows(IllegalArgumentException.class, () -> eventService.updateEvent(1L, eventAlcatraz));
    }

    /**
     * Test getFilteredEvents
     */
    @Test
    void testGetFilteredEventsWithStringExistInOneMemberName_shoulReturnOneEvent() {
        //GIVEN
        final var bandRamones = createBand("The Ramones", "Queen Ava Dunlap", "Queen Haleema Poole");
        final var bandSum41 = createBand("Sum41", "Queen Jamie Petty", "Queen Charlie Wolf");
        final var eventGrasPop = createEvent("GrasPop Metal Meeting", bandRamones);
        final var eventMotoCultor = createEvent("Motocultor", bandSum41);
        final var events = Arrays.asList(eventGrasPop, eventMotoCultor);
        when(eventRepository.findAllBy()).thenReturn(events);

        //WHEN
        final var filteredEvents = eventService.getFilteredEvents("olf");
        //the expected values
        bandSum41.setName("Sum41 [2]");
        eventMotoCultor.setBands(new HashSet<>(Collections.singletonList(bandSum41)));
        eventMotoCultor.setTitle("Motocultor [1]");

        //THEN
        assertThat(filteredEvents)
                .containsOnly(eventMotoCultor);

    }

    /**
     * Test addCountChildToEvents
     */
    @Test
    void testAddCountChildToEventsWithNonEmptyEventList_shouldAddCountsToEventList() {
        //GIVEN
        final var bandRamones = createBand("The Ramones", "Queen Ava Dunlap", "Queen Haleema Poole");
        final var eventGrasPop = createEvent("GrasPop Metal Meeting", bandRamones);

        //WHEN
        eventService.addCountChildToEvents(Collections.singletonList(eventGrasPop));

        //THEN
        assertThat(eventGrasPop.getTitle())
                .isEqualTo("GrasPop Metal Meeting [1]");
        assertThat(eventGrasPop.getBands().iterator().next().getName())
                .isEqualTo("The Ramones [2]");
    }
}
