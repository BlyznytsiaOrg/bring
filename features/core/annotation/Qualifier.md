# Qualifier Annotation

## Introduction


Annotation used to qualify the selection of a bean when multiple beans of the same type
are available for autowiring or injection.

This annotation can be applied to fields, methods, parameters, types
to specify the qualifier value, aiding in the selection of the exact bean to be injected or autowired.

The `value` attribute within `@Qualifier` allows for providing a specific identifier 
or qualifier string to differentiate between multiple beans of the same type. When used in conjunction
with dependency injection bring frameworks, it helps in precisely identifying the intended bean to be injected.

## Usage

By default, Bring resolves autowired dependencies based on their types.
In cases where there are multiple beans of the same type within the container, 
the framework will raise a NoUniqueBeanException. 
This exception indicates that there is a conflict, and Bring cannot determine which bean to inject 
as a collaborator in a specific instance.
Consider a scenario where two potential candidates exist for Bring to inject as bean collaborators:

```java
    public interface Printer {
      String print ();
    }

    @Component
    public class CanonPrinter implements Printer{
      @Override
      public String print() {
        return "Canon";
      }
    }
    
    @Component
    public class LexmarkPrinter implements Printer{
      @Override
      public String print() {
        return "Lexmark";
      }
    }
    
    @Service
    @Getter
    public class PrintService {
      @Autowired
      private Printer printer;
    }


   ```

Attempting to load `PrintService` into our context will result in the Bring framework raising a NoUniqueBeanException. 
This occurs because Bring lacks clarity on which bean to inject. 
To circumvent this issue, various solutions exist, and one such remedy involves 
using the `@Qualifier` annotation.

With the incorporation of the @Qualifier annotation, the ambiguity of determining which bean 
should be injected is resolved:

```java
    @Service
    @Getter
    public class PrintService {
    
      @Autowired
      @Qualifier("canonPrinter")
      private Printer printer;
    
    }
   ```

It's important to note that the qualifier name to be utilized corresponds to the one declared 
in the `@Component` annotation, or bean name resolved through class or method name.
Additionally, it's worth mentioning that achieving the same outcome is possible by applying 
the `@Qualifier` annotation directly on the Printer implementing classes, rather than 
specifying the names in their `@Component` annotations:

```java
    @Component
    @Qualifier("canonPrinter")
    public class CanonPrinter implements Printer{
      //...
    }
    
    @Component
    @Qualifier("lexmarkPrinter")
    public class LexmarkPrinter implements Printer{
        //...
    }

   ```

## Using @Qualifier with method parameter

@Qualifier annotation can be used to disambiguate the choice of bean implementation 
by specifying it with a constructor or method parameter, like in example below:

```java

    public interface Music {
      String getSong();
    }
    
    @Component
    public class ClassicMusic implements Music{
    
      @Override
      public String getSong() {
        return "Beethoven - Moonlight Sonata";
      }
    }
    
    @Component
    public class RockMusic implements Music {
    
      @Override
      public String getSong() {
        return "Lynyrd Skynyrd - Sweet Home Alabama";
      }
    }
    
    @Service
    public class MusicPlayer {
    
      private Music music1;
      private Music music2;
    
      @Autowired
      public MusicPlayer(@Qualifier("classicMusic") Music music1, @Qualifier("rockMusic")Music music2) {
        this.music1 = music1;
        this.music2 = music2;
      }
    
      public String playMusic() {
        return music1.getSong() + " " + music2.getSong();
      }
    }

   ```
Bring recognized which implementations to inject into the MusicPlayer constructor based on 
`@Qualifier` annotations before parameter

Next, initiate the application context and check what will be returned by playMusic() method:

```java
     BringApplicationContext bringApplicationContext = BringApplication.run(Config.class");
     MusicPlayer musicPlayer = bringApplicationContext.getBean(MusicPlayer.class);
     assertThat(musicPlayer.playMusic()).isEqualTo("Beethoven - Moonlight Sonata Lynyrd Skynyrd - Sweet Home Alabama");

   ```
The assertion indicates that the ambiguity has been resolved for both parameters using the @Qualifier 
annotation during the autowiring process.

## Important Points
Bring supports several options for disambiguating the search for dependencies for injection. 
If it is necessary to inject a dependency and there are several suitable beans of the same type, 
then Bring initially tries to disambiguate with the help of `@Primary`, then with the help of `@Qualifier` 
and ultimately by matching the bean names and field/parameter names

[Java Doc](https://BlyznytsiaOrg.github.io/bring-core-javadoc/com/bobocode/bring/core/annotation/Qualifier.html)


