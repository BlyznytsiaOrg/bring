package com.bobocode.bring.core.injection.primary;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.context.impl.BringApplicationContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.di.positive.primary.bean.Employee;
import testdata.di.positive.primary.component.A;
import testdata.di.positive.primary.component.C;

import static org.assertj.core.api.Assertions.assertThat;

class PrimaryBeanInjectionTest {

    private static final String TEST_DATA_PACKAGE = "testdata.di.positive";

    @DisplayName("Should autowire appropriate bean when we have 2 implementations and one of them is marked by @Primary annotation")
    @Test
    void shouldInjectPrimaryComponent() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".primary.component");

        // when
        C c = bringApplicationContext.getBean(C.class);

        // then
        assertThat(c).isNotNull();
        assertThat(c.getField()).isNotNull();
        assertThat(c.getField()).isInstanceOf(A.class);
    }

    @DisplayName("Should register appropriate bean when we have 2 methods with the same bean type to return and one of them is marked by @Primary annotation")
    @Test
    void shouldRegisterPrimaryBean() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".primary.bean");

        // when
        Employee employee = bringApplicationContext.getBean(Employee.class);

        // then
        assertThat(employee).isNotNull();
        assertThat(employee.getName()).isEqualTo("Jerry");
    }
}
