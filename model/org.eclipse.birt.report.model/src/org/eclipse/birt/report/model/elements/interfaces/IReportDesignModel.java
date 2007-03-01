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
	 * Name of the refresh rate property.
	 */

	public static final String REFRESH_RATE_PROP = "refreshRate"; //$NON-NLS-1$

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
	 * Name of the method called before starting a presentation time action.
	 */

	public static final String BEFORE_RENDER_METHOD = "beforeRender"; //$NON-NLS-1$

	/**
	 * Name of the method called after starting a presentation time action.
	 */

	public static final String AFTER_RENDER_METHOD = "afterRender"; //$NON-NLS-1$

	/**
	 * Name of the property to store report design icon/thumbnail file path.
	 */

	public static final String ICON_FILE_PROP = "iconFile"; //$NON-NLS-1$

	/**
	 * Name of the property to store the cheet sheet file name.
	 */

	public static final String CHEAT_SHEET_PROP = "cheatSheet"; //$NON-NLS-1$

	/**
	 * Name of the property to store the thumbnail image for the design or
	 * template.
	 */

	public static final String THUMBNAIL_PROP = "thumbnail"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the layout format of this report
	 * design.
	 */
	public static final String LAYOUT_PREFERENCE_PROP = "layoutPreference"; //$NON-NLS-1$

	/**
	 * Encoding mode for the thumbnail image.
	 */

	public static final String CHARSET = "8859_1"; //$NON-NLS-1$

	// Design slots
	// See constants defined in the module class.

	/**
	 * Identifier of the slot that holds styles.
	 */

	public static final int STYLE_SLOT = 0;

	/**
	 * Identifier of the body slot that contains the report sections.
	 */

	public static final int BODY_SLOT = 6;

	/**
	 * Identifier of the scratch pad slot.
	 */

	public static final int SCRATCH_PAD_SLOT = 7;

	/**
	 * Identifier of the template parameter definition slot.
	 */

	public static final int TEMPLATE_PARAMETER_DEFINITION_SLOT = 8;

	/**
	 * Identifier of the slot that holds a collections of cube elements.
	 */

	public static final int CUBE_SLOT = 9;

	/**
	 * Number of slots in the report design element.
	 */

	public static final int SLOT_COUNT = 10;

}
