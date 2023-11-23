package com.bobocode.bring.core.injection.value;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.testdata.di.negative.fieldproperties.NotFoundValue;
import com.bobocode.bring.testdata.di.positive.constructorproperties.ProfileBeanConstructor;
import com.bobocode.bring.testdata.di.positive.fieldproperties.ProfileBean;
import com.bobocode.bring.testdata.di.positive.setterproperties.ProfileBeanSetter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BringValueInjectionTest {

    private static final String TEST_DATA_POSITIVE_PACKAGE = "com.bobocode.bring.testdata.di.positive";
    private static final String TEST_DATA_NEGATIVE_PACKAGE = "com.bobocode.bring.testdata.di.negative";

    @DisplayName("Should found profile bean and read application properties and do field injection")
    @Test
    void shouldFoundProfileBeanAndReadApplicationPropertiesAndSetValueToFieldInjection() {
        //when
        var bringApplicationContext = BringApplication.run(TEST_DATA_POSITIVE_PACKAGE + ".fieldproperties");
        ProfileBean profileBean = bringApplicationContext.getBean(ProfileBean.class);

        //then
        assertThat(bringApplicationContext.getProfileName()).isEqualTo("dev");
        assertThat(bringApplicationContext.getProperties()).isNotNull()
                .hasSize(1)
                .containsEntry("bring.main.banner-mode", "on");

        assertThat(profileBean).isNotNull();
        assertThat(profileBean.getBannerMode())
                .isNotNull()
                .isEqualTo("on");
    }

    @DisplayName("Should found profile bean and read application properties and do setter injection")
    @Test
    void shouldFoundProfileBeanAndReadApplicationPropertiesAndSetValueToSetterInjection() {
        //when
        var bringApplicationContext = BringApplication.run(TEST_DATA_POSITIVE_PACKAGE + ".setterproperties");
        ProfileBeanSetter profileBeanSetter = bringApplicationContext.getBean(ProfileBeanSetter.class);

        //then
        assertThat(bringApplicationContext.getProfileName()).isEqualTo("dev");
        assertThat(bringApplicationContext.getProperties()).isNotNull()
                .hasSize(1)
                .containsEntry("bring.main.banner-mode", "on");

        assertThat(profileBeanSetter).isNotNull();
        assertThat(profileBeanSetter.getBannerMode())
                .isNotNull()
                .isEqualTo("on");
    }

    @DisplayName("Should found profile bean and read application properties and do constructor injection")
    @Test
    void shouldFoundProfileBeanAndReadApplicationPropertiesAndSetValueToConstructorInjection() {
        //when
        var bringApplicationContext = BringApplication.run(TEST_DATA_POSITIVE_PACKAGE + ".constructorproperties");
        ProfileBeanConstructor profileBeanConstructor = bringApplicationContext.getBean(ProfileBeanConstructor.class);

        //then
        assertThat(bringApplicationContext.getProfileName()).isEqualTo("dev");
        assertThat(bringApplicationContext.getProperties()).isNotNull()
                .hasSize(1)
                .containsEntry("bring.main.banner-mode", "on");

        assertThat(profileBeanConstructor).isNotNull();
        assertThat(profileBeanConstructor.getBannerMode())
                .isNotNull()
                .isEqualTo("on");
    }


    @Disabled("Need to throw exception when we cannot find value and do the same for constructor, setter and field injection")
    @DisplayName("Should found profile bean and read application properties and do field injection but value not found")
    @Test
    void shouldFoundProfileBeanAndReadApplicationPropertiesAndSetNullValueToFieldInjectionIfNotFound() {
        //when
        var bringApplicationContext = BringApplication.run(TEST_DATA_NEGATIVE_PACKAGE + ".fieldproperties");
        NotFoundValue notFoundValue = bringApplicationContext.getBean(NotFoundValue.class);
    }
}
