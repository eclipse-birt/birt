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

package org.eclipse.birt.report.model.api.metadata;

/**
 * Provides constants used in the meta-data package.
 */

public interface MetaDataConstants {
	/**
	 * Internal name of the style element type.
	 */

	public static final String STYLE_NAME = "Style"; //$NON-NLS-1$

	/**
	 * Internal name of the report element type.
	 */

	public static final String REPORT_ELEMENT_NAME = "ReportElement"; //$NON-NLS-1$

	/**
	 * Internal name of the report design element type.
	 */

	public static final String REPORT_DESIGN_NAME = "ReportDesign"; //$NON-NLS-1$

	/**
	 * Code for an element that does not appear in any name space.
	 */

	public static final String NO_NAME_SPACE = "NONE";

	/**
	 * Code for an element that does not take a name.
	 */

	public static final int NO_NAME = 0;

	/**
	 * Code for an element with an optional name.
	 */

	public static final int OPTIONAL_NAME = 1;

	/**
	 * Code for an element with a required name.
	 */

	public static final int REQUIRED_NAME = 2;

}
