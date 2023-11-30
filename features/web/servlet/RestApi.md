# Building REST API with Bring: A Quick Guide

## Introduction

When building a RESTful API using the Bring framework, the annotations like `@RestController`, `@RequestMapping`, `@GetMapping`, `@PathVariable`, `@RequstBody` and others can be used. 
These annotations simplify the development process and ensure a standardized approach to creating web services. 
`ResponseEntity` allows you to customize the HTTP response, including status codes, headers, and the response body.
This guide will walk you through the essential steps to set up a REST controller.

**1. Create a REST Controller**

To get started, create a class and annotate it with`@RestController`. 
This annotation indicates that the class will handle HTTP requests and produce HTTP responses for a RESTful API. 
The class should implement marker interface `BringServlet` to be recognized as controller by the framework and setup of the REST controller context.
The `@RequestMapping` annotation can be utilized to define the base path for all endpoints in the controller (`/api`).

**Example:**
```java
@RestController
@RequestMapping(path = "/api")
public class MyRestController implements BringServlet {
    // REST controller logic and methods
}
```
**NOTE:** If there is a duplication of request paths in the web application, the `RequestPathDuplicateException` is thrown.


**2. Define Endpoints with @GetMapping**

Use the @GetMapping annotation to define methods that handle HTTP GET requests. These methods will serve as endpoints for retrieving information.
When you need to extract values from the URI use the `@PathVariable` or `@RequestParam` annotation.

**Example:**
```java
 @RestController
 @RequestMapping(path = "/api")
 public class MyRestController implements BringServlet {
    
    @GetMapping(path = "/resource/{id}")
    public ResponseEntity<Resource> getResource(@PathVariable Long id) {
         // Your implementation logic here
    }
 }
```
**Example:**
```java
@RestController
@RequestMapping(path = "/api")
public class MyRestController implements BringServlet {

    @GetMapping(path = "/resource")
    public ResponseEntity<Resource> getResource(@RequestParam Long id, @RequestParam String name) {
        // Your implementation logic here
    }
}
```
**NOTE:** If a required request parameter is absent for a method parameter type annotated with `@RequestParam`, the `MissingRequestParamException` is thrown.

**3. Define Endpoints with @PutMapping**

Use the `@PutMapping` annotation to define methods that handle HTTP PUT requests. These methods will serve as endpoints for updating existing resources.

**Example:**
```java
 @RestController
 @RequestMapping(path = "/api")
 public class MyRestController implements BringServlet {
    
    @PutMapping(path = "/resource/{id}")
    public ResponseEntity<String> updateResource(@PathVariable Long id, 
                                                 @RequestBody UserDto dto) {
         // Your implementation logic here
    }
 }
```
Here, the updateResource method handles `PUT` requests to `/api/resource/{id}`. 
The `@PathVariable` annotation extracts the id from the URI and `@RequestBody` is used to capture the data sent in the request body.

**4. Define Endpoints with  @RequestHeader**

Utilize the @RequestHeader annotation to extract values from HTTP headers in your endpoint methods.

**Example:**
```java
@RestController
@RequestMapping(path = "/api")
public class MyRestController implements BringServlet {

    @PostMapping(path = "/resource")
    public ResponseEntity<String> resourceHeaders(@RequestHeader("Authorization") String authToken) {
        // Your implementation logic here
    }
}
```
**NOTE:** If the required value is missing for the @RequestHeader annotation, the MissingRequestHeaderAnnotationValueException is thrown.

**5. Use ResponseEntity for Flexible Responses**

`ResponseEntity` allows to customize the HTTP response, including status codes, headers and the response body.

**Example:**

```java
import java.util.ResourceBundle;

@RestController
@RequestMapping(path = "/api")
public class MyRestController implements BringServlet {

    @PostMapping(path = "/resource")
    public ResponseEntity<Resource> saveResource(@RequestBody ResourceDto dto) {
        // Save resource to DB
        Resource resource = saveToDB(dto);
        
        // Set custom headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("HeaderName", "HeaderValue");
        
        // return instance of ResponseEntity
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
```

**6. Define Endpoints with @ResponseStatus**

Utilize the @ResponseStatus annotation to define the desired HTTP response status code for specific methods.

**Example:**
```java
 @RestController
 @RequestMapping(path = "/api")
 public class MyRestController implements BringServlet {
    
    @PutMapping(path = "/resource/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<String> updateResource(@PathVariable Long id, 
                                                 @RequestBody UserDto dto) {
         // Your implementation logic here
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
 }
```
**NOTE:** When both `ResponseEntity` and `@ResponseStatus` are used in a controller method, 
the HTTP status from `ResponseEntity` takes precedence over the one specified by `@ResponseStatus`.

### See Also
- [Web Module](../../Web.md)