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

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElementConstants;

/**
 * The interface for the root element to store the constants.
 */

public interface IModuleModel {

	/**
	 * Identifier of the parameter slot.
	 */

	public static final int PARAMETER_SLOT = 1;

	/**
	 * Identifier of the data source slot.
	 */

	public static final int DATA_SOURCE_SLOT = 2;

	/**
	 * Identifier of the data set slot.
	 */

	public static final int DATA_SET_SLOT = 3;

	/**
	 * Identifier of the master page slot.
	 */

	public static final int PAGE_SLOT = 4;

	/**
	 * Identifier of the component slot.
	 */

	public static final int COMPONENT_SLOT = 5;

	/**
	 * Name of the author property.
	 */

	public static final String AUTHOR_PROP = "author"; //$NON-NLS-1$

	/**
	 * Name of the "base" property.
	 */

	public static final String BASE_PROP = "base"; //$NON-NLS-1$

	/**
	 * Name of the default units property. These are the units assumed for any
	 * dimension property that does not explicitly specify units.
	 */

	public static final String UNITS_PROP = "units"; //$NON-NLS-1$

	/**
	 * Name of the help guide property.
	 */

	public static final String HELP_GUIDE_PROP = "helpGuide"; //$NON-NLS-1$

	/**
	 * Name of the created by property. Gives the name of the tool that created the
	 * design.
	 */

	public static final String CREATED_BY_PROP = "createdBy"; //$NON-NLS-1$

	/**
	 * Name of the title property.
	 */

	public static final String TITLE_PROP = "title"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the message ID for the localized report
	 * title.
	 */

	public static final String TITLE_ID_PROP = "titleID"; //$NON-NLS-1$

	/**
	 * Name of the description property.
	 */

	public static final String DESCRIPTION_PROP = "description"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the message ID for the localized report
	 * description.
	 */

	public static final String DESCRIPTION_ID_PROP = "descriptionID"; //$NON-NLS-1$

	/**
	 * Name of the custom color palette property.
	 */

	public static final String COLOR_PALETTE_PROP = "colorPalette"; //$NON-NLS-1$

	/**
	 * Name of the custom config variables property.
	 */

	public static final String CONFIG_VARS_PROP = "configVars"; //$NON-NLS-1$

	/**
	 * Name of the embedded images property.
	 */

	public static final String IMAGES_PROP = "images"; //$NON-NLS-1$

	/**
	 * Name of the property that gives some include libraries.
	 */

	public static final String LIBRARIES_PROP = "libraries"; //$NON-NLS-1$

	/**
	 * Name of the property that gives some include scripts.
	 */

	public static final String INCLUDE_SCRIPTS_PROP = "includeScripts"; //$NON-NLS-1$

	/**
	 * Base name of the included resource bundle file.
	 */

	public static final String INCLUDE_RESOURCE_PROP = "includeResource"; //$NON-NLS-1$

	/**
	 * Name of the method called when the report starts executing in the BIRT Report
	 * Engine.
	 */

	public static final String INITIALIZE_METHOD = "initialize"; //$NON-NLS-1$

	/**
	 * Name of the theme property.
	 */

	public static final String THEME_PROP = ISupportThemeElementConstants.THEME_PROP;

	/**
	 * Name of the "propertyBindings" property.
	 */

	public static final String PROPERTY_BINDINGS_PROP = "propertyBindings"; //$NON-NLS-1$

	/**
	 * Name of the "scriptLibs" property.
	 */

	public static final String SCRIPTLIBS_PROP = "scriptLibs"; //$NON-NLS-1$

	/**
	 * Name of the "subject" property.Typically, the subject will be represented
	 * using keywords, key phrases, or classification codes.
	 */
	public static final String SUBJECT_PROP = "subject"; //$NON-NLS-1$
}
