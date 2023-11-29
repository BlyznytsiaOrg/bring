# StaticResourceController

## Overview

**Static Resource Controller:** This `RestController` is responsible for handling static resource requests. It provides an endpoint to retrieve static files based on the requested URI.

The `StaticResourceController` facilitates the serving of static files (e.g., HTML, CSS, JavaScript, images) directly to clients without any processing or modification by the server. This content is typically served as-is from the server's file system or a specified directory.

## Dependencies

- `RestController`
- `StaticResourceService`
- `BringServlet`
- `GetMapping`
- `HttpServletRequest`
- `HttpServletResponse`

## Methods

#### `getStaticFile(HttpServletRequest request, HttpServletResponse response)`

Handles GET requests for static resources.

- **Parameters:**
    - `request`: The incoming HTTP request.
    - `response`: The outgoing HTTP response.

#### `setContentType(HttpServletResponse response, Path path)`

Sets the content type in the HTTP response based on the file type.

- **Parameters:**
    - `response`: The outgoing HTTP response.
    - `path`: The path of the static file.

- **Throws:**
    - `IOException`: If an I/O error occurs while processing the response.

## Details

- Annotated with `@Controller` to mark it as a controller handling web requests.
- Implements the `BringServlet` interface, indicating its role in the Bring Framework's servlet infrastructure.
- The `getStaticFile` method uses the `StaticResourceService` to retrieve the absolute path of the requested static file.
- The `setContentType` method determines the content type based on the file type and sets it in the HTTP response.

---