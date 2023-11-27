# TomcatWebServer

## Overview

**The TomcatWebServer** implements the `WebServer` interface for the `Apache Tomcat` server.
This class encapsulates the embedded Tomcat server and provides methods to start,
stop, and retrieve information about the server.

The `TomcatWebServer` constructor takes a configured `Tomcat` instance and initializes the server. The server is started in a separate thread to trigger initialization listeners. Additionally, a blocking non-daemon thread is created to prevent immediate shutdown of the server.

This class uses a monitor object for synchronization and a volatile boolean flag (started) to track the server's state. It also contains methods for checking whether connectors have started, getting a description of ports, and obtaining the context path.

## Dependencies

- `Tomcat`

## Methods

### `initialize()`
Initializes the TomcatWebServer.

### `startDaemonAwaitThread()`
Starts a blocking non-daemon thread to prevent immediate shutdown of the server.

### `stopSilently()`
Stops the Tomcat server silently. 

### `destroySilently()`
Destroys the Tomcat server silently.

### `start()`
Starts the Tomcat server. Logs information about the server's start.

### `checkThatConnectorsHaveStarted()`
Checks that connectors have started.

### `checkConnectorHasStarted(Connector connector)`
Checks if a specific connector has started.
- **Parameters:**
    - `connector`: The `Connector` class in `Tomcat`. It is responsible for configuring and managing the properties of the connection.

### `getPortsDescription(boolean localPort)`
Gets the description of ports.

### `getContextPath()`
Gets the context path.

### `stop()`
Stops the Tomcat server.

### `getPort()`
Gets the port of the Tomcat server.