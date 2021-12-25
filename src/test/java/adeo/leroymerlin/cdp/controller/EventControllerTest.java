package adeo.leroymerlin.cdp.controller;

import adeo.leroymerlin.cdp.dao.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static adeo.leroymerlin.cdp.Helper.createBand;
import static adeo.leroymerlin.cdp.Helper.createEvent;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Tests for the {@link EventController}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc eventMockMvc;

    @MockBean
    private EventRepository eventRepository;

    private static final String ENTITY_API_URL = "/api/events";

    /**
     * Test findEvents
     *
     * @throws Exception
     */
    @Test
    void testFindEvents() throws Exception {
        final var eventGrasPop = createEvent("GrasPop Metal Meeting",
                createBand("The Ramones", "Queen Ava Dunlap", "Queen Haleema Poole"));

        when(eventRepository.findAllBy()).thenReturn(Collections.singletonList(eventGrasPop));

        eventMockMvc
                .perform(get(ENTITY_API_URL + "/").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("GrasPop Metal Meeting"));
    }

    /**
     * Test deleteEvent
     *
     * @throws Exception
     */
    @Test
    void testDeleteEvent() throws Exception {
        eventMockMvc.perform(delete(ENTITY_API_URL + "/1000").accept(MediaType.APPLICATION_JSON));
        verify(eventRepository).deleteById(1000L);
    }

    /**
     * Test updateEvent
     *
     * @throws Exception
     */
    @Test
    void testUpdateEvent() throws Exception {
        final var eventMotocultor = createEvent("Motocultor",
                createBand("Sum41", "Queen Jamie Petty", "Queen Charlie Wolf (Chick)"));
        eventMotocultor.setId(1000L);

        when(eventRepository.findById(1004L)).thenReturn(Optional.of(eventMotocultor));
        eventMockMvc.perform(put(ENTITY_API_URL + "/{id}", 1004)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"id\":1004,\"title\":\"Motocultor\",\"imgUrl\":null,\"bands\":" +
                        "[{\"name\":\"Sum41\",\"members\":[{\"name\":\"Queen Jamie Petty\"}," +
                        "{\"name\":\"Queen Charlie Wolf (Chick)\"}," +
                        "{\"name\":\"Queen Betty Thomas (Ilsa)\"}," +
                        "{\"name\":\"Queen Danielle Connor (Dannon)\"}]}]," +
                        "\"nbStars\":null,\"comment\":null}")
                .accept(MediaType.APPLICATION_JSON_VALUE));

        verify(eventRepository).findById(1004L);
        verify(eventRepository).save(eventMotocultor);
    }

    /**
     * Test findEvents with filter
     *
     * @throws Exception
     */
    @Test
    void testFindEventsWithFilter() throws Exception {
        final var eventGrasPop = createEvent("GrasPop Metal Meeting",
                createBand("The Ramones", "Queen Ava Dunlap", "Queen Haleema Poole"));
        final var eventMotocultor = createEvent("Motocultor",
                createBand("Sum41", "Queen Jamie Petty", "Queen Charlie Wolf (Chick)"));

        when(eventRepository.findAllBy()).thenReturn(Arrays.asList(eventMotocultor, eventGrasPop));

        eventMockMvc
                .perform(get(ENTITY_API_URL + "/search/{query}", "oole").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("GrasPop Metal Meeting [1]"))
                .andExpect(jsonPath("$[0].bands[0].name").value("The Ramones [2]"));
    }
}
