[![Maven Central](https://img.shields.io/maven-central/v/io.github.blyznytsiaorg.bring.web/web.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.blyznytsiaorg.bring.web/web)


# Bring Framework Documentation

- [Overview](features/Overview.md)  History, Design Philosophy, Feedback, Getting Started.
- [Core](features/Core.md)  IoC Container, Resources, Type Conversion, Scheduling.
- [Web](features/Web.md)  Bring Web.


## Getting Started

If you're new to Bring, consider initiating your experience with a [Bring playground application repo](https://github.com/BlyznytsiaOrg/bring-playground) with a variety of examples of how to use it.
Bring Boot offers a swift and opinionated method to develop a Bring-based application ready for play.
Leveraging the Bring Framework, it prioritizes conventions over extensive configurations, aiming to accelerate your setup process and swiftly get you up and running.


## Installation

- Open your Maven Project:

Open the Maven project where you want to add the Bring framework dependencies.

- Edit pom.xml:

Locate the pom.xml file in your project.

- Add Repository Configuration:

Inside the <repositories> section of your pom.xml, add the following repository configuration 
if you want to try latest Snapshot version of Bring project:

```
<repositories>
		<repository>
			<releases>
				<enabled>false</enabled>
				<updatePolicy>always</updatePolicy>
				<checksumPolicy>warn</checksumPolicy>
			</releases>
			<snapshots>
				<enabled>true</enabled>
				<updatePolicy>never</updatePolicy>
				<checksumPolicy>fail</checksumPolicy>
			</snapshots>
			<name>Nexus Snapshots</name>
			<id>snapshots-repo</id>
			<url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
			<layout>default</layout>
		</repository>
</repositories>

```

This configuration informs Maven about the repository location where it can find the Bring framework artifacts.

- Include Dependency:

Within the <dependencies> section of your pom.xml, add the Bring framework dependency (You will have web & core):

```
<dependencies>
    <dependency>
        <groupId>io.github.blyznytsiaorg.bring.web</groupId>
        <artifactId>web</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>

```

if you would like to work with core only then you need to add core dependency


```
<dependencies>
    <dependency>
        <groupId>io.github.blyznytsiaorg.bring.core</groupId>
        <artifactId>core</artifactId>
        <version>2.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>

```

Ensure the groupId, artifactId, and version values are accurate for the Bring framework you intend to use.

To obtain the release version, simply review the version we've released; there's no need for the repository section:


```
<dependencies>
    <dependency>
        <groupId>io.github.blyznytsiaorg.bring.web</groupId>
        <artifactId>web</artifactId>
        <version>1.0.0</version> <!-- or latest -->
    </dependency>
</dependencies>

```

if you would like to work with core only then you need to add core dependency


```
<dependencies>
    <dependency>
        <groupId>io.github.blyznytsiaorg.bring.core</groupId>
        <artifactId>core</artifactId>
        <version>1.0.0</version> <!-- or latest -->
    </dependency>
</dependencies>

```


- Save Changes:

Save the pom.xml file after adding the repository and dependency configurations.

- Build the Project:

Run a Maven build for your project. You can do this through your IDE or by using the command line:

```
mvn clean install
```

Maven will download the required dependencies from the specified repository and add them to your project's classpath.

- Verify Dependency Addition:

Check the build logs for any errors or warnings related to dependency resolution. If the build completes successfully, it indicates that the Bring framework dependencies are successfully added to your project.

## Feedback and Contributions

If you suspect an issue within the Bring Framework or wish to propose a new feature, kindly utilize [GitHub Issues](https://github.com/BlyznytsiaOrg/bring/issues/new) for reporting problems or submitting feature suggestions
If you have a solution in mind or a suggested fix, you can submit a pull request on [Github](https://github.com/BlyznytsiaOrg/bring). In addition, please read and configure you idea to follow our [Setup Code Style Guidelines](https://github.com/BlyznytsiaOrg/bring/wiki#setup-code-style-guidelines)
