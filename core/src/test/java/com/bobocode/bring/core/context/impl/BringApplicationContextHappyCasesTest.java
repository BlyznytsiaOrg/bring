package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.testdata.di.positive.configuration.client.RestClient;
import com.bobocode.bring.testdata.di.positive.configuration.configuration.TestConfiguration;
import com.bobocode.bring.testdata.di.positive.configuration.service.BringService;
import com.bobocode.bring.testdata.di.positive.constructor.BringBeansService;
import com.bobocode.bring.testdata.di.positive.contract.Barista;
import com.bobocode.bring.testdata.di.positive.fullinjection.GetInfoFromExternalServicesUseCase;
import com.bobocode.bring.testdata.di.positive.setter.A;
import com.bobocode.bring.testdata.di.positive.setter.B;
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

    @DisplayName("Should register all beans from configuration class in Bring Context")
    @Test
    void shouldRegisterConfigurationBeans() {
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

    @DisplayName("Should register @Component and @Service beans in Bring Context")
    @Test
    void shouldCreateComponentBeans() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(BringBeansService.class);
        
        // when
        BringBeansService bringBeansService = bringApplicationContext.getBean(BringBeansService.class);

        // then
        assertThat(bringBeansService).isNotNull();
    }

    @DisplayName("Should inject beans annotated with different annotations")
    @Test
    void shouldCreateAndInjectDifferentBeans() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".fullinjection");;

        // when
        var useCase = bringApplicationContext.getBean(GetInfoFromExternalServicesUseCase.class);

        // then
        assertThat(useCase).isNotNull();
        assertThat(useCase.getInfoFromService())
                .hasSize(4)
                .contains("Some info")
                .contains("Other info")
                .contains("Some info2")
                .contains("Other info2");
        assertThat(useCase).hasToString("GetInfoFromExternalService2UseCase{" +
                "externalService=ExternalService1{" +
                "restClient=RestClient{url='https://exterl.service', username='user100'}}, " +
                "externalService2=ExternalService2{" +
                "restClient2=RestClient{url='https://exterl.service2', username='user200'}}}");
    }

}