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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for report design element to store the constants.
 */
public interface IReportDesignModel
{

	/**
	 * Name of the author property.
	 */

	public static final String AUTHOR_PROP = "author"; //$NON-NLS-1$

	/**
	 * Name of the help guide property.
	 */

	public static final String HELP_GUIDE_PROP = "helpGuide"; //$NON-NLS-1$

	/**
	 * Name of the created by property. Gives the name of the tool that created
	 * the design.
	 */

	public static final String CREATED_BY_PROP = "createdBy"; //$NON-NLS-1$

	/**
	 * Name of the default units property. These are the units assumed for any
	 * dimension property that does not explicitly specify units.
	 */

	public static final String UNITS_PROP = "units"; //$NON-NLS-1$

	/**
	 * Name of the refresh rate property.
	 */

	public static final String REFRESH_RATE_PROP = "refreshRate"; //$NON-NLS-1$

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
	 * Name of the "base" property.
	 */

	public static final String BASE_PROP = "base"; //$NON-NLS-1$

	/**
	 * Base name of the linked resource bundle file.
	 */

	public static final String MSG_BASE_NAME_PROP = "msgBaseName"; //$NON-NLS-1$

	/**
	 * Name of the method called when the report starts executing in the BIRT
	 * Report Engine.
	 */

	public static final String INITIALIZE_METHOD = "initialize"; //$NON-NLS-1$

	/**
	 * Name of the method called at the start of the Factory after the
	 * initialize( ) method and before opening the report document (if any).
	 */

	public static final String BEFORE_FACTORY_METHOD = "beforeFactory"; //$NON-NLS-1$

	/**
	 * Name of the method called at the end of the Factory after closing the
	 * report document (if any). This is the last method called in the Factory.
	 */

	public static final String AFTER_FACTORY_METHOD = "afterFactory"; //$NON-NLS-1$

	/**
	 * Name of the method called just before opening the report document in the
	 * Factory.
	 */

	public static final String BEFORE_OPEN_DOC_METHOD = "beforeOpenDoc"; //$NON-NLS-1$

	/**
	 * Name of the method called just after opening the report document in the
	 * Factory.
	 */

	public static final String AFTER_OPEN_DOC_METHOD = "afterOpenDoc"; //$NON-NLS-1$

	/**
	 * Name of the method called just before closing the report document file in
	 * the Factory.
	 */

	public static final String BEFORE_CLOSE_DOC_METHOD = "beforeCloseDoc"; //$NON-NLS-1$

	/**
	 * Name of the method called just after closing the report document file in
	 * the Factory.
	 */

	public static final String AFTER_CLOSE_DOC_METHOD = "afterCloseDoc"; //$NON-NLS-1$

	/**
	 * Name of the method called before starting a presentation time action.
	 */

	public static final String BEFORE_RENDER_METHOD = "beforeRender"; //$NON-NLS-1$

	/**
	 * Name of the method called after starting a presentation time action.
	 */

	public static final String AFTER_RENDER_METHOD = "afterRender"; //$NON-NLS-1$

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

	public static final String INCLUDE_LIBRARIES = "includeLibraries"; //$NON-NLS-1$

	/**
	 * Name of the property that gives some include scripts.
	 */

	public static final String INCLUDE_SCRIPTS = "includeScripts"; //$NON-NLS-1$

	// Design slots
	// See constants defined in the RootElement class.

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
	 * Identifier of the body slot that contains the report sections.
	 */

	public static final int BODY_SLOT = 6;

	/**
	 * Identifier of the scratch pad slot.
	 */

	public static final int SCRATCH_PAD_SLOT = 7;

	/**
	 * Number of slots in the report design element.
	 */

	public static final int SLOT_COUNT = 8;

}
