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

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElementConstants;

/**
 * The interface for the root element to store the constants.
 */

public interface IModuleModel {

	/**
	 * Identifier of the parameter slot.
	 */

	int PARAMETER_SLOT = 1;

	/**
	 * Identifier of the data source slot.
	 */

	int DATA_SOURCE_SLOT = 2;

	/**
	 * Identifier of the data set slot.
	 */

	int DATA_SET_SLOT = 3;

	/**
	 * Identifier of the master page slot.
	 */

	int PAGE_SLOT = 4;

	/**
	 * Identifier of the component slot.
	 */

	int COMPONENT_SLOT = 5;

	/**
	 * Name of the author property.
	 */

	String AUTHOR_PROP = "author"; //$NON-NLS-1$

	/**
	 * Name of the "base" property.
	 */

	String BASE_PROP = "base"; //$NON-NLS-1$

	/**
	 * Name of the default units property. These are the units assumed for any
	 * dimension property that does not explicitly specify units.
	 */

	String UNITS_PROP = "units"; //$NON-NLS-1$

	/**
	 * Name of the help guide property.
	 */

	String HELP_GUIDE_PROP = "helpGuide"; //$NON-NLS-1$

	/**
	 * Name of the created by property. Gives the name of the tool that created the
	 * design.
	 */

	String CREATED_BY_PROP = "createdBy"; //$NON-NLS-1$

	/**
	 * Name of the title property.
	 */

	String TITLE_PROP = "title"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the message ID for the localized report
	 * title.
	 */

	String TITLE_ID_PROP = "titleID"; //$NON-NLS-1$

	/**
	 * Name of the description property.
	 */

	String DESCRIPTION_PROP = "description"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the message ID for the localized report
	 * description.
	 */

	String DESCRIPTION_ID_PROP = "descriptionID"; //$NON-NLS-1$

	/**
	 * Name of the custom color palette property.
	 */

	String COLOR_PALETTE_PROP = "colorPalette"; //$NON-NLS-1$

	/**
	 * Name of the custom config variables property.
	 */

	String CONFIG_VARS_PROP = "configVars"; //$NON-NLS-1$

	/**
	 * Name of the embedded images property.
	 */

	String IMAGES_PROP = "images"; //$NON-NLS-1$

	/**
	 * Name of the property that gives some include libraries.
	 */

	String LIBRARIES_PROP = "libraries"; //$NON-NLS-1$

	/**
	 * Name of the property that gives some include scripts.
	 */

	String INCLUDE_SCRIPTS_PROP = "includeScripts"; //$NON-NLS-1$

	/**
	 * Base name of the included resource bundle file.
	 */

	String INCLUDE_RESOURCE_PROP = "includeResource"; //$NON-NLS-1$

	/**
	 * Name of the method called when the report starts executing in the BIRT Report
	 * Engine.
	 */

	String INITIALIZE_METHOD = "initialize"; //$NON-NLS-1$

	/**
	 * Name of the theme property.
	 */

	String THEME_PROP = ISupportThemeElementConstants.THEME_PROP;

	/**
	 * Name of the "propertyBindings" property.
	 */

	String PROPERTY_BINDINGS_PROP = "propertyBindings"; //$NON-NLS-1$

	/**
	 * Name of the "scriptLibs" property.
	 */

	String SCRIPTLIBS_PROP = "scriptLibs"; //$NON-NLS-1$

	/**
	 * Name of the "subject" property.Typically, the subject will be represented
	 * using keywords, key phrases, or classification codes.
	 */
	String SUBJECT_PROP = "subject"; //$NON-NLS-1$
}
