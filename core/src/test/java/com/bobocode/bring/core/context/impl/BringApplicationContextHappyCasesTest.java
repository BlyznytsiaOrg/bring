package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.testdata.di.positive.configuration.client.RestClient;
import com.bobocode.bring.testdata.di.positive.configuration.configuration.TestConfiguration;
import com.bobocode.bring.testdata.di.positive.configuration.service.BringService;
import com.bobocode.bring.testdata.di.positive.constructor.BringBeansService;
import com.bobocode.bring.testdata.di.positive.constructorproperties.ProfileBeanConstructor;
import com.bobocode.bring.testdata.di.positive.contract.Barista;
import com.bobocode.bring.testdata.di.positive.fieldproperties.ProfileBean;
import com.bobocode.bring.testdata.di.positive.fullinjection.GetInfoFromExternalServicesUseCase;
import com.bobocode.bring.testdata.di.positive.prototype.off.CoffeeShop;
import com.bobocode.bring.testdata.di.positive.prototype.off.SimpleClass;
import com.bobocode.bring.testdata.di.positive.primary.bean.Employee;
import com.bobocode.bring.testdata.di.positive.primary.component.C;
import com.bobocode.bring.testdata.di.positive.qualifier.PrintService;
import com.bobocode.bring.testdata.di.positive.qualifier.Printer;
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
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".fullinjection");

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

    @DisplayName("Should autowire appropriate bean when we have 2 implementations and one of them is marked by @Primary annotation")
    @Test
    void injectPrimaryComponent() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".primary.component");

        // when
        C c = bringApplicationContext.getBean(C.class);

        // then
        assertThat(c).isNotNull();
        assertThat(c.getField()).isNotNull();
        assertThat(c.getField()).isInstanceOf(com.bobocode.bring.testdata.di.positive.primary.component.A.class);
    }

    @DisplayName("Should register appropriate bean when we have 2 methods with the same bean type to return and one of them is marked by @Primary annotation")
    @Test
    void registerPrimaryBean() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".primary.bean");

        // when
        Employee employee = bringApplicationContext.getBean(Employee.class);

        // then
        assertThat(employee).isNotNull();
        assertThat(employee.getName()).isEqualTo("Jerry");

    }

    @DisplayName("Should autowire appropriate bean when we have 2 implementations and @Qualifier annotation")
    @Test
    void registerQualifierComponent() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".qualifier");

        // when
        PrintService printer = bringApplicationContext.getBean(PrintService.class);

        // then
        assertThat(printer).isNotNull();
        assertThat(printer.getPrinter().print()).isEqualTo("Canon");

    }

    @DisplayName("Should inject implementations of Interface to Field")
    @Test
    void shouldInjectListOfInterfaceImplementationToField() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".listfieldinjector");

        // when
        var aBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.listfieldinjector.AField.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(aBean.getList()).isNotNull();
        assertThat(aBean.getList()).hasSize(3);
    }

    @DisplayName("Should inject implementations of Interface to Constructor")
    @Test
    void shouldInjectListOfInterfaceImplementationToConstructor() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".listconstructorinjector");

        // when
        var aBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.listconstructorinjector.AConstructor.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(aBean.getList()).isNotNull();
        assertThat(aBean.getList()).hasSize(2);
    }

    @DisplayName("Should inject implementations of Interface to Setter")
    @Test
    void shouldInjectListOfInterfaceImplementationToSetter() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".listsetterinjector");

        // when
        var aBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.listsetterinjector.ASetter.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(aBean.getList()).isNotNull();
        assertThat(aBean.getList()).hasSize(2);
    }

    @DisplayName("Should return new object when getting prototype bean")
    @Test
    void shouldCreatePrototypeBean() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".prototype.off");

        // when
        var barista = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.prototype.off.Barista.class);
        var barista2 = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.prototype.off.Barista.class);

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
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".prototype.off");

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
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".prototype.off");

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
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".prototype.on");

        // when
        var barista = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.prototype.on.Barista.class);
        var barista2 = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.prototype.on.Barista.class);

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
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".prototype.onwithinterface");

        // when
        var barista = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.prototype.onwithinterface.Barista.class);
        var barista2 = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.prototype.onwithinterface.Barista.class);

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
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".prototype.on");

        // when
        var bean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.prototype.on.SimpleClass.class);
        var bean2 = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.prototype.on.SimpleClass.class);

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
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".prototype.on");

        // when
        var coffeeShop = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.prototype.on.CoffeeShop.class);
        var coffeeShop2 = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.prototype.on.CoffeeShop.class);

        // then
        assertThat(coffeeShop).isNotNull();
        assertThat(coffeeShop2).isNotNull();
        assertThat(coffeeShop).isEqualTo(coffeeShop2);
        assertThat(coffeeShop.getBarista()).isNotEqualTo(coffeeShop2.getBarista());
        assertThat(coffeeShop.getBarista().getUuid()).isNotEqualTo(coffeeShop2.getBarista().getUuid());
        assertThat(coffeeShop.getBarista()).isNotEqualTo(coffeeShop2.getBarista());
        assertThat(coffeeShop.getBarista().getUuid()).isNotEqualTo(coffeeShop2.getBarista().getUuid());
    }

    @DisplayName("Should inject implementations of Interface to Field in appropriate Order")
    @Test
    void shouldInjectListOfInterfaceImplementationToFieldInOrder() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".listfieldinjector");

        // when
        var aBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.listfieldinjector.AField.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(aBean.getList()).isNotNull();
        assertThat(aBean.getList().get(0)).isInstanceOf(com.bobocode.bring.testdata.di.positive.listfieldinjector.B.class);
        assertThat(aBean.getList().get(1)).isInstanceOf(com.bobocode.bring.testdata.di.positive.listfieldinjector.D.class);
        assertThat(aBean.getList().get(2)).isInstanceOf(com.bobocode.bring.testdata.di.positive.listfieldinjector.C.class);
    }
}