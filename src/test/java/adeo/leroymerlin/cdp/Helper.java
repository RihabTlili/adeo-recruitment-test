package adeo.leroymerlin.cdp;

import adeo.leroymerlin.cdp.model.Band;
import adeo.leroymerlin.cdp.model.Event;
import adeo.leroymerlin.cdp.model.Member;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Utility class for testing the service and the controller.
 */
public final class Helper {

    public static Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        return member;
    }

    public static Band createBand(String name, String... memberNames) {
        Band band = new Band();
        band.setMembers(Arrays.stream(memberNames).map(Helper::createMember).collect(Collectors.toSet()));
        band.setName(name);
        return band;
    }

    public static Event createEvent(String title, Band... bands) {
        Event event = new Event();
        event.setBands(new HashSet<>(Arrays.asList(bands)));
        event.setTitle(title);
        return event;
    }
}
