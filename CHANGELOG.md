# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## Experimental

These changes are currently considered experimental and are not yet ready
to merge to `develop`. They are in various branches and are slated to
make it in before 1.0.0.
  
- Java 9 Jigsaw (module) support.
  See: https://github.com/roscopeco/moxy/tree/modular-java9
  
## [Unreleased]

These changes are currently in the `develop` branch, and are slated
to make it into a release soon.

### Added

- New `matches(Function<T, Boolean>)` (and primitive variants) added to
  `Matchers`. Allows a function to be passed, without the extra functionality
  provided by `custom(MoxyMatcher)` (i.e. custom matcher stack manipulation).

## [0.90.1] - 2018-07-10

### Fixed

- Static mock regression; New features introduced a bug in classic mocking
  whereby static methods were no longer ignored. This precludes e.g.
  Collections mocking in Java 9+ See [#4](https://github.com/roscopeco/moxy/issues/4). 

## [0.90.0] - 2018-07-04

### Added
- Class Mocking - a new style of mocking that co-exists with standard
  (now called _classic_) mocking, and allows mocking of finals, statics and
  constructors. This functionality is accessed via the `Moxy.mockClasses(...)`
  API.
  
- Multiple stubs for the same invocation are now supported by chaining
  calls to `then[...]` methods on `ASMMoxyStubber` and `ASMMoxyVoidStubber`.
  Mocks stubbed in this way will behave according to the applied stubbed 
  behaviours, in order, with the final stubbed behaviour persisting until
  explicitly reset or a new `when(...)` call is made.
  
### Changed

- Multiple stubs for the same invocation are now supported by chaining
  calls to `then[...]` methods on `ASMMoxyStubber` and `ASMMoxyVoidStubber`.
  Mocks stubbed in this way will behave according to the applied stubbed 
  behaviours, in order, with the final stubbed behaviour persisting until
  explicitly reset or a new `when(...)` call is made.
  
- `then(Return|Throw|CallRealMethod)` are
  no longer terminal, due to the ongoing stubbing feature being 
  fully implemented.
  
## [0.88.1] - 2018-06-20

### Fixed

Critical bug in delegate caching/invocation that could cause undefined behaviour
under certain (rare) circumstances.

## [0.88.0] - 2018-06-20

### Added

- thenDelegateTo added to MoxyStubber. This allows delegation
  of mocked method calls to any object with a compatible method.
- JUnit 5 extension to reset mocks before each test.
- Simple annotations support and JUnit extension to support same.

### Changed

- Removed restriction on applying different behaviours or stubbing to
  already-stubbed methods.
  
### Fixed

- Bug-fixes around spying and static methods (was breaking tests under e.g. Jacoco)

## [0.86.0] - 2018-06-13

### Added

- Ordered verification, and verification of multiple invocations at once
  with the `assertMocks` method.
- Convenience methods to construct spies directly from static methods
  on Moxy.
- `constructMock` and `constructSpy` methods that provide a
  convenient way to call constructors to create mocks.
- Added project code of conduct and guide to contributing.

### Changed

- Constructors (only) are no longer `synthetic` to avoid false
  negatives in external libraries that treat them as inner class
  constructors.
- Integration tests are now separated out by functionality under
  test, instead of being in one long file.
   
## [0.84] - 2018-06-07

### Added

- Stubbing now supports answers with [thenAnswer](https://roscopeco.github.io/moxy/com/roscopeco/moxy/api/MoxyStubber.html#thenAnswer-com.roscopeco.moxy.api.AnswerProvider-) method.
- Stubbing now supports an arbitrary number of _actions_
  via the [thenDo](https://roscopeco.github.io/moxy/com/roscopeco/moxy/api/MoxyStubber.html#thenDo-java.util.function.Consumer-) method.

### Changed

- **BREAKING CHANGE** - `then(Return|Throw|CallRealMethod)` are
  now terminal. This breaks API from 0.82.
- Much improved fail-fast behaviour when stubbing/asserting.
  Thanks to improved logic behind the way these are handled,
  we now have far fewer edge cases that could result in random
  exceptions.
  
### Removed

- **BREAKING CHANGE** - MoxyMatcherEngine is now removed from the 
  public API.

## 0.82 - 2018-06-02

First public release, and the first version pushed to Maven Central.

### Added

- Ability to generate mocks
- Ability to stub mocks to return or throw
- Partial mocking and basic spy support
- Behaviour verification
- Argument matchers
- Add project documentation
- Setup for CI with Travis and Sonarcloud

[Unreleased]: https://github.com/roscopeco/moxy/compare/v0.90.1...develop
[0.90.0]: https://github.com/roscopeco/moxy/compare/v0.90.0...v0.90.1
[0.90.0]: https://github.com/roscopeco/moxy/compare/v0.88.0...v0.90.0
[0.88.1]: https://github.com/roscopeco/moxy/compare/v0.88.0...v0.88.1
[0.88.0]: https://github.com/roscopeco/moxy/compare/v0.86.0...v0.88.0
[0.86.0]: https://github.com/roscopeco/moxy/compare/v0.84...v0.86.0
[0.84]: https://github.com/roscopeco/moxy/compare/v0.82...v0.84
