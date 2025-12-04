# GitHub Copilot Instructions for Eclipse BIRT

## Project Overview

Eclipse BIRT (Business Intelligence and Reporting Tools) is an open source Eclipse Platform-based reporting system that integrates with Java/Java EE applications to produce compelling reports. The project is built using Apache Maven with Tycho plugins.

## Project Structure

- **`build/`** - Build configuration and release engineering components
- **`chart/`** - Chart generation and visualization components
- **`core/`** - Core BIRT framework and APIs
- **`data/`** - Data access and manipulation components
- **`docs/`** - Project documentation
- **`engine/`** - Report engine components
- **`features/`** - Eclipse feature definitions
- **`model/`** - Report model and design API
- **`nl/`** - Internationalization and localization resources
- **`testsuites/`** - Test suites and test data
- **`UI/`** - User interface components for report design
- **`viewer/`** - Report viewer components
- **`xtab/`** - Cross-tab (pivot table) components

## Build Environment Requirements

- **JDK**: 21 (LTS)
- **Maven**: 3.9.11
- **Build System**: Apache Maven with Tycho for Eclipse plugin builds

## Building the Project

### Build without tests
```bash
mvn clean verify -DskipTests=true
```

### Build with tests
```bash
mvn clean verify -DskipTests=false
```

### Package build
```bash
mvn package -DskipTests
```

## Testing

- Tests are located in the `testsuites/` directory
- Run all tests with: `mvn clean verify -DskipTests=false`
- The project uses JUnit 4.13.2 for testing
- Always run tests before finalizing changes to ensure no regressions

## Code Style and Conventions

### License Headers
- All source files must include the Eclipse Public License 2.0 header
- Use SPDX-License-Identifier: EPL-2.0
- Example format:
  ```java
  /*******************************************************************************
   * Copyright (c) YYYY Contributors to the Eclipse Foundation
   *
   * This program and the accompanying materials are made available under the
   * terms of the Eclipse Public License 2.0 which is available at
   * https://www.eclipse.org/legal/epl-2.0/.
   *
   * SPDX-License-Identifier: EPL-2.0
   *
   * Contributors:
   *   See git history
   *******************************************************************************/
  ```

### Java Conventions
- Follow Eclipse Java code conventions
- Use meaningful variable and method names
- Add Javadoc comments for public APIs
- Maintain backward compatibility when possible
- Target Java 21 features where appropriate

### XML/POM Files
- Maintain consistent indentation (tabs for XML)
- Keep dependencies organized and up-to-date
- Document any new Maven properties or plugin configurations

## Contribution Requirements

### Eclipse Contributor Agreement
- Contributors must electronically sign the Eclipse Contributor Agreement (ECA)
- Non-committers must have an Eclipse Foundation account
- See: http://www.eclipse.org/legal/ECA.php

### Commit Guidelines
- Write clear, descriptive commit messages
- Reference issue numbers when applicable
- Keep commits focused and atomic

## Key Dependencies and Technologies

- **Eclipse Platform**: Core framework
- **Apache Maven**: Build tool
- **Tycho**: Maven plugins for Eclipse
- **JUnit**: Testing framework
- **Jetty**: For web components (Tomcat 9 for runtime)

## Common Tasks

### Adding New Features
1. Create new components in appropriate module directories
2. Follow existing package structure conventions
3. Add necessary Maven/POM configurations
4. Include unit tests in corresponding test modules
5. Update documentation if adding public APIs

### Bug Fixes
1. Identify the affected module(s)
2. Write or update tests to reproduce the bug
3. Make minimal, focused changes
4. Verify tests pass
5. Check for potential side effects in related components

### Documentation Updates
1. Technical docs go in `docs/` directory
2. Update README.md for user-facing changes
3. Update CONTRIBUTING.md for process changes
4. Include Javadoc updates for API changes

## Runtime Environment

- **Supported JDK**: Java 21 (LTS)
- **Tomcat**: 9.0.7x through 9.0.10x (note: Tomcat 10 & 11 not supported)
- **Important**: BIRT 4.21+ requires JVM argument: `-add-opens=java.base/java.net=ALL-UNNAMED`

## Important Notes

- This is a multi-module Maven project with 279 pom.xml files
- Build times can be significant; be patient with CI/CD pipelines
- The project uses Tycho for Eclipse plugin builds, which has specific requirements
- Changes should maintain backward compatibility where possible
- Consider performance impact, especially in core engine components
- Security is critical - always validate input and handle sensitive data properly

## Useful Links

- Project site: https://eclipse.org/birt
- CI: https://ci.eclipse.org/birt/job/build/
- Issues: https://github.com/eclipse-birt/birt/issues
- Discussions: https://github.com/eclipse-birt/birt/discussions
- Mailing list: https://dev.eclipse.org/mailman/listinfo/birt-dev

## Making Changes

1. **Understand the scope**: BIRT is a large, complex project. Take time to understand the module you're working in.
2. **Test thoroughly**: Run relevant tests locally before pushing changes.
3. **Minimal changes**: Make the smallest possible changes to achieve the goal.
4. **Check dependencies**: Be aware of module dependencies when making changes.
5. **Document**: Update relevant documentation for user-facing or API changes.
6. **Security**: Always validate security implications of changes.

## When in Doubt

- Check existing code patterns in the same module
- Review the CONTRIBUTING.md file
- Look at recent similar changes in git history
- Consult Eclipse BIRT documentation: https://eclipse.org/birt
