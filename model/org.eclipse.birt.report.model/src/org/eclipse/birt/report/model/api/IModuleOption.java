/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

/**
 * Implements to define the keys to do some setting in a module.
 */

public interface IModuleOption {

	/**
	 * Key to control whether to call semantic-check when opening a module. True if
	 * user wants to do some semantic checks about the module when opening it;
	 * otherwise false.
	 */

	static final String PARSER_SEMANTIC_CHECK_KEY = "semanticCheck"; //$NON-NLS-1$

	/**
	 * Key to set the resource folder of the module.
	 */

	static final String RESOURCE_FOLDER_KEY = "resourceFolder"; //$NON-NLS-1$

	/**
	 * Key to indicate the resource locator of the module.
	 */

	static final String RESOURCE_LOCATOR_KEY = "resourceLocator"; //$NON-NLS-1$

	/**
	 * Key to control whether to mark line number of the element in xml source. True
	 * if user wants to mark them during parsing and saving, false otherwise.
	 * Default value is true.
	 */

	static final String MARK_LINE_NUMBER_KEY = "markLineNumber"; //$NON-NLS-1$

	/**
	 * whether support unknown version if so, 1, allow run unknown version
	 * design/document. 2, ignore all the errors related with unknown tag/unknown
	 * property and log out a warning. 3, if a design contains warnings for unknown
	 * version, ignore the waring and continue run/render. default value is "false"
	 */
	static final String SUPPORTED_UNKNOWN_VERSION_KEY = "supportedUnknownVersion"; //$NON-NLS-1$

	/**
	 * Key to indicate the locale of the module.
	 */

	static final String LOCALE_KEY = "locale"; //$NON-NLS-1$

	/**
	 * Key to indicate the created-by information of the module.
	 */

	String CREATED_BY_KEY = "createdBy"; //$NON-NLS-1$

	/**
	 * Key to indicate that the created design file is the simplest and blank, which
	 * contains nothing.
	 */
	String BLANK_CREATION_KEY = "blankCreation"; //$NON-NLS-1$

	/**
	 * Key to indicate that the design file will be simply parsed to read only that
	 * simple property for the root element(report design or library) rather than
	 * the whole design tree. The result report/library will be read-only and can
	 * not do any changes.
	 */
	String READ_ONLY_MODULE_PROPERTIES = "readOnlyModuleProperties"; //$NON-NLS-1$

	/**
	 * Key to indicate that the design should be update to the latest report version
	 * when creates.
	 */

	String TO_LATEST_VERSION = "toLatestVersion"; //$NON-NLS-1$
}
