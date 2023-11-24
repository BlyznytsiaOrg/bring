package com.bobocode.bring.core.postprocessor.impl;

import com.bobocode.bring.core.anotation.Profile;
import com.bobocode.bring.core.context.impl.DefaultBringBeanFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.env.impl.ProfileSourceResolve;
import com.bobocode.bring.core.postprocessor.BeanFactoryPostProcessor;

import java.util.Map;


/**
 * This post processor populates properties for the DefaultBringBeanFactory using ProfileSourceResolve.
 * It sets properties for the factory based on profiles found in bean definitions.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */

public class ValuePropertiesPostProcessor implements BeanFactoryPostProcessor {

    public static final String PROPERTIES = ".properties";
    private final ProfileSourceResolve profileSourceResolve = new ProfileSourceResolve();

    /**
     * Processes the DefaultBringBeanFactory to populate properties based on profiles in bean definitions.
     *
     * @param defaultBeanFactory The DefaultBringBeanFactory to be processed.
     */
    @Override
    public void postProcessBeanFactory(DefaultBringBeanFactory defaultBeanFactory) {
        String profileName = null;
        Map<String, String> properties = defaultBeanFactory.getProperties();

        if (!properties.isEmpty()) {
            return;
        }

        for (String beanName : defaultBeanFactory.getAllBeanDefinitionNames()) {
            BeanDefinition beanDefinition = defaultBeanFactory.getBeanDefinitionByName(beanName);

            if (beanDefinition.isProfile()) {
                Class<?> beanClass = beanDefinition.getBeanClass();
                if (beanClass.isAnnotationPresent(Profile.class)) {
                    profileName = beanClass.getAnnotation(Profile.class).value();
                }
            }
        }

        properties = profileSourceResolve.resolve(profileName, PROPERTIES);
        defaultBeanFactory.setProperties(properties);
        defaultBeanFactory.setProfileName(profileName);
    }

}
