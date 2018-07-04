[![Build Status](https://travis-ci.org/roscopeco/moxy.svg?branch=master)](https://travis-ci.org/roscopeco/moxy) [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco.moxy%3Amoxy&metric=alert_status)](https://sonarcloud.io/dashboard/index/com.roscopeco.moxy%3Amoxy) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco.moxy%3Amoxy&metric=bugs)](https://sonarcloud.io/component_measures?id=com.roscopeco.moxy%3Amoxy&metric=bugs) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco.moxy%3Amoxy&metric=code_smells)](https://sonarcloud.io/component_measures?id=com.roscopeco.moxy%3Amoxy&metric=code_smells) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco.moxy%3Amoxy&metric=coverage)](https://sonarcloud.io/component_measures?id=com.roscopeco.moxy%3Amoxy&metric=coverage) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco.moxy%3Amoxy&metric=ncloc)](https://sonarcloud.io/component_measures?id=com.roscopeco.moxy%3Amoxy&metric=ncloc)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco.moxy%3Amoxy&metric=sqale_rating)](https://sonarcloud.io/component_measures?id=com.roscopeco.moxy%3Amoxy&metric=sqale_rating) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco.moxy%3Amoxy&metric=reliability_rating)](https://sonarcloud.io/component_measures?id=com.roscopeco.moxy%3Amoxy&metric=reliability_rating) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco.moxy%3Amoxy&metric=security_rating)](https://sonarcloud.io/component_measures?id=com.roscopeco.moxy%3Amoxy&metric=security_rating) [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco.moxy%3Amoxy&metric=sqale_index)](https://sonarcloud.io/component_measures?id=com.roscopeco.moxy%3Amoxy&metric=sqale_index)

# Moxy

## Lean-and-mean mocking framework for Java with a fluent API.

See also [Javadoc](https://roscopeco.github.io/moxy/).

### What is this?

Moxy is a fairly lightweight yet surprisingly powerful and refeshingly non-opinionated
mock/spy framework for use in testing Java code. It has a fluent, IDE-friendly stubbing
and verification API, runs on Java 8+, and includes out-of-the-box support for
allegedly-evil things such as mocking finals and statics. It knows its place as 
a _tool_ - when it's right for the job, it's just what you need, but equally it will
let you do all the wrong things really, really well if that's your cup of tea.

If it helps, think of it like a hammer. Or an elephant gun. Or something... 

### How do I use it?

There's full documentation on [the wiki](https://github.com/roscopeco/moxy/wiki),
which took quite a while to write, and which I try to keep as up-to-date as 
possible.

But in the spirit of getting you started, here's a [SSCCE](http://sscce.org/) we
made earlier, paraphrased somewhat from Moxy's own tests:

```java
import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestClass {
  public String sayHelloTo(final String who) {
    return "Hello, " + who;
  }

  public String hasTwoArgs(final String arg1, final int arg2) {
    return "" + arg1 + arg2;
  }
}

final class HardToMockClass {
  public static final String staticSayHello(final String who) {
    return "Hello, " + who;
  }

  public final String finalSayHello(final String who) {
    return "Goodbye, " + who;
  }
}

public class ReadmeSSCCE {
  @Test
  public void testClassMockVerifying() {
    mockClasses(HardToMockClass.class);

    when(() -> HardToMockClass.staticSayHello("Bill")).thenCallRealMethod();
    when(() -> HardToMockClass.staticSayHello("Steve")).thenReturn("Hi there, Steve!");

    assertThat(HardToMockClass.staticSayHello("Bill")).isEqualTo("Hello, Bill");
    assertThat(HardToMockClass.staticSayHello("Steve")).isEqualTo("Hi there, Steve!");

    final HardToMockClass mock = new HardToMockClass();

    when(() -> mock.finalSayHello("Jim")).thenAnswer(args -> "He's dead, Jim");

    assertThat(mock.finalSayHello("Jim")).isEqualTo("He's dead, Jim");

    assertMock(() -> HardToMockClass.staticSayHello(any())).wasCalledTwice();
    assertMock(() -> mock.finalSayHello(any())).wasCalledOnce();
  }

  @Test
  public void testClassicMockVerifying() {
    final TestClass mock = mock(TestClass.class);

    mock.sayHelloTo("Bill");
    mock.hasTwoArgs("one", 1);

    assertMocks(() -> {
      mock.sayHelloTo("Steve");
      mock.hasTwoArgs("two", 2);
    })
        .wereNotCalled();

    assertMocks(() -> {
      mock.sayHelloTo("Bill");
      mock.hasTwoArgs("one", 1);
    })
        .wereAllCalledOnce()
        .inThatOrder();
  }
}
```

This barely scratches the surface though, so be sure to check out that [lovingly-crafted documentation](https://github.com/roscopeco/moxy/wiki).
  
### Requirements

##### Compile/runtime:

* Java (1.8+).
* ASM 6.2 (base, -tree and -util)
* Apache commons-lang3 3.7
* opentest4j 1.0.0

##### For building/testing

* Maven 3 (tested with 3.5.3)
* Junit 5
* AssertJ 3.8

#### Getting the code

##### Maven 

Moxy releases are available in Maven Central. You can pull it in with the 
following dependency in your POM:

```xml
<dependency>
	<groupId>com.roscopeco.moxy</groupId>
	<artifactId>moxy-core</artifactId>
	<version>0.90.0</version>
</dependency>
```

If you want to use the JUnit 5 extension to ensure your mocks are reset prior
to each test, include the following dependency:

```xml
<dependency>
	<groupId>com.roscopeco.moxy</groupId>
	<artifactId>moxy-junit5</artifactId>
	<version>0.90.0</version>
</dependency>
```

and if you're interesting in creating your mocks with Mockito-style annotations,
include this:

```xml
<dependency>
	<groupId>com.roscopeco.moxy</groupId>
	<artifactId>moxy-annotations</artifactId>
	<version>0.90.0</version>
</dependency>
```

Development snapshots are sometimes made available in Maven via some extra
configuration. See [this wiki page](https://github.com/roscopeco/moxy/wiki/Maven-Coordinates)
for more information.

Note that, depending on where we are in the release cycle, it is possible for the
latest snapshot to be _behind_ the current release. Check version numbers before
using a snapshot.

##### Gradle

Something like the following should have you set up:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    testCompile 'com.roscopeco.moxy:moxy-core:0.90.0'
}
```

##### Clone from Git

You can clone the latest code (or any release using the appropriate tag) 
[directly from GitHub](https://github.com/roscopeco/moxy), and just set it
up as a dependent project/module/whatever in your IDE, or make sure it's 
somewhere on your classpath, and you should be good to go. 

The project is built with Maven, so just running `mvn package` will grab 
the dependencies and build you a `.jar` file in the `target/` directory.

If you do `mvn install` you'll be able to reference it from your other
Maven projects locally in the usual way.

You can generate a bit of Javadoc with `mvn javadoc:javadoc`,
which generates the docs in `target/apidocs`. The Javadoc for the current
release can always be found at https://roscopeco.github.io/moxy/ .

### The legal bit

Moxy is copyright (c)2018 Ross Bamford (and contributors)

This is open-source software under the MIT license. See LICENSE for details.
