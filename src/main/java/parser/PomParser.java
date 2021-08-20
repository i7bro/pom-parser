package parser;

import exception.ReadPomException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@UtilityClass
public final class PomParser {

    private static final Logger log = LoggerFactory.getLogger(PomParser.class);
    private static final MavenXpp3Reader reader = new MavenXpp3Reader();

    @NonNull
    public static List<String> findPlugins(@NonNull String path) {
        try {
            var pomFile = new File(path);
            var parentModel = reader.read(new FileReader(pomFile));

            if (parentModel.getModules().size() == 0) {
                return getPluginsStreamOfParentPom(parentModel)
                        .collect(toList());
            } else {
                return List.of(
                            getPluginsStreamOfParentPom(parentModel),
                            getPluginsStreamOfChildPoms(parentModel.getModules(), pomFile.getParent()))
                        .stream()
                        .flatMap(el -> el)
                        .distinct()
                        .collect(toList());
            }
        } catch (Exception e) {
            log.error("Read file error {}", e.getMessage());
            throw new ReadPomException(e);
        }
    }

    private static Stream<String> getPluginsStreamOfParentPom(Model model) {
        return model.getBuild().getPlugins().stream()
                .map(plugin -> String.format("%s:%s", plugin.getGroupId(), plugin.getArtifactId()))
                .distinct();
    }

    private static Stream<String> getPluginsStreamOfChildPoms(List<String> modules, String parentPath) {
        return modules.stream()
                .flatMap(module -> findPlugins(String.join("/", parentPath, module, "pom.xml")).stream());
    }
}
