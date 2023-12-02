# Banner Class

The `Banner` class provides functionality for printing a banner to the console, either from a predefined string or custom banner by reading from a file.

![DEFAULT_BANNER](https://github.com/YevgenDemoTestOrganization/bring/assets/114337016/9444e18e-e574-41a4-a41c-af72a4b0ef65)

The `Banner` class supports dynamic profile configuration using VM parameters. 
The default `Mode` value is `ON`, meaning the banner will be printed to the console. To configure the `Mode` to `OFF`, use the `-Dbring.main.banner=off` parameter during application launch. 
To print a custom banner, create a file named `banner.txt` in the `resources` folder and set `-Dbring.main.banner.file=resources/banner.txt` as a VM parameter.


## Configuration Options:

- `BRING_BANNER_KEY`: Key for enabling/disabling the main banner. Default is "ON".
- `BRING_BANNER_FILE_KEY`: Key for specifying the path to the banner file. Default is "resource/banner.txt".

## Method: `printBanner()`

Prints the banner to the console based on the configured mode and file settings.

## Method: `getBannerMode()`

Gets the configured banner mode (ON or OFF).

## Method: `hasToReadFromFile()`

Checks if the banner should be read from a file based on the configuration.

## Method: `getPath()`

Gets the path to the banner file, resolving it based on the classpath.

## Enumeration: Mode
An enumeration of possible values for configuring the Banner.

### Values:

**OFF:**
Disable printing of the banner.

**ON:**
Print the banner to console.