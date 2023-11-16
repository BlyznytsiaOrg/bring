package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.testdata.di.positive.configuration.client.RestClient;
import com.bobocode.bring.testdata.di.positive.configuration.configuration.TestConfiguration;
import com.bobocode.bring.testdata.di.positive.configuration.service.BringService;
import com.bobocode.bring.testdata.di.positive.constructor.BringBeansService;
import com.bobocode.bring.testdata.di.positive.constructorproperties.ProfileBeanConstructor;
import com.bobocode.bring.testdata.di.positive.contract.Barista;
import com.bobocode.bring.testdata.di.positive.fieldproperties.ProfileBean;
import com.bobocode.bring.testdata.di.positive.setter.A;
import com.bobocode.bring.testdata.di.positive.setter.B;
import com.bobocode.bring.testdata.di.positive.setterproperties.ProfileBeanSetter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BringApplicationContextHappyCasesTest {

    private static final String TEST_DATA_PACKAGE = "com.bobocode.bring.testdata.di.positive";

    @DisplayName("Should found one interface and inject it")
    @Test
    void shouldFoundOneInterfaceAndInjectIt() {
        //given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".contract");
        String expectedMessage = "Barista is preparing a drink: Making a delicious latte!";

        //when
        var barista = bringApplicationContext.getBean(Barista.class);

        //then
        assertThat(barista.prepareDrink()).isEqualTo(expectedMessage);
    }

    @DisplayName("Should inject dependency when you forget to add autowired and you have one constructor")
    @Test
    void shouldInjectConstructorDependencyIfYouHaveOneAndForgetToAddAutowired() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE +".constructor");

        // when
        var aBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.constructor.A.class);
        var bBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.constructor.B.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNotNull();
        assertThat(bBean.getA()).isEqualTo(aBean);
    }

    @DisplayName("Should inject dependency when you have multiple constructors but one of it was market as autowired")
    @Test
    void shouldInjectDependencyWhenYouHaveMultipleConstructorButOneOfItWasMarketAsAutowired() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE +".secondconstructor");

        // when
        var aBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.secondconstructor.A.class);
        var bBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.secondconstructor.B.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNotNull();
        assertThat(bBean.getA()).isEqualTo(aBean);
    }

    @DisplayName("Should inject dependency via Field")
    @Test
    void shouldInjectDependencyViaField() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE +".field");

        // when
        var aBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.field.A.class);
        var bBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.field.B.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNotNull();
        assertThat(bBean.getA()).isEqualTo(aBean);
    }

    @DisplayName("Should inject dependency via setter")
    @Test
    void shouldInjectDependencyViaSetter() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".setter");

        // when
        var aBean = bringApplicationContext.getBean(A.class);
        var bBean = bringApplicationContext.getBean(B.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNotNull();
        assertThat(bBean.getA()).isEqualTo(aBean);
    }

    @DisplayName("Should inject interface bean implementation taking into account constructor parameter name")
    @Test
    void shouldInjectInterfaceBeanImplementation() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".contract2");
        
        // when
        var bean = bringApplicationContext
                .getBean(com.bobocode.bring.testdata.di.positive.contract2.Barista.class);
        
        // then
        assertThat(bean).isNotNull();
        assertThat(bean.getDrink().make()).isEqualTo("Making a delicious latte!");
    }

    @DisplayName("All beans from configuration class registered in Bring Context")
    @Test
    void testConfigurationBeansRegistration() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(TestConfiguration.class);

        // when
        RestClient restClient = bringApplicationContext.getBean(RestClient.class);
        Map<String, BringService> bringServices = bringApplicationContext.getBeans(BringService.class);

        // then
        assertThat(restClient).isNotNull();
        assertThat(bringServices).hasSize(2);

        List<RestClient> restClients = bringServices.values().stream().map(BringService::getBringRestClient).toList();
        assertThat(restClients.stream().allMatch(rc -> rc.equals(restClient))).isTrue();
    }

    @DisplayName("@Component and @Service beans registered in Bring Context")
    @Test
    void createComponentBeans() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(BringBeansService.class);
        
        // when
        BringBeansService bringBeansService = bringApplicationContext.getBean(BringBeansService.class);

        // then
        assertThat(bringBeansService).isNotNull();
    }


    @DisplayName("Should found profile bean and read application properties and do field injection")
    @Test
    void shouldFoundProfileBeanAndReadApplicationPropertiesAndSetValueToFieldInjection() {
        //when
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".fieldproperties");
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
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".setterproperties");
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
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".constructorproperties");
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

}