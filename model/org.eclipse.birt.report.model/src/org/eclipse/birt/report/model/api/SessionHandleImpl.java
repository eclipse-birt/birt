/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.LibraryChangeEvent;
import org.eclipse.birt.report.model.api.command.ResourceChangeEvent;
import org.eclipse.birt.report.model.api.core.IResourceChangeListener;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Style;

import com.ibm.icu.util.ULocale;

/**
 * Represents the design state -- a session for a user. In the Eclipse
 * environment, this represents the set of open designs. In the web environment,
 * this represents open designs and locale for the session. A session has a set
 * of default values for style properties and a default unit. The session also
 * has methods to create and open designs.
 * <p>
 * A session can also provides the mechanism for specified file searching
 * algorithm. After get an new instance of SessionHandle, an algorithm of how to
 * search a file should be set by calling
 * <code>{@link #setResourceLocator(IResourceLocator)}</code> if the default
 * resource locator is not the expected one.
 * <p>
 * The default resource locator will search in the folder where the design file
 * is located.
 *
 * @see org.eclipse.birt.report.model.util.ResourceLocatorImpl
 * @see org.eclipse.birt.report.model.core.DesignSession
 */

class SessionHandleImpl {

	/**
	 * The implementation of the design session.
	 */

	protected DesignSession session;

	/**
	 * Sets resource path.
	 *
	 * @param resourcePath the resource path to set. It must be an absolute path
	 *                     based on file system and must present a dictory.
	 */

	public static void setBirtResourcePath(String resourcePath) {
		DesignSession.setResourcePath(resourcePath);
	}

	/**
	 * Gets resource path. {@link #setBirtResourcePath(String)}
	 *
	 * @return the resource path set in the session
	 */

	public static String getBirtResourcePath() {
		return DesignSession.getResourcePath();
	}

	/**
	 * Constructs a handle for the session with the given locale.
	 *
	 * @param locale the user's locale. If null, then the system locale is assumed.
	 *
	 * @deprecated to use ICU4J, this method is replaced by: SessionHandle(ULocale
	 *             locale)
	 */

	@Deprecated
	public SessionHandleImpl(Locale locale) {
		ULocale uLocale = ULocale.forLocale(locale);
		session = new DesignSession(uLocale);
	}

	/**
	 * Constructs a handle for the session with the given locale.
	 *
	 * @param locale the user's locale which is <code>ULocale</code>. If null, then
	 *               the system locale is assumed.
	 */

	public SessionHandleImpl(ULocale locale) {
		session = new DesignSession(locale);
	}

	/**
	 * Activates this session within a thread. Used in the web environment to
	 * associate this session with a message thread or that the client user startup
	 * a new thread. Required to allow the thread to access localized messages.
	 */

	public void activate() {
		session.activate();
	}

	/**
	 * Opens a module regardless of the module type(library or report design).
	 * design.
	 *
	 * @param fileName name of the file to open.
	 * @param is       stream to read the design
	 * @return handle to the report design
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public ModuleHandle openModule(String fileName, InputStream is) throws DesignFileException {
		Module module = session.openModule(fileName, is);
		return module.getModuleHandle();
	}

	/**
	 * Opens a module regardless of the module type(library or report design).
	 * design.
	 *
	 * @param fileName name of the file to open.
	 * @param is       stream to read the design
	 * @param options  the options set for this module
	 * @return handle to the report design
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public ModuleHandle openModule(String fileName, InputStream is, ModuleOption options) throws DesignFileException {
		Module module = session.openModule(fileName, is, options);
		return module.getModuleHandle();
	}

	/**
	 * Opens a module regardless of the module type(library or report design).
	 * design.
	 *
	 * @param fileName name of the file to open.
	 * @return handle to the report design
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public ModuleHandle openModule(String fileName) throws DesignFileException {
		Module module = session.openModule(fileName);
		return module.getModuleHandle();
	}

	/**
	 * Opens a module regardless of the module type(library or report design).
	 * design.
	 *
	 * @param fileName name of the file to open.
	 * @param options  the options set for this module
	 * @return handle to the report design
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public ModuleHandle openModule(String fileName, ModuleOption options) throws DesignFileException {
		Module module = session.openModule(fileName, options);
		return module.getModuleHandle();
	}

	/**
	 * Opens a design with the given the file name.
	 *
	 * @param fileName name of the file to open. It may contain the
	 *                 relative/absolute path information. This name must include
	 *                 the file name with the filename extension.
	 *
	 * @return handle to the report design
	 *
	 * @throws DesignFileException if the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public ReportDesignHandle openDesign(String fileName) throws DesignFileException {
		return session.openDesign(fileName).handle();
	}

	/**
	 * Opens a design with the given the file name.
	 *
	 * @param fileName name of the file to open. It may contain the
	 *                 relative/absolute path information. This name must include
	 *                 the file name with the filename extension.
	 * @param options  the options set for this module
	 *
	 * @return handle to the report design
	 *
	 * @throws DesignFileException if the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public ReportDesignHandle openDesign(String fileName, ModuleOption options) throws DesignFileException {
		return session.openDesign(fileName, options).handle();
	}

	/**
	 * Opens a design given a stream to the design and the the file name of the
	 * design.
	 *
	 * @param fileName name of the file to open. If <code>null</code>, the design
	 *                 will be treated as a new design, and will be saved to a
	 *                 different file. If not <code>null</code>, it may contain the
	 *                 relative/absolute path information. This name must include
	 *                 the file name with the filename extension.
	 * @param is       stream to read the design
	 * @return handle to the report design
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public ReportDesignHandle openDesign(String fileName, InputStream is) throws DesignFileException {
		return session.openDesign(fileName, is).handle();
	}

	/**
	 * Opens a design given a stream to the design and the the file name of the
	 * design.
	 *
	 * @param fileName name of the file to open. If <code>null</code>, the design
	 *                 will be treated as a new design, and will be saved to a
	 *                 different file. If not <code>null</code>, it may contain the
	 *                 relative/absolute path information. This name must include
	 *                 the file name with the filename extension.
	 * @param is       stream to read the design
	 * @param options  options set for this module
	 * @return handle to the report design
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public ReportDesignHandle openDesign(String fileName, InputStream is, ModuleOption options)
			throws DesignFileException {
		return session.openDesign(fileName, is, options).handle();
	}

	/**
	 * Opens a design given a stream to the design and the the file name of the
	 * design. *
	 *
	 * @param systemId the uri where to find the relative sources for the report.
	 *                 This url is treated as an absolute directory.
	 * @param is       the input stream to read the design
	 *
	 * @return handle to the report design
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public ReportDesignHandle openDesign(URL systemId, InputStream is) throws DesignFileException {
		return session.openDesign(systemId, is).handle();
	}

	/**
	 * Opens a design given a stream to the design and the the file name of the
	 * design. *
	 *
	 * @param systemId the uri where to find the relative sources for the report.
	 *                 This url is treated as an absolute directory.
	 * @param is       the input stream to read the design
	 * @param options  the options set for this module
	 *
	 * @return handle to the report design
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public ReportDesignHandle openDesign(URL systemId, InputStream is, ModuleOption options)
			throws DesignFileException {
		return session.openDesign(systemId, is, options).handle();
	}

	/**
	 * Opens a library with the given the file name.
	 *
	 * @param fileName name of the file to open. This name must include the file
	 *                 name with the filename extension.
	 * @return handle to the library
	 *
	 * @throws DesignFileException if the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public LibraryHandle openLibrary(String fileName) throws DesignFileException {
		return session.openLibrary(fileName).handle();
	}

	/**
	 * Opens a library with the given the file name.
	 *
	 * @param fileName name of the file to open. This name must include the file
	 *                 name with the filename extension.
	 * @param options  the options set for this module
	 * @return handle to the library
	 *
	 * @throws DesignFileException if the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public LibraryHandle openLibrary(String fileName, ModuleOption options) throws DesignFileException {
		return session.openLibrary(fileName, options).handle();
	}

	/**
	 * Opens a library given a stream to the design and the the file name of the
	 * design.
	 *
	 * @param fileName name of the file to open. If <code>null</code>, the library
	 *                 will be treated as a new library, and will be saved to a
	 *                 different file. If not <code>null</code>, it may contain the
	 *                 relative/absolute path information. This name must include
	 *                 the file name with the filename extension.
	 * @param is       the stream to read the library
	 * @return the library instance
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public LibraryHandle openLibrary(String fileName, InputStream is) throws DesignFileException {
		return session.openLibrary(fileName, is).handle();
	}

	/**
	 * Opens a library given a stream to the design and the the file name of the
	 * design.
	 *
	 * @param fileName name of the file to open. If <code>null</code>, the library
	 *                 will be treated as a new library, and will be saved to a
	 *                 different file. If not <code>null</code>, it may contain the
	 *                 relative/absolute path information. This name must include
	 *                 the file name with the filename extension.
	 * @param is       the stream to read the library
	 * @param options  the options set for this module
	 * @return the library instance
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public LibraryHandle openLibrary(String fileName, InputStream is, ModuleOption options) throws DesignFileException {
		return session.openLibrary(fileName, is, options).handle();
	}

	/**
	 * Opens a library with the given the file name.
	 *
	 * @param systemId the uri where to find the relative sources for the library.
	 *                 This url is treated as an absolute directory.
	 * @param is       the input stream
	 * @return handle to the library
	 *
	 * @throws DesignFileException if the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public LibraryHandle openLibrary(URL systemId, InputStream is) throws DesignFileException {
		return session.openLibrary(systemId, is).handle();
	}

	/**
	 * Opens a library with the given the file name.
	 *
	 * @param systemId the uri where to find the relative sources for the library.
	 *                 This url is treated as an absolute directory.
	 * @param is       the input stream
	 * @param options  the options set for this module
	 * @return handle to the library
	 *
	 * @throws DesignFileException if the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public LibraryHandle openLibrary(URL systemId, InputStream is, ModuleOption options) throws DesignFileException {
		return session.openLibrary(systemId, is, options).handle();
	}

	/**
	 * Creates a new design based on a file name.
	 *
	 * @param fileName file name.
	 * @return A handle to the report design.
	 */

	public ReportDesignHandle createDesign(String fileName) {
		return session.createDesign(fileName, null).handle();
	}

	/**
	 * Creates a new design based on the file name and the module options.
	 *
	 * @param fileName
	 * @param options
	 * @return new report design handle.
	 */
	public ReportDesignHandle createDesign(String fileName, ModuleOption options) {
		return session.createDesign(fileName, options).handle();
	}

	/**
	 * Creates a new design based on a template file.
	 *
	 * @param templateDesignName The name of the template for the design.
	 * @return A handle to the report design.
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public ReportDesignHandle createDesignFromTemplate(String templateDesignName) throws DesignFileException {
		return session.createDesignFromTemplate(templateDesignName).handle();
	}

	/**
	 * Creates a new design based a given template file name and input stream.
	 *
	 * @param templateDesignName The name of the template for the design.
	 * @return A handle to the report design.
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public ReportDesignHandle createDesignFromTemplate(String templateDesignName, InputStream is)
			throws DesignFileException {
		return session.createDesignFromTemplate(templateDesignName, is).handle();
	}

	/**
	 * Creates a new library based on a template file.
	 *
	 * @param templateLibraryName The name of the template for the library.
	 * @return A handle to the report library.
	 * @throws DesignFileException If the file is not found, or the file contains
	 *                             fatal errors.
	 */

	public LibraryHandle createLibraryFromTemplate(String templateLibraryName) throws DesignFileException {
		return session.createLibraryFromTemplate(templateLibraryName).handle();
	}

	/**
	 * Creates a new empty design. This new design is not based on a template.
	 *
	 * @return the handle of the new report design.
	 */

	public ReportDesignHandle createDesign() {
		return session.createDesign(null, null).handle();
	}

	/**
	 * Creates a new empty library.
	 *
	 * @return the handle of the new library.
	 */

	public LibraryHandle createLibrary() {
		return session.createLibrary().handle();
	}

	/**
	 * Saves all designs and librariesthat need a save.
	 *
	 * @throws IOException if a save error occurs
	 */

	public void saveAll() throws IOException {
		Iterator<Module> iter = session.getModuleIterator();
		while (iter.hasNext()) {
			Module module = iter.next();
			ModuleHandle handle = (ModuleHandle) module.getHandle(module);
			if (handle.needsSave()) {
				handle.save();
			}
		}
	}

	/**
	 * Closes all open designs and libraires.
	 *
	 * @param save <code>true</code> if designs are to be saved before closing
	 * @throws IOException if a save error occurs
	 */

	public void closeAll(boolean save) throws IOException {
		Iterator<Module> iter = session.getModuleIterator();
		while (iter.hasNext()) {
			Module module = iter.next();
			ModuleHandle handle = (ModuleHandle) module.getHandle(module);
			if (save && handle.needsSave()) {
				handle.save();
			}
			handle.close();
		}
	}

	/**
	 * Sets the units to be used by the application. These units are independent of
	 * those set for the design. The application and design can use the same units,
	 * or different units. The application units are those used when getting and
	 * setting dimension properties using double (float) values. The possible values
	 * are defined in <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>UNITS_IN</code></li>
	 * <li><code>UNITS_CM</code></li>
	 * <li><code>
	 * UNITS_MM</code></li>
	 * <li><code>UNITS_PT</code></li>
	 * <li><code>UNITS_PC
	 * </code></li>
	 * </ul>
	 *
	 * @param units the units to set for the session -- application
	 * @throws PropertyValueException if <code>units</code> is not one of the above
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.DimensionValue
	 */

	public void setApplicationUnits(String units) throws PropertyValueException {
		session.setUnits(units);
	}

	/**
	 * Returns the current session (application) units. The return values are
	 * defined in <code>DesignChoiceConstants</code> and is one of:
	 *
	 * <ul>
	 * <li><code>UNITS_IN</code></li>
	 * <li><code>UNITS_CM</code></li>
	 * <li><code>
	 * UNITS_MM</code></li>
	 * <li><code>UNITS_PT</code></li>
	 * <li><code>UNITS_PC
	 * </code></li>
	 * </ul>
	 *
	 * @return the application units
	 *
	 * @see #setApplicationUnits
	 */

	public String getApplicationUnits() {
		return session.getUnits();
	}

	/**
	 * Sets the rgb color display preference to be used by the application. If the
	 * preference is not set the application will use default CSS_ABSOLUTE_FORMAT
	 * like "RGB(255,0,0)" for a rgb color display value. The rgbFormat argument is
	 * an integer value that may be the following constants defined in
	 * <code>ColorUtil</code>:
	 * <ul>
	 * <li><code>INT_FORMAT</code>
	 * <li><code>HTML_FORMAT</code>
	 * <li><code>
	 * JAVA_FORMAT</code>
	 * <li><code>CSS_ABSOLUTE_FORMAT</code>
	 * <li><code>
	 * CSS_RELATIVE_FORMAT</code>
	 * </ul>
	 *
	 * @param rgbFormat the rgb color display preference to set.
	 *
	 * @throws PropertyValueException if <code>rgbFormat</code> is not one of the
	 *                                above.
	 *
	 * @see org.eclipse.birt.report.model.metadata.ColorPropertyType
	 */

	public void setColorFormat(int rgbFormat) throws PropertyValueException {
		session.setColorFormat(rgbFormat);
	}

	/**
	 * Returns the current application rgb color display preference. The return is
	 * one of the following constants defined in <code>ColorUtil</code>:
	 *
	 * <ul>
	 * <li><code>INT_FORMAT</code>
	 * <li><code>HTML_FORMAT</code>
	 * <li><code>
	 * JAVA_FORMAT</code>
	 * <li><code>CSS_ABSOLUTE_FORMAT</code>
	 * <li><code>
	 * CSS_RELATIVE_FORMAT</code>
	 * </ul>
	 *
	 * @return application rgb color display preference
	 *
	 * @see #setColorFormat(int)
	 */

	public int getColorFormat() {
		return session.getColorFormat();
	}

	/**
	 * Sets the specified default value of style property.
	 *
	 * @param propName style property name
	 * @param value    default value to set
	 * @throws PropertyValueException if value is invalid.
	 */

	public void setDefaultValue(String propName, Object value) throws PropertyValueException {
		session.setDefaultValue(propName, value);
	}

	/**
	 * Gets the default value of the specified style property.
	 *
	 * @param propName style property name
	 * @return The default value of this style property. If the default value is not
	 *         set, return <code>null</code>.
	 */

	public Object getDefaultValue(String propName) {
		return session.getDefaultValue(propName);
	}

	/**
	 * Sets the resource locator for the specified file searching algorithm.
	 *
	 * @param locator the resource locator to be set.
	 */

	public void setResourceLocator(IResourceLocator locator) {
		// This file locator is actually set on DesignSession so the Module can
		// access it

		session.setResourceLocator(locator);
	}

	/**
	 * Returns the installed resource locator.
	 *
	 * @return the resource locator.
	 */

	public IResourceLocator getResourceLocator() {
		return session.getResourceLocator();
	}

	/**
	 * Returns the locale of the current session.
	 *
	 * @return the locale of the current session
	 * @deprecated to use ICU4J, replaced by: public ULocale getLocale()
	 */

	@Deprecated
	public Locale getLocale() {
		return Locale.getDefault();
	}

	/**
	 * Returns the locale of the current session.
	 *
	 * @return the locale of the current session, the return value is of
	 *         <code>ULocale</code>.
	 */

	public ULocale getULocale() {
		return session.getLocale();
	}

	/**
	 * Informs this session some resources is changed. Session will check all opened
	 * mudules, all interfered modules will be informed of the changes.
	 *
	 * <p>
	 * Current, only changes of library is supported.
	 *
	 * @param ev the resource change event to fire
	 */

	public void fireResourceChange(ResourceChangeEvent ev) {
		if (ev.getEventType() == NotificationEvent.LIBRARY_CHANGE_EVENT) {
			session.fireLibChange((LibraryChangeEvent) ev);
		}
	}

	/**
	 * Adds one resource change listener. The duplicate listener will not be added.
	 *
	 * @param listener the resource change listener to add
	 */

	public void addResourceChangeListener(IResourceChangeListener listener) {
		session.addResourceChangeListener(listener);
	}

	/**
	 * Removes one resource change listener. If the listener not registered, then
	 * the request is silently ignored.
	 *
	 * @param listener the resource change listener to remove
	 * @return <code>true</code> if <code>listener</code> is successfully removed.
	 *         Otherwise <code>false</code>.
	 *
	 */

	public boolean removeResourceChangeListener(IResourceChangeListener listener) {
		return session.removeResourceChangeListener(listener);
	}

	/**
	 * Gets the design session of the handle.
	 *
	 * @return the design session of the handle
	 */

	DesignSession getSession() {
		return this.session;
	}

	/**
	 * Sets the resource folder for this session.
	 *
	 * @param resourceFolder the folder to set
	 */

	public void setResourceFolder(String resourceFolder) {
		session.setResourceFolder(resourceFolder);
	}

	/**
	 * Gets the resource folder set in this session.
	 *
	 * @return the resource folder set in this session
	 */

	public String getResourceFolder() {
		return session.getResourceFolder();
	}

	/**
	 * Gets default TOC Style.Heading 1 -> Heading 10.
	 *
	 * @param name style name
	 * @return style handle which is read only.
	 */

	public StyleHandle getDefaultTOCStyle(String name) {
		List<DesignElement> result = session.getDefaultTOCStyleValue();
		if (result == null) {
			return null;
		}
		Iterator<DesignElement> iterator = result.iterator();
		while (iterator.hasNext()) {
			Style tmpStyle = (Style) iterator.next();
			if (tmpStyle.getName().equals(name)) {
				return tmpStyle.handle(tmpStyle.getRoot());
			}
		}
		return null;
	}

	/**
	 * Returns the provider instance which provides the absolute dimension value of
	 * predefined font size choice.
	 * <ul>
	 * <li><code>FONT_SIZE_XX_SMALL</code>
	 * <li><code>FONT_SIZE_X_SMALL</code>
	 * <li><code>FONT_SIZE_SMALL</code>
	 * <li><code>FONT_SIZE_MEDIUM</code>
	 * <li><code>FONT_SIZE_LARGE</code>
	 * <li><code>FONT_SIZE_X_LARGE</code>
	 * <li><code>FONT_SIZE_XX_LARGE</code>
	 * </ul>
	 *
	 * @return the instance of <code>IAbsoluteFontSizeValueProvider</code>
	 */
	public IAbsoluteFontSizeValueProvider getPredefinedFontSizeProvider() {
		return session.getPredefinedFontSizeProvider();
	}
}
