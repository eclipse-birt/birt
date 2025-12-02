# Jakarta EE 9 Migration Guide

This document describes the migration of BIRT from Java EE 8 (javax.*) to Jakarta EE 9 (jakarta.*) APIs.

## Overview

With the transition of Java EE to the Eclipse Foundation as Jakarta EE, the namespace for Jakarta EE APIs changed from `javax.*` to `jakarta.*` starting with Jakarta EE 9. This migration updates BIRT to use the new Jakarta EE 9+ compatible APIs.

## Changes Made

### 1. Servlet API Migration
- **Scope**: 204 servlet import statements migrated
- **Change**: `javax.servlet.*` → `jakarta.servlet.*`
- **Impact**: All servlet-related functionality including:
  - HTTP servlets and filters
  - Servlet contexts and sessions  
  - Request/response handling
  - Chart viewer components
  - Report viewer components

**Example**:
```java
// Before
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;

// After  
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
```

### 2. OLAP API Migration
- **Scope**: 229 OLAP import statements migrated + 16 source files relocated
- **Change**: `javax.olap.*` → `jakarta.olap.*`
- **Impact**: BIRT's internal OLAP implementation migrated to Jakarta namespace
- **Files Relocated**: 
  - `data/org.eclipse.birt.data/src/javax/olap/*` → `data/org.eclipse.birt.data/src/jakarta/olap/*`

**Note**: BIRT includes its own implementation of OLAP APIs, which have been migrated to the Jakarta namespace.

### 3. JNDI Migration  
- **Scope**: 11 naming import statements migrated
- **Change**: `javax.naming.*` → `jakarta.naming.*`
- **Impact**: JNDI context and naming operations

### 4. SQL DataSource Migration
- **Scope**: 5 SQL import statements migrated  
- **Change**: `javax.sql.*` → `jakarta.sql.*`
- **Impact**: DataSource and connection pooling interfaces

### 5. Build Configuration Updates
- **File**: `data/org.eclipse.birt.data/pom.xml`
- **Change**: Updated JAR inclusion path from `javax/olap/` to `jakarta/olap/`

### 6. OSGi Manifest Updates
- **Scope**: 5 MANIFEST.MF files updated
- **Files Updated**:
  - `chart/org.eclipse.birt.chart.viewer/META-INF/MANIFEST.MF`
  - `chart/org.eclipse.birt.chart.examples/META-INF/MANIFEST.MF`
  - `viewer/org.eclipse.birt.report.viewer/META-INF/MANIFEST.MF`
  - `viewer/org.eclipse.birt.report.viewer.tests/META-INF/MANIFEST.MF`
  - `core/org.eclipse.birt.core/META-INF/MANIFEST.MF`
- **Changes**: 
  - Updated `Import-Package` declarations from `javax.servlet.*` to `jakarta.servlet.*`
  - Updated servlet API versions from 2.4.0/3.1.0 to 5.0.0 (Jakarta EE 9+)
  - Updated JSP API versions from 2.0.0/2.1.0 to 3.0.0 (Jakarta EE 9+)
  - Updated `Require-Bundle` from `javax.servlet-api` to `jakarta.servlet-api`

**Example**:
```manifest
# Before
Import-Package: javax.servlet;version="3.1.0",
 javax.servlet.http;version="3.1.0"

# After  
Import-Package: jakarta.servlet;version="5.0.0",
 jakarta.servlet.http;version="5.0.0"
```

## APIs NOT Migrated

The following `javax.*` APIs remain unchanged as they are part of Java SE (not Jakarta EE):

- **javax.swing.*** - Swing GUI components (Java SE)
- **javax.imageio.*** - Image I/O APIs (Java SE)  
- **javax.print.*** - Printing APIs (Java SE)
- **javax.xml.parsers.*** - XML parsing APIs (Java SE)
- **javax.xml.transform.*** - XML transformation APIs (Java SE)

## Runtime Dependencies

Applications using BIRT after this migration must:

1. **Use Jakarta EE 9+ compatible servlet containers** such as:
   - Apache Tomcat 10+
   - Eclipse Jetty 11+
   - WildFly 22+ 
   - OpenLiberty 21+

2. **Update application dependencies** to use Jakarta versions:
   ```xml
   <!-- Replace javax.servlet-api with jakarta.servlet-api -->
   <dependency>
       <groupId>jakarta.servlet</groupId>
       <artifactId>jakarta.servlet-api</artifactId>
       <version>5.0.0</version>
       <scope>provided</scope>
   </dependency>
   ```

3. **Update web.xml** (if using XML configuration):
   ```xml
   <!-- Update namespace from javax to jakarta -->
   <web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee 
                                https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd"
            version="5.0">
   ```

## Compilation Verification

After migration, the codebase should compile successfully with:
- Jakarta EE 9+ APIs
- Java SE 11+
- Servlet containers supporting Jakarta EE 9+

## Breaking Changes

This migration introduces **breaking changes** for applications using BIRT:

1. **Applications must migrate** their own servlet code to Jakarta EE 9+
2. **Servlet container compatibility**: Only Jakarta EE 9+ compatible containers supported
3. **Custom extensions** using BIRT's OLAP APIs must update imports from `javax.olap.*` to `jakarta.olap.*`

## Migration Timeline

- **Phase 1**: ✅ Automated migration of javax.* to jakarta.* imports (this change)
- **Phase 2**: Manual testing and runtime verification  
- **Phase 3**: Documentation updates for deployment guides
- **Phase 4**: Integration testing with Jakarta EE 9+ containers

## Support

For issues related to this migration:
1. Check servlet container compatibility with Jakarta EE 9+
2. Verify all custom code has been migrated to Jakarta APIs
3. Review application dependencies for Jakarta EE compatibility

---

**Migration completed**: September 27, 2025  
**BIRT Version**: 4.22.0+  
**Target Jakarta EE Version**: 9.0+