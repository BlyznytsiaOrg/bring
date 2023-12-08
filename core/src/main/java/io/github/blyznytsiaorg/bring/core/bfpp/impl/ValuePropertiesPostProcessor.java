package io.github.blyznytsiaorg.bring.core.bfpp.impl;

import io.github.blyznytsiaorg.bring.core.annotation.BeanProcessor;
import io.github.blyznytsiaorg.bring.core.annotation.Order;
import io.github.blyznytsiaorg.bring.core.context.impl.DefaultBringBeanFactory;
import io.github.blyznytsiaorg.bring.core.env.impl.ProfileSourceResolve;
import io.github.blyznytsiaorg.bring.core.bfpp.BeanFactoryPostProcessor;

import java.util.Map;


/**
 * This post processor populates properties for the DefaultBringBeanFactory using ProfileSourceResolve.
 * It sets properties for the factory based on profiles found in bean definitions.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */

@Order(Integer.MIN_VALUE)
@BeanProcessor
public class ValuePropertiesPostProcessor implements BeanFactoryPostProcessor {

    public static final String PROPERTIES = ".properties";
    public static final String ACTIVE_PROFILE_KEY = "bring.profiles.active";
    private final ProfileSourceResolve profileSourceResolve = new ProfileSourceResolve();

    /**
     * Processes the DefaultBringBeanFactory to populate properties based on profiles in bean definitions.
     *
     * @param defaultBeanFactory The DefaultBringBeanFactory to be processed.
     */
    @Override
    public void postProcessBeanFactory(DefaultBringBeanFactory defaultBeanFactory) {
        String profileName = System.getProperty(ACTIVE_PROFILE_KEY);
        Map<String, String> properties = defaultBeanFactory.getProperties();

        if (!properties.isEmpty()) {
            return;
        }

        properties = profileSourceResolve.resolve(profileName, PROPERTIES);
        defaultBeanFactory.setProperties(properties);
        defaultBeanFactory.setProfileName(profileName);
    }

}
