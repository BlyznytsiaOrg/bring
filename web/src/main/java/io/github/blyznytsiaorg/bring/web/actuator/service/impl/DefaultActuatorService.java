package io.github.blyznytsiaorg.bring.web.actuator.service.impl;

import io.github.blyznytsiaorg.bring.core.annotation.PostConstruct;
import io.github.blyznytsiaorg.bring.core.annotation.Service;
import io.github.blyznytsiaorg.bring.web.actuator.dto.Commit;
import io.github.blyznytsiaorg.bring.web.actuator.dto.GitInfo;
import io.github.blyznytsiaorg.bring.web.actuator.dto.PropertiesData;
import io.github.blyznytsiaorg.bring.web.actuator.service.ActuatorService;
import io.github.blyznytsiaorg.bring.web.server.properties.ServerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Default implementation of the {@link ActuatorService} interface.
 * Provides methods for preparing Git information and system properties data for the Actuator endpoint.
 * <p>
 * This class uses the {@code @Service} annotation to indicate that it is a Bring component and should be
 * automatically detected and registered during component scanning.
 * <p>
 * The Git information is loaded from the "git.properties" file, and the system properties data is obtained
 * using {@code System.getProperties()}.
 * <p>
 * The {@code @PostConstruct} method {@code prepareGitInfo()} is annotated to be executed after the
 * bean is constructed. It loads the Git information during the initialization phase.
 * <p>
 * The class uses constants for error messages, Git file name, and keys of properties of Git.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultActuatorService implements ActuatorService {

    // Constant for error messages
    private static final String GIT_PROPERTIES_FILE_NOT_FOUND_MESSAGE = "File '%s' not found";

    // The name of the git file
    private static final String GIT_FILE_NAME = "git.properties";

    // Keys of properties of git
    private static final String GIT_BRANCH_PROPERTY = "git.branch";
    private static final String GIT_COMMIT_ID_PROPERTY = "git.commit.id";
    private static final String GIT_COMMIT_MESSAGE_FULL_PROPERTY = "git.commit.message.full";
    private static final String GIT_COMMIT_TIME_PROPERTY = "git.commit.time";

    // Static field to store Git information
    private static GitInfo gitInfo;

    // ServerProperties for configuration
    private final ServerProperties serverProperties;

    /**
     * Returns the Git information for the Actuator endpoint.
     *
     * @return Git information including branch details and the latest commit information.
     */
    @Override
    public GitInfo getGitInfo() {
        return gitInfo;
    }

    /**
     * Prepares system properties data for the Actuator endpoint.
     *
     * @return Properties data containing all system properties.
     */
    @Override
    public PropertiesData preparePropertyData() {
        return PropertiesData.builder()
                .properties((Map) System.getProperties())
                .build();
    }

    /**
     * Initializes the Git information during the bean's post-construction phase.
     * This method is annotated with {@code @PostConstruct} and is executed after the bean is constructed.
     */
    @PostConstruct
    public void prepareGitInfo() {
        try (InputStream source = DefaultActuatorService.class.getClassLoader().getResourceAsStream(GIT_FILE_NAME)) {
            Objects.requireNonNull(source, String.format(GIT_PROPERTIES_FILE_NOT_FOUND_MESSAGE, GIT_FILE_NAME));
            Properties properties = new Properties();
            properties.load(source);

            gitInfo = prepareGitInfo(properties, prepareCommitInfo(properties));
        } catch (Exception e) {
            log.warn(String.format(GIT_PROPERTIES_FILE_NOT_FOUND_MESSAGE, GIT_FILE_NAME));
        }
    }

    /**
     * Prepares Git information based on the provided properties and commit details.
     *
     * @param properties The properties containing Git information.
     * @param commit     The details of the latest commit.
     * @return Git information including branch details and the latest commit information.
     */
    private GitInfo prepareGitInfo(Properties properties, Commit commit) {
        return GitInfo.builder()
                .branch(properties.getProperty(GIT_BRANCH_PROPERTY))
                .commit(commit)
                .build();
    }

    /**
     * Prepares commit information based on the provided properties.
     *
     * @param properties The properties containing commit details.
     * @return The details of the latest commit, including ID, message, and timestamp.
     */
    private Commit prepareCommitInfo(Properties properties) {
        return Commit.builder()
                .id(properties.getProperty(GIT_COMMIT_ID_PROPERTY))
                .message(serverProperties.isWithMessage() ? properties.getProperty(GIT_COMMIT_MESSAGE_FULL_PROPERTY) : null)
                .time(properties.getProperty(GIT_COMMIT_TIME_PROPERTY))
                .build();
    }
}
