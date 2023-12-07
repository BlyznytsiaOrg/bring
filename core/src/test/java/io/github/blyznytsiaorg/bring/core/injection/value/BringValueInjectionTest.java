package io.github.blyznytsiaorg.bring.core.injection.value;

import io.github.blyznytsiaorg.bring.core.BringApplication;
import io.github.blyznytsiaorg.bring.core.bfpp.impl.ValuePropertiesPostProcessor;
import io.github.blyznytsiaorg.bring.core.context.impl.BringApplicationContext;
import io.github.blyznytsiaorg.bring.core.exception.PropertyValueNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.di.negative.properties.defaultvalue.DefaultValueFromField;
import testdata.di.positive.constructorproperties.ProfileBeanConstructor;
import testdata.di.positive.fieldproperties.ProfileBean;
import testdata.di.positive.setterproperties.ProfileBeanSetter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BringValueInjectionTest {

    private static final String TEST_DATA_POSITIVE_PACKAGE = "testdata.di.positive";
    private static final String TEST_DATA_NEGATIVE_PACKAGE = "testdata.di.negative";

    @DisplayName("Should set profile and read application properties and do field injection")
    @Test
    void shouldFoundProfileBeanAndReadApplicationPropertiesAndSetValueToFieldInjection() {
        //when
        System.setProperty(ValuePropertiesPostProcessor.ACTIVE_PROFILE_KEY, "dev");
        var bringApplicationContext = BringApplication.run(TEST_DATA_POSITIVE_PACKAGE + ".fieldproperties");
        ProfileBean profileBean = bringApplicationContext.getBean(ProfileBean.class);

        //then
        org.assertj.core.api.Assertions.assertThat(bringApplicationContext.getProfileName()).isEqualTo("dev");
        org.assertj.core.api.Assertions.assertThat(bringApplicationContext.getProperties()).isNotNull()
                .hasSize(1)
                .containsEntry("bring.main.banner-mode", "on");

        assertThat(profileBean).isNotNull();
        assertThat(profileBean.getBannerMode())
                .isNotNull()
                .isEqualTo("on");
    }

    @DisplayName("Should set profile and read application properties and do setter injection")
    @Test
    void shouldFoundProfileBeanAndReadApplicationPropertiesAndSetValueToSetterInjection() {
        //when
        System.setProperty(ValuePropertiesPostProcessor.ACTIVE_PROFILE_KEY, "dev");
        var bringApplicationContext = BringApplication.run(TEST_DATA_POSITIVE_PACKAGE + ".setterproperties");
        ProfileBeanSetter profileBeanSetter = bringApplicationContext.getBean(ProfileBeanSetter.class);

        //then
        org.assertj.core.api.Assertions.assertThat(bringApplicationContext.getProfileName()).isEqualTo("dev");
        org.assertj.core.api.Assertions.assertThat(bringApplicationContext.getProperties()).isNotNull()
                .hasSize(1)
                .containsEntry("bring.main.banner-mode", "on");

        assertThat(profileBeanSetter).isNotNull();
        assertThat(profileBeanSetter.getBannerMode())
                .isNotNull()
                .isEqualTo("on");
    }

    @DisplayName("Should set profile and read application properties and do constructor injection")
    @Test
    void shouldFoundProfileBeanAndReadApplicationPropertiesAndSetValueToConstructorInjection() {
        //when
        System.setProperty(ValuePropertiesPostProcessor.ACTIVE_PROFILE_KEY, "dev");
        var bringApplicationContext = BringApplication.run(TEST_DATA_POSITIVE_PACKAGE + ".constructorproperties");
        ProfileBeanConstructor profileBeanConstructor = bringApplicationContext.getBean(ProfileBeanConstructor.class);

        //then
        org.assertj.core.api.Assertions.assertThat(bringApplicationContext.getProfileName()).isEqualTo("dev");
        org.assertj.core.api.Assertions.assertThat(bringApplicationContext.getProperties()).isNotNull()
                .hasSize(1)
                .containsEntry("bring.main.banner-mode", "on");

        assertThat(profileBeanConstructor).isNotNull();
        assertThat(profileBeanConstructor.getBannerMode())
                .isNotNull()
                .isEqualTo("on");
    }


    @DisplayName("Should found profile bean and read application properties and do field injection but value not found")
    @Test
    void shouldFoundProfileBeanAndReadApplicationPropertiesAndSetNullValueToFieldInjectionIfNotFound() {
        PropertyValueNotFoundException exception = assertThrows(PropertyValueNotFoundException.class,
                () -> BringApplication.run(TEST_DATA_NEGATIVE_PACKAGE + ".properties.field"));

        Assertions.assertEquals(
                "Property value not found for the specified key: bring.banner-mode.field, field: banner.",
                exception.getMessage());
    }

    @DisplayName("Should found profile bean and read application properties and do constructor field injection but value not found")
    @Test
    void shouldFoundProfileBeanAndReadApplicationPropertiesAndSetNullValueToConstructorInjectionIfNotFound() {
        PropertyValueNotFoundException exception = assertThrows(PropertyValueNotFoundException.class,
                () -> BringApplication.run(TEST_DATA_NEGATIVE_PACKAGE + ".properties.constructor"));

        Assertions.assertEquals(
                "Property value not found for the specified key: bring.banner-mode.constructor, field: banner.",
                exception.getMessage());
    }

    @DisplayName("Should read default value and set to field if not found in application properties")
    @Test
    void ShouldReadDefaultValueAndSetToFieldIfNotFoundInApplicationProperties() {
        BringApplicationContext context = BringApplication.run(TEST_DATA_NEGATIVE_PACKAGE + ".properties.defaultvalue");
        DefaultValueFromField defaultValue = context.getBean(DefaultValueFromField.class);

        Assertions.assertEquals("defaultValue", defaultValue.getBanner());
    }
}
