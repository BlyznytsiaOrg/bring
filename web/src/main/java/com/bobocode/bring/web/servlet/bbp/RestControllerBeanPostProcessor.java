package com.bobocode.bring.web.servlet.bbp;

import com.bobocode.bring.core.annotation.BeanProcessor;
import com.bobocode.bring.web.servlet.annotation.RestController;
import com.bobocode.bring.core.bpp.BeanPostProcessor;
import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.exception.MissingBringServletImplException;

/**
 * A {@code BeanPostProcessor} implementation that processes bean initialization
 * annotated with {@code @RestController}.
 * <p>
 * This post-processor checks if the bean is annotated with {@code @RestController}
 * and if it implements the {@code BringServlet} interface. If not, it throws a
 * {@code MissingBringServletImplException}.
 * </p>
 *
 * @see RestController
 * @see BringServlet
 * @see MissingBringServletImplException
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */

@BeanProcessor
public class RestControllerBeanPostProcessor implements BeanPostProcessor {



    /**
     * Processes bean initialization annotated with {@code @RestController}.
     *
     * @param bean     the bean instance
     * @param beanName the name of the bean
     * @return the processed bean
     * @throws MissingBringServletImplException if the bean is annotated with
     * {@code @RestController} but does not implement the {@code BringServlet} interface
     */


    @Override
    public Object postProcessInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(RestController.class)
                && (!BringServlet.class.isAssignableFrom(beanClass))) {
                throw new MissingBringServletImplException(
                        String.format("RestController '%s' should implement "
                                + "BringServlet interface", beanName));

        }
        return BeanPostProcessor.super.postProcessInitialization(bean, beanName);
    }
}
