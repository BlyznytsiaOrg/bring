package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.BringApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.di.positive.configuration.client.RestClient;
import testdata.di.positive.configuration.configuration.TestConfiguration;
import testdata.di.positive.configuration.service.BringService;
import testdata.di.positive.constructor.BringBeansService;
import testdata.di.positive.fullinjection.GetInfoFromExternalServicesUseCase;
import testdata.di.positive.mixconfigurationandservice.BeanA;
import testdata.di.positive.mixconfigurationandservice.BeanB;
import testdata.di.positive.prototype.off.CoffeeShop;
import testdata.di.positive.prototype.off.SimpleClass;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class BringApplicationContextHappyCasesTest {

    private static final String TEST_DATA_PACKAGE = "testdata.di.positive";

    @DisplayName("Should inject interface bean implementation taking into account constructor parameter name")
    @Test
    void shouldMixBeansConfigurationAndService() {
        // given
        var bringApplicationContext = BringApplication.run(
            TEST_DATA_PACKAGE + ".mixconfigurationandservice");

        // when
        var beanB = bringApplicationContext.getBean(BeanB.class);
        var beanA = bringApplicationContext.getBean(BeanA.class);

        // then
        assertThat(beanB).isNotNull();
        assertThat(beanB.getBeanA()).isNotNull();
        assertThat(beanB.getBeanA()).isEqualTo(beanA);
    }

    @DisplayName("Should inject interface bean implementation taking into account constructor parameter name")
    @Test
    void shouldInjectInterfaceBeanImplementation() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".contract2");

        // when
        var bean = bringApplicationContext.getBean(testdata.di.positive.contract2.Barista.class);

        // then
        assertThat(bean).isNotNull();
        assertThat(bean.getDrink().make()).isEqualTo("Making a delicious latte!");
    }

    @DisplayName("Should register all beans from configuration class in Bring Context")
    @Test
    void shouldRegisterConfigurationBeans() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(
            TestConfiguration.class);

        // when
        RestClient restClient = bringApplicationContext.getBean(RestClient.class);
        Map<String, RestClient> restClients = bringApplicationContext.getBeans(RestClient.class);
        Map<String, RestClient> restClients2 = bringApplicationContext.getBeans(RestClient.class);
        Map<String, BringService> bringServices = bringApplicationContext.getBeans(
            BringService.class);

        // then
        assertThat(restClient).isNotNull();
        assertThat(restClient.getUrl()).isEqualTo("https://");
        assertThat(restClient.getKey()).isEqualTo("KEY");
        assertThat(restClients).hasSize(2);
        assertThat(restClients2).hasSize(2);
        assertThat(restClients).containsValue(restClient);
        assertThat(restClients2).containsValue(restClient);
        assertThat(restClients.get("bringRestClient22").getUuid())
            .isNotEqualTo(restClients2.get("bringRestClient22").getUuid());

        assertThat(bringServices).hasSize(3);

        List<RestClient> clients = bringServices.values().stream()
            .map(BringService::getBringRestClient)
            .filter(client -> Objects.equals(client, restClient))
            .toList();
        assertThat(clients).hasSize(2);

        assertThat(bringServices.get("bringService3")).isNotNull();
        assertThat(bringServices.get("bringService3").getBringRestClient()).isNotNull();
        assertThat(bringServices.get("bringService3").getBringRestClient().getUrl()).isEqualTo(
            "https://ssssss");
        assertThat(bringServices.get("bringService3").getBringRestClient().getKey()).isEqualTo(
            "KEY");
    }

    @DisplayName("Should register @Component and @Service beans in Bring Context")
    @Test
    void shouldCreateComponentBeans() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(
            BringBeansService.class);

        // when
        BringBeansService bringBeansService = bringApplicationContext.getBean(
            BringBeansService.class);

        // then
        assertThat(bringBeansService).isNotNull();
    }


    @DisplayName("Should inject beans annotated with different annotations")
    @Test
    void shouldCreateAndInjectDifferentBeans() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(
            TEST_DATA_PACKAGE + ".fullinjection");

        // when
        var useCase = bringApplicationContext.getBean(GetInfoFromExternalServicesUseCase.class);
        var restClient3 = bringApplicationContext.getBean(
            testdata.di.positive.fullinjection.RestClient.class, "restClient3");

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
        assertThat(restClient3.getUrl()).isEqualTo("url");
        assertThat(restClient3.getUsername()).isEqualTo("username");
    }

    @DisplayName("Should return new object when getting prototype bean")
    @Test
    void shouldCreatePrototypeBean() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(
            TEST_DATA_PACKAGE + ".prototype.off");

        // when
        var barista = bringApplicationContext.getBean(
            testdata.di.positive.prototype.off.Barista.class);
        var barista2 = bringApplicationContext.getBean(
            testdata.di.positive.prototype.off.Barista.class);

        // then
        assertThat(barista).isNotNull();
        assertThat(barista2).isNotNull();
        assertThat(barista).isNotEqualTo(barista2);
        assertThat(barista.getUuid()).isNotEqualTo(barista2.getUuid());
    }

    @DisplayName("Should return new object when getting prototype configuration bean")
    @Test
    void shouldCreatePrototypeConfigurationBean() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(
            TEST_DATA_PACKAGE + ".prototype.off");

        // when
        var bean = bringApplicationContext.getBean(SimpleClass.class);
        var bean2 = bringApplicationContext.getBean(SimpleClass.class);

        // then
        assertThat(bean).isNotNull();
        assertThat(bean2).isNotNull();
        assertThat(bean).isNotEqualTo(bean2);
        assertThat(bean.getUuid()).isNotEqualTo(bean2.getUuid());
    }

    @DisplayName("Should return same prototype object when proxy mode OFF")
    @Test
    void shouldCreateSingletonWithPrototypeBean() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(
            TEST_DATA_PACKAGE + ".prototype.off");

        // when
        var coffeeShop = bringApplicationContext.getBean(CoffeeShop.class);
        var coffeeShop2 = bringApplicationContext.getBean(CoffeeShop.class);

        // then
        assertThat(coffeeShop).isNotNull();
        assertThat(coffeeShop2).isNotNull();
        assertThat(coffeeShop).isEqualTo(coffeeShop2);
        assertThat(coffeeShop.getBarista().getUuid()).isEqualTo(coffeeShop2.getBarista().getUuid());
    }

    @DisplayName("Should return new object when getting prototype ProxyMode ON bean")
    @Test
    void shouldCreatePrototypeBean_withProxy() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(
            TEST_DATA_PACKAGE + ".prototype.on");

        // when
        var barista = bringApplicationContext.getBean(
            testdata.di.positive.prototype.on.Barista.class);
        var barista2 = bringApplicationContext.getBean(
            testdata.di.positive.prototype.on.Barista.class);

        // then
        assertThat(barista).isNotNull();
        assertThat(barista2).isNotNull();
        assertThat(barista).isNotEqualTo(barista2);
        assertThat(barista.getUuid()).isNotEqualTo(barista2.getUuid());
    }

    @DisplayName("Should return new object when getting prototype ProxyMode ON bean injection via interface")
    @Test
    void shouldCreatePrototypeBeanInterfaceInjection_withProxy() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(
            TEST_DATA_PACKAGE + ".prototype.onwithinterface");

        // when
        var barista = bringApplicationContext.getBean(
            testdata.di.positive.prototype.onwithinterface.Barista.class);
        var barista2 = bringApplicationContext.getBean(
            testdata.di.positive.prototype.onwithinterface.Barista.class);

        // then
        assertThat(barista).isNotNull();
        assertThat(barista2).isNotNull();
        assertThat(barista).isNotEqualTo(barista2);
        assertThat(barista.getUuid()).isNotEqualTo(barista2.getUuid());
    }

    @DisplayName("Should return new object when getting prototype ProxyMode ON configuration bean")
    @Test
    void shouldCreatePrototypeConfigurationBean_withProxy() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(
            TEST_DATA_PACKAGE + ".prototype.on");

        // when
        var bean = bringApplicationContext.getBean(
            testdata.di.positive.prototype.on.SimpleClass.class);
        var bean2 = bringApplicationContext.getBean(
            testdata.di.positive.prototype.on.SimpleClass.class);

        // then vi
        assertThat(bean).isNotNull();
        assertThat(bean2).isNotNull();
        assertThat(bean).isNotEqualTo(bean2);
        assertThat(bean.getUuid()).isNotEqualTo(bean2.getUuid());
    }

    @DisplayName("Should return different prototype object when proxy mode ON")
    @Test
    void shouldCreateSingletonWithPrototypeBean_withProxy() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(
            TEST_DATA_PACKAGE + ".prototype.on");

        // when
        var coffeeShop = bringApplicationContext.getBean(
            testdata.di.positive.prototype.on.CoffeeShop.class);
        var coffeeShop2 = bringApplicationContext.getBean(
            testdata.di.positive.prototype.on.CoffeeShop.class);

        // then
        assertThat(coffeeShop).isNotNull();
        assertThat(coffeeShop2).isNotNull();
        assertThat(coffeeShop).isEqualTo(coffeeShop2);
        assertThat(coffeeShop.getBarista()).isNotEqualTo(coffeeShop2.getBarista());
        assertThat(coffeeShop.getBarista().getUuid()).isNotEqualTo(
            coffeeShop2.getBarista().getUuid());
        assertThat(coffeeShop.getBarista()).isNotEqualTo(coffeeShop2.getBarista());
        assertThat(coffeeShop.getBarista().getUuid()).isNotEqualTo(
            coffeeShop2.getBarista().getUuid());
    }

}