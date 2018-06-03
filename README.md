[![Build Status](https://travis-ci.org/roscopeco/moxy.svg?branch=master)](https://travis-ci.org/roscopeco/moxy) [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco%3Amoxy&metric=alert_status)](https://sonarcloud.io/dashboard/index/com.roscopeco%3Amoxy) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco%3Amoxy&metric=bugs)](https://sonarcloud.io/component_measures?id=com.roscopeco%3Amoxy&metric=bugs) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco%3Amoxy&metric=code_smells)](https://sonarcloud.io/component_measures?id=com.roscopeco%3Amoxy&metric=code_smells) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco%3Amoxy&metric=coverage)](https://sonarcloud.io/component_measures?id=com.roscopeco%3Amoxy&metric=coverage) [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco%3Amoxy&metric=ncloc)](https://sonarcloud.io/component_measures?id=com.roscopeco%3Amoxy&metric=ncloc)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco%3Amoxy&metric=sqale_rating)](https://sonarcloud.io/component_measures?id=com.roscopeco%3Amoxy&metric=sqale_rating) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco%3Amoxy&metric=reliability_rating)](https://sonarcloud.io/component_measures?id=com.roscopeco%3Amoxy&metric=reliability_rating) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco%3Amoxy&metric=security_rating)](https://sonarcloud.io/component_measures?id=com.roscopeco%3Amoxy&metric=security_rating) [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.roscopeco%3Amoxy&metric=sqale_index)](https://sonarcloud.io/component_measures?id=com.roscopeco%3Amoxy&metric=sqale_index)

# Moxy

## Lean-and-mean mocking framework for Java with a fluent API.

### Contents

* [What is this?](#what-is-this)
* [Purpose](#_another_-mock-framework)
* [Usage](#enough-about-that-how-do-i-use-it)
  * [Requirements](#requirements)
  * [Getting the code](#getting-the-code)
  * [Using the code](#using-the-code)
    * [Creating mocks](#creating-mocks)
    * [Stubbing](#stubbing)
    * [Verifying](#verifying)
    * [Argument matchers](#argument-matchers)
    * [Partial Mocking and Spying](#partial-mocking-and-spying)
    
### What is this?

Moxy is a fairly lightweight mock/spy framework for use in Java automated
testing. Its purpose is to allow lightweight mock implementations of 
interfaces and classes to be easily created and used within tests, 
allowing the class under test's dependencies to be fulfilled without
spinning up servers, talking to databases, and all that other stuff
that slows things down and gets in the way.

If the concept of mocks is completely new to you, go ahead and Google
around a bit. There's a wealth of information (as well as other mock
frameworks) out there. The rest of this document assumes you're at
least aware of mocking, and the whys and hows of using it.

### _Another_ mock framework??

In using other frameworks, I found myself constantly looking for something
extra, or for a different API, or chasing down weird errors that would pop
up sometimes hundreds of lines later, in completely different tests 
(admittedly usually because I was using the framework incorrectly). 

I wanted to understand mocking from the inside out, to get to grips with
how and why the existing frameworks worked the way they do, and had the
limitations that they had. So, I decided to write my own simple mocking
framework to help me along. That way, I'd get a better understanding of
the issues, and also I'd end up with something that had an API that 
I personally wanted to use - win/win!

That simple framework grew a bit, and became Moxy. Do we need another 
framework? I don't know. There's a lot of good code already in the wild. 
But because this scratched my itch, there's a chance it might scratch 
yours too, so I decided to release it under the MIT license.

To summarise, the aims of Moxy are:

* To be simple to pick up and use.
* To be powerful enough to get you where you need to go.
* To have a useful, usable, IDE-friendly API.
* To minimise the _gotchas_ that come with some other frameworks.
* To fail fast wherever possible, and not punish you too harshly for incorrect usage.
* And finally, to be easily extensible to suit your needs. 
  
### Enough about that, how do I use it?

#### Requirements

##### Compile/runtime:

* Java (1.8+).
* ASM 6.2 (base, -tree and -util)
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
	<groupId>com.roscopeco</groupId>
	<artifactId>moxy</artifactId>
	<version>0.82</version>
</dependency>
```

##### Gradle

Something like the following should have you set up:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    testCompile 'com.roscopeco:moxy:0.82'
}
```

##### Clone from Git

You can clone the latest code (or any release using the appropriate tag) 
directly from GitHub, and just set it up as a dependent project/module/whatever 
in your IDE, or make sure it's somewhere on your classpath, and you should
be good to go. 

The project is built with Maven, so just running `mvn package` will grab 
the dependencies and build you a `.jar` file in the `target/` directory.

If you do `mvn install` you'll be able to reference it from your other
Maven projects locally in the usual way.

You can generate a bit of Javadoc with `mvn javadoc:javadoc`,
which generates the docs in `target/apidocs`.

#### Using the code

##### Javadoc

Full JavaDoc for the latest available release can be found at https://roscopeco.github.io/moxy/

##### Creating mocks 

The API is, in a general sense, modelled after other mocking frameworks
that are already out there, as well as the APIs of commonly-used test
frameworks, so if you've already done any testing you'll be up to speed
in no time.

The standard idiom is firstly, static import the appropriate methods:

```java
import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
```

The first line gives you the basic mock functionality, while the second
gives you access to _argument matchers_, which are so cool they get their
own section later on. 

With this, you can create mocks. Let's imagine for a moment that you have
an interface `AmazingButSlowDatabaseService` that the class you're 
testing uses to talk to a database. You'd simply mock it like so:

```java
AmazingButSlowDatabaseService mock = mock(AmazingButSlowDatabaseService.class);
```

**Side note**
> In this case, we said it was an interface, but it could just as easily
be a class (abstract or concrete) - the API is exactly the same regardless.

##### Stubbing

By default, this will mock all public methods of the class, and make them
all return sensible defaults (think zeroes, nulls, falseys). If you want to
make a given method return something else, you would _stub_ it, like so:

```java
// Given 'String AmazingButSlowDatabaseService.connect(String)'...
when(() -> mock.connectDatabase("mydatabase")).thenReturn("OK");
```

Now, whenever you call `connect` with `"mydatabase"` as the argument,
it'll return "OK". That argument part is kind of important - if you call
it with anything other than `"mydatabase"`, it'll still return `null`.
If you need it to be more general than that, you'll want to use _matchers_,
which we'll get to in a bit.

You can also stub it to throw an exception. Behold:

```java
when(() -> mock.connectDatabase("invalid")).thenThrow(new Exception());
```

The same applies regarding the arguments - here, it will only throw if 
it's called with `"invalid"` as the argument.

Of course, there's no reason you can't combine the two. You can stub the 
same method for different arguments as many times as you like, implementing
a combination of (potentially different) returns and throws as you need.

**Side note**
> Incidentally, Moxy is smart enough to know if you're stubbing a void method,
in which case you won't have the option to `thenReturn`, because stubbing
a return value for a void method would be, well, pointless.

Once you're done stubbing, you can go ahead and pass your mock to the class
you're testing (or to whoever else needs it really). If you've stubbed 
appropriately, the clients will never know they're not talking to the
real deal and you can test with impunity.

##### Spying

Instead of stubbing, you can turn your mock into a spy by having it call 
the real method, like so:

```java
when(() -> mock.connectDatabase("invalid")).thenCallRealMethod();
```

Obviously this only applies if your mock isn't based on an Interface and the 
real method isn't `abstract`. If either of those conditions are true, you'll
receive a helpful exception when the would-be spy is called.

When using `thenCallRealMethod`, you still get all the usual verification
goodness that Moxy provides, so you can still use matchers, for example,
or check how many times it was called and make sure it didn't throw exceptions.

**Side note**
> It is worth noting that a mock set to call its real method will do so
every time it's invoked, including when it's invoked in an `assertMock`
call. So be careful when calling real methods that have side-effects...

##### Verifying

So you passed in your mocks, and you appear to have gotten away with it -
nothing went bang and your code _seems_ to have done what it should. 
But how can you be sure it called the methods you expected it to on 
the service? The short (and in fact only) answer is, you _verify_, like so:

```java
assertMock(() -> mock.connectDatabase("mydatabase")).wasCalledOnce();
```

I'm sure you get the gist - this asserts that the given method was called,
with the given arguments, once. There are a variety of other assertions 
you can make. Here are just a few:

```java
assertMock(() -> /* mock method call... */).wasCalledTwice();	// twice
assertMock(() -> /* mock method call... */).wasCalled(5);			// 5 times
assertMock(() -> /* mock method call... */).wasCalledAtLeast(2);	// >= twice
```

And you can chain them:

```java
assertMock(() -> /* mock method call... */)
		.wasCalledAtLeast(2)							// >= twice
		.wasCalledAtMost(5)							// and <= 5
```

For a full list of the available asserts, take a look at the
`com.roscopeco.moxy.api.MoxyVerifier` class.

##### Argument matchers

When the time comes that matching methods based on simple immediate arguments
just isn't enough, you'll want to take a look at _argument matchers_. 

Simply put, matchers are used in `when()` and `assertMock()` calls to
allow you to specify variable arguments based on some condition. 

For example, say you want your `connectDatabase` method to _always_ return
`"OK"`, regardless of the string that's passed to it. Moxy has your back:

```java
when(() -> mock.connectDatabase(any())).thenReturn("OK");
```

That `any()` right there is a special argument that signals to Moxy that
it shouldn't care what argument is passed - it should just always return 
`"OK"`. `any()` is probably the simplest _argument matcher_ Moxy has,
and also one that you'll probably use quite a lot.

Argument matchers work using some dark magic and a bit of luck under the 
hood, and that means there are unfortunately some caveats to using them:

* For a given invocation, once you pass one argument a matcher, you **must** pass the rest with matchers too (see below).
* You **must not** use any of the argument matchers outside of a `when()` or `assertMock` call. Although there really isn't any reason you'd want to anyway.
* You may occasionally receive an odd error message when using matchers. The aim is that this should always be in the right place (not at some indeterminate future time) though, and should always give you an error message telling you what went wrong and why. If ever that's not the case, then I consider it a potential bug and would be grateful if you'd report it on GitHub.

**Side note**
> I'm working hard to eliminate as many of these caveats as possible. Especially the one about passing all arguments as matchers, that one's got me up nights trying to figure a way around it.

If you've used certain other mock frameworks, then you'll be familiar with some of these caveats.
I'm guessing their matchers work in more or less the same way as Moxy's.

##### Passing all arguments as matchers

So you've used a matcher for one of the arguments to a method, let's call it 
`mock.hasThreeArguments(String, String, String)`, but you want to pass
the rest of the arguments as exact arguments. How do you do that with matchers?
Once again, we've got you covered:

```java
when(() -> mock.hasThreeArguments(any(), eq("exact"), eq("arguments")))
```

`eq()` is another matcher that's custom designed for this very purpose. Using 
it, you can pass exact arguments as matchers.

##### Matcher-based assertions

Matchers work exactly the same way in `assertMock()` as they do in `when()`,
except instead of stubbing a return or throws value, they match invocations to
be asserted against. So:

```java
assertMock(() -> mock.method(any())).wasCalledOnce();
```

is simply saying "Assert that mock.method was called once, with any arguments. Simples!

##### Primitive matchers

Ahh, good old primitives. They've not overcomplicated the matcher API at all...

Most of the matchers provided by Moxy support primitive arguments as well as reference types.
However, it's important that you make sure you use the appropriate primitive variants
of the matcher methods when creating your matchers. So, e.g. for an `int`, you'd use
`anyInt()` rather than plain `any()`. 

Sadly, due to the ~~stupid~~ way Java generics and autoboxing are implemented, the
compiler won't catch you if you forget this rule, and one of two things will 
happen at runtime:

* _Most of the time_ Moxy will catch it, and you'll get a helpful error message telling you that it appears you may have forgotten to use a primitive matcher.
* _But some of the time_ you'll get a no-message `NullPointerException` from some seemingly-random place in your code that is actually the result of the JVM trying to auto-unbox a null to a primitive wrapper class.

So if you do get that seemingly random NPE, and you're using matchers, check carefully
that you're using primitive matchers as appropriate. A common vector for getting this 
wrong seems to be when using primitive `and()` and `or()` matchers - the matchers 
that are being and'ed and or'ed _also_ need to be primitive. So, e.g., instead of:

```java
mock.method(andLong(gt(5), lt(10)))
```

you want:

```java
mock.method(andLong(gtLong(5), ltLong(10)))
```

The proper suffixes for the primitive variants of the matcher methods are:

* `Byte` - byte
* `Char` - char
* `Short` - short
* `Int` - int
* `Long` - long
* `Float` - float
* `Double` - double
* `Bool` - boolean

##### Matcher types

The following matcher types are supported out-of-the-box:

* `and(matcher1, matcher2, ..., matcherN)` - Matches only if all nested matchers match.
* `any()` - Matches anything
* `anyOf(Object1, Object2, ..., ObjectN)` - Matches if the argument is in the list.
* `endsWith(String)` - Matches if the `String` argument ends with the given string. *****
* `eq(Object)` - Matches if the argument `.equals()` the given object. Supports null.
* `gt(Object)` - Matches if the `Comparable` argument is greater-than the given object.
* `lt(Object)` - Matches if the `Comparable` argument is less-than the given object.
* `neq(Object)` - Matches if the argument doesn't `.equals()` the given object. Supports null.
* `not(matcher)` - Negates the given matcher. Note the difference between this and `neq()`.
* `or(matcher1, matcher2, ..., matcherN)` - Matches if any of the nested matchers match.
* `regexMatch(String)` - Matches if the String argument matches the supplied regular expression. *****
* `startsWith(String)` - Matches if the `String` argument starts with the given string. *****

 ***** These matchers have no primitive equivalent.
 
There are lots more matcher types planned, so take a look at the 
`com.roscopeco.moxy.matchers.Matchers` class documentation for up-to-the minute 
matcher type support. It's also a good place to find full details for all the matchers.

##### Custom matchers

You can implement your own matchers in one of two ways:

* For simple matchers, the `custom()` matcher accepts a Java 1.8 lambda expression which receives the actual argument and is expected to return a boolean indicating whether it matches or not. E.g:

```java
assertMock(() -> mock.method(custom((arg) -> arg instanceof String))).wasCalled();
```

* For more complicated matchers, you can create your own implementation of `MoxyMatcher` and pass that to the `custom()` method instead of a lambda expression. You will need to do this, for example, if your matcher has more complex requirements and needs to manipulate the matcher stack. E.g.

```java
class MyMatcher<T> implements MoxyMatcher<T> {
  @Override
  public boolean matches(T arg) {
    // ... convoluted logic ... 
  }
  
  @Override
  public void addToStack(Deque<MoxyMatcher<?>> stack) {
    // ... more convoluted logic ... 
  }
}
  
// ... Later ... 
  
assertMock(() -> mock.method(custom(new MyMatcher<String>()))).wasCalled();
```

Note that a `default` implementation of `addToStack()` is provided by the interface,
so if you're implementing the interface because your matcher is too long for a readable 
lambda then you don't _have_ to implement it, as long as your matcher only needs to be 
pushed to the stack (like 90% of all matchers do).

**Side note**
> The _matcher stack_? Yup, this is an advanced topic. See the documentation on the MoxyMatcher class for more details.

As a final note on matchers, and custom matchers in particular, if you find you're using
a particular custom matcher a lot and feel that others may benefit from it too, please
do suggest it for inclusion into the core Moxy distribution. GitHub pull requests are the 
preferred format for such suggestions, but if you don't have time for that feel free to submit
a bug/feature request instead.

##### Partial Mocking and Spying

In addition to the standard behaviour of mocking all public methods, Moxy also supports
_partial mocking_, where only _some_ of the methods in a given class are mocked, leaving
the rest with their original implementation.

Partial mocking is inherently trickier than straight-up complete mocking, mostly because
**Moxy doesn't call constructors**. This means that, if the methods you don't mock
require any state that is set in a constructor, they're likely to fail miserably.

For this reason, the usual static _Moxy.mock(...)_ API doesn't support partial mocking -
you'll have to get a little deeper into the API and call a constructor yourself.
This is a deliberate design decision, to ensure that mocks remain type-safe and 
don't suddenly break if you decide later that you need to initialise something in
your constructor.

**Side note**
> Why doesn't Moxy call constructors? Because it can't intelligently choose which constructor
it should be calling, or what arguments it might pass in. These decisions are best left 
to you. Read on to find out how to do it once you've made your decision!

In order to make this work, you'll have to generate the mock `Class` rather than 
directly creating an instance out of thin-air as you've done before. Don't worry,
it's not that much more work:

Given that you have a constructor that looks like:

```java
public MyClass(String arg1, int arg2) { ... }
```

You simply need to generate the class, and then instantiate it with reflection.
The only thing to note is that the mock class will have an extra constructor 
argument, prepended to the normal constructor arguments, into which you'll 
pass the `MoxyEngine` that created the class. So the generated constructor
will look like:

```java
public MyClass(MoxyEngine engine, String arg1, int arg2) { ... }
```

If you're mocking a single method `myMethod`, the whole generation-and-instantiation
piece looks like:

```java
Class<? extends MyClass> mockClass = 
    Moxy.getMoxyEngine().getMockClass(MyClass.class,
        Collections.singleton(
            SimpleAbstractClass.class.getMethod("myMethod")));

MyClass mock = mockClass
    .getConstructor(MoxyEngine.class, String.class, int.class)
    .newInstance(Moxy.getMoxyEngine(), "Some string", 42);
```

Those six extra lines might seem like a bit of a pain, but think of the 
extra work as a reminder that partial mocking is a little bit dangerous.

Often, the behaviour you'll achieve with partial mocking can also be 
accomplished with the `MoxyStubber.thenCallRealMethod()` method,
or you might find you need a combination of the two. In any event, 
you can happily use them together as the need arises.

### How does it work?

In a nutshell, Moxy works by dynamically generating subclasses of your classes and 
interfaces at runtime (this means it can't handle `final` classes yet, but it will
soon, using a Java agent). This generation is done with the excellent ASM library.

These subclasses override all your public methods, and use some internal magic to 
get them to record their arguments, their throws and returns, and some other information,
and then return or throw whatever they've been asked to return or throw.

Other parts of the library then pick up these recorded invocations, and massage them
a bit, put them into lists, sort them, lose them, find them again, and that kind of
thing, ready for the stubbers and verifiers to come along and either tell the mock
what it should do when its called, or ask it what its been up to.

More dark magic and questionable code then take over to turn all these internal things
into friendly external things that are suitable for human consumption.

If you're interested, you take a look around the `com.roscopeco.moxy.impl.asm` package
(start with `com.roscopeco.moxy.impl.asm.ASMMoxyEngine`) to see what's going
on. Internally, the top-level `Moxy` class forwards all its calls to this engine
which then fires off whatever it needs to to get the job done.

You can also see the decompiled bytecode for your mocks in a sort-of user-friendly
format by passing a `PrintStream` to the mock method, e.g.:

```java
MyClass mock = mock(MyClass.class, System.out);
```

will dump the generated class to System.out just prior to defining it
in the `ClassLoader`.

### The legal bit

Moxy is copyright (c)2018 Ross Bamford (and contributors)

This is open-source software under the MIT license. See LICENSE for details.
