package adeo.leroymerlin.cdp.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Test {@link EventUtils}
 */
@SpringBootTest(classes = EventUtilsTest.class)
 class EventUtilsTest {

    /**
     * Test concatStringToNumber
     */
    @Test
    void testConcatStringToNumberWithStringAndNumber_shouldReturnOneString() {
        assertThat(EventUtils.concatStringToNumber("Download Festival", 4))
                .isEqualTo("Download Festival [4]");
    }
}
