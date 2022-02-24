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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.ibm.icu.util.ULocale;

/**
 * Provide the way to do some setting about the module.
 */

public class ModuleOption implements IModuleOption {

	/**
	 * Maps to store the key/value pairs.
	 */

	protected Map<String, Object> options = new HashMap<String, Object>();

	/**
	 * Default constructor.
	 */

	public ModuleOption() {

	}

	/**
	 * Constructs the module options with mapping of the option settings.
	 * 
	 * @param options the option settings to add
	 */

	public ModuleOption(Map options) {
		if (options != null && !options.isEmpty())
			this.options.putAll(options);
	}

	/**
	 * Determines whether to do some semantic checks when opening a module.
	 * 
	 * @return true if user wants to do the checks, otherwise false
	 */

	public boolean useSemanticCheck() {
		Object semanticCheck = options.get(PARSER_SEMANTIC_CHECK_KEY);
		if (semanticCheck != null)
			return ((Boolean) semanticCheck).booleanValue();
		return true;
	}

	/**
	 * Sets the semantic check control status. True if user wants to do the semantic
	 * checks when opening a module, otherwise false.
	 * 
	 * @param useSemanticCheck the control status
	 */

	public void setSemanticCheck(boolean useSemanticCheck) {
		options.put(PARSER_SEMANTIC_CHECK_KEY, Boolean.valueOf(useSemanticCheck));
	}

	/**
	 * Gets the resource folder.
	 * 
	 * @return the resource folder
	 */

	public String getResourceFolder() {
		return (String) options.get(RESOURCE_FOLDER_KEY);
	}

	/**
	 * Sets the resource folder
	 * 
	 * @param resourceFolder the resource folder to set
	 */

	public void setResourceFolder(String resourceFolder) {
		if (resourceFolder != null)
			options.put(RESOURCE_FOLDER_KEY, resourceFolder);
	}

	/**
	 * Sets an option of this setting.
	 * 
	 * @param key   the option key
	 * @param value the option value
	 */

	public void setProperty(String key, Object value) {
		options.put(key, value);
	}

	/**
	 * Gets the value in this setting.
	 * 
	 * @param key the key to search
	 * @return the value in this setting if found, otherwise <code>null</code>
	 */

	public Object getProperty(String key) {
		return options.get(key);
	}

	/**
	 * Determines whether to mark line number of element when opening/saving a
	 * module.
	 * 
	 * Note: if user switch semantic check off, this method will also return false.
	 * 
	 * @return true if user wants to mark line number of element and user switch
	 *         semantic check on, otherwise false
	 */

	public boolean markLineNumber() {

		Object markLineNumber = options.get(MARK_LINE_NUMBER_KEY);
		if (markLineNumber != null)
			return ((Boolean) markLineNumber).booleanValue();

		return true;
	}

	/**
	 * Sets the marking line number control status. True if user wants to mark the
	 * line number of the element in xml source when opening/saving a module,
	 * otherwise false.
	 * 
	 * @param markLineNumber the control status
	 */

	public void setMarkLineNumber(boolean markLineNumber) {
		options.put(MARK_LINE_NUMBER_KEY, Boolean.valueOf(markLineNumber));
	}

	/**
	 * Gets the resource locator.
	 * 
	 * @return the resource locator
	 */

	public IResourceLocator getResourceLocator() {
		return (IResourceLocator) options.get(RESOURCE_LOCATOR_KEY);
	}

	/**
	 * Sets the resource locator.
	 * 
	 * @param locator the resource locator to set
	 */

	public void setResourceLocator(IResourceLocator locator) {
		if (locator != null)
			options.put(RESOURCE_LOCATOR_KEY, locator);
	}

	/**
	 * Gets the locale.
	 * 
	 * @return the locale
	 */

	public ULocale getLocale() {
		Object locale = options.get(LOCALE_KEY);
		if (locale instanceof ULocale)
			return (ULocale) locale;
		else if (locale instanceof Locale) {
			return ULocale.forLocale(((Locale) locale));
		}
		return null;
	}

	/**
	 * Sets the locale.
	 * 
	 * @param locale the locale
	 */

	public void setLocale(ULocale locale) {
		if (locale != null)
			options.put(LOCALE_KEY, locale);
	}

	/**
	 * Sets the flag that is used to update the design to the latest version when
	 * creates.
	 * 
	 * @param toSet
	 */

	public void setToLatestVersion(boolean toSet) {
		if (toSet)
			options.put(TO_LATEST_VERSION, Boolean.TRUE);
	}

	/**
	 * Returns the flag that indicates whether the report should be updated to the
	 * latest version when creates.
	 * 
	 * @return
	 */

	public boolean toLatestVersion() {
		Object retValue = options.get(TO_LATEST_VERSION);
		if (retValue == null)
			return false;

		if (!(retValue instanceof Boolean))
			return false;

		return ((Boolean) retValue).booleanValue();
	}

	/**
	 * Returns the copy of the current options.
	 * 
	 * @return the copy of the current options
	 * 
	 * @throws CloneNotSupportedException
	 */

	public Object copy() throws CloneNotSupportedException {
		ModuleOption obj = new ModuleOption();
		obj.options = new HashMap();
		obj.options.putAll(options);
		return obj;
	}

	/**
	 * Returns a read-only map for all options.
	 * 
	 * @return the options
	 */
	public Map getOptions() {
		return Collections.unmodifiableMap(options);
	}

	/**
	 *
	 * @param options
	 */
	public void setOptions(Map options) {
		if (options == null || options.isEmpty())
			return;
		this.options.putAll(options);
	}

	/**
	 * Clears all options for non-primitive values.
	 * 
	 * @since 4.7
	 */
	public void close() {
		Iterator<Map.Entry<String, Object>> iterator = options.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = iterator.next();
			if (!isPrimitiveType(entry.getValue())) {
				iterator.remove();
			}
		}
	}

	private boolean isPrimitiveType(Object obj) {
		// check if instance of ULocale or class name is like
		// java.lang.String,Integer,Long
		return obj == null || (obj instanceof ULocale) || obj.getClass().getName().startsWith("java.lang."); //$NON-NLS-1$
	}

	/**
	 * return whether support unknown version
	 * 
	 * @return
	 */
	public boolean isSupportedUnknownVersion() {
		Object isSupportedUnknownVersion = options.get(SUPPORTED_UNKNOWN_VERSION_KEY);
		if (isSupportedUnknownVersion != null && isSupportedUnknownVersion instanceof Boolean
				&& (Boolean) isSupportedUnknownVersion) {
			return true;
		}
		return false;
	}

	/**
	 * set whether support unknown version
	 * 
	 * @param isSupportedUnknownVersion
	 */
	public void setSupportedUnknownVersion(boolean isSupportedUnknownVersion) {
		options.put(SUPPORTED_UNKNOWN_VERSION_KEY, isSupportedUnknownVersion);
	}
}
