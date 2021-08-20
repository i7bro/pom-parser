package parser;

import exception.ReadPomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class PomParserTest {

    private static final String PATH_POM_WITHOUT_MODULES = "src/test/resources/module/pom.xml";
    private static final String PATH_POM_INCLUDE_MODULES = "src/test/resources/parent-module/pom.xml";
    private static final String PATH_WRONG_FILE = "src/test/resources/not-xml.txt";

    @Test
    @DisplayName("test pom.xml without modules")
    public void testPars() {
        List<String> pars = PomParser.findPlugins(PATH_POM_WITHOUT_MODULES);

        assertEquals(2, pars.size());
    }

    @Test
    @DisplayName("test pom.xml include modules")
    public void testPars1() {
        List<String> pars = PomParser.findPlugins(PATH_POM_INCLUDE_MODULES);

        assertEquals(6, pars.size());
    }

    @Test
    @DisplayName("test throw exception")
    public void testPars2() {
        assertThrows(ReadPomException.class, () -> PomParser.findPlugins(PATH_WRONG_FILE));
    }
}
