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
	 * Name of the default units property. These are the units assumed for any
	 * dimension property that does not explicitly specify units.
	 */

	public static final String UNITS_PROP = "units"; //$NON-NLS-1$

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

	// Design slots
	// See constants defined in the module class.

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
