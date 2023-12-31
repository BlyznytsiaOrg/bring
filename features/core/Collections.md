# Collection Injection in Bring

## Introduction

Collection injection is a powerful feature in the Bring that allows you to inject collections, such as lists and sets
into your Bring beans. This feature is particularly useful when you need to manage and inject a group of related
objects.

## Types of Collections

Bring supports the injection of various types of collections:

1. **List Injection:**
    - Used when you want to inject a list of elements.
    - Example: `List<SomeInterface>`.

2. **Set Injection:**
    - Used when you want to inject a set of unique elements.
    - Example: `Set<SomeInterface>`.

## Collection Injection in Bring

### 1.List Injection

* **Via constructor**

```java
import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;

import java.util.List;

@Component
public class MyListBean {

    private List<SomeInterface> myList;

    @Autowired
    public MyListBean(List<SomeInterface> myList) {
        this.myList = myList;
    }

    // Rest of the class...
}
```

* **Via field**

```java
import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;

import java.util.List;

@Component
public class MyListBean {

    @Autowired
    private List<SomeInterface> myList;

    public MyListBean(List<SomeInterface> myList) {
        this.myList = myList;
    }

    // Rest of the class...
}
```

* **Via setter**

```java
import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;

import java.util.List;

@Component
public class MyListBean {

    private List<SomeInterface> myList;

    @Autowired
    public void setList(List<SomeInterface> list) {
        this.list = list;
    }

    // Rest of the class...
}
```

### 2. Set Injection

Supported injections like in previous example (via constructor, field, setter).

```java
import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;

import java.util.Set;

@Component
public class MySetBean {

    private Set<SomeInterface> mySet;

    @Autowired
    public MySetBean(Set<SomeInterface> mySet) {
        this.mySet = mySet;
    }

    // Rest of the class...
}
```