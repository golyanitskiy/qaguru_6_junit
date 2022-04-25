package guru.qa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ParametrisedTest {

    @BeforeAll
    static void setUp() {
        Configuration.headless = true;
    }

    @AfterAll
    static void tearDown() {
        Selenide.closeWebDriver();
    }

    @Disabled("Намеренно отключенный тест")
    @DisplayName("Предельно простой тест для демонстрации аннотации disabled")
    @Test
    void firstTest() {
        Assertions.assertTrue(true);
    }

    @ParameterizedTest(name = "Тест поиска в Википедии с ValueSource по запросу {0}")
    @ValueSource(strings = {
            "JUnit",
            "Allure",
            "Jenkins"
    })
    void secondTest(String query) {
        Selenide.open("https://ru.wikipedia.org/wiki/");
        $("#searchInput").setValue(query);
        $("#searchButton").click();

        $("#siteSub").shouldBe(visible);
        $("#firstHeading").shouldHave(text(query));
    }

    @CsvSource(value = {
            "perl | how to execute the Perl interpreter",
            "ls | list directory contents",
            "man | an interface to the on-line reference manuals"
    },
            delimiter = '|'
    )
    @ParameterizedTest(name = "Тест поиска с датапровайдером CsvSource по запросу {0}")
    void thirdTest(String query, String expectedResult ) {
        open("https://explainshell.com");
        $("#explain").setValue(query).pressEnter();

        $("#command").shouldBe(visible);
        $("#command").shouldHave(text(query));
        $("#help").shouldHave(text(expectedResult));
    }

    static Stream<Arguments> methodSourceExampleTest() {
        return Stream.of(
                Arguments.of("first string", List.of(42, 13)),
                Arguments.of("second string", List.of(1, 2))
        );
    }

    @MethodSource("methodSourceExampleTest")
    @ParameterizedTest
    void methodSourceExampleTest(String first, List<Integer> second) {
        System.out.println(first + " and list: " + second);
    }
}
