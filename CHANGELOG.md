# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html)
from version 0.86 onwards.

## Experimental

These changes are currently considered experimental and are not yet ready
to merge to `develop`. They are in various branches and are slated to
make it in before 1.0.0.
  
- Java 9 Jigsaw (module) support.
  See: https://github.com/roscopeco/moxy/tree/modular-java9

## [Unreleased]

These changes are currently in the `develop` branch, and are slated
to make it into a release soon.

**Latest release is up-to-date**

## [0.86] - 2018-06-13

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

[Unreleased]: https://github.com/roscopeco/moxy/compare/v0.86...develop
[0.86]: https://github.com/roscopeco/moxy/compare/v0.84...v0.86
[0.84]: https://github.com/roscopeco/moxy/compare/v0.82...v0.84
