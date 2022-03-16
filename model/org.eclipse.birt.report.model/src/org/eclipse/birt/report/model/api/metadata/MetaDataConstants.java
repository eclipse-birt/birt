/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
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

	String STYLE_NAME = "Style"; //$NON-NLS-1$

	/**
	 * Internal name of the report element type.
	 */

	String REPORT_ELEMENT_NAME = "ReportElement"; //$NON-NLS-1$

	/**
	 * Internal name of the report design element type.
	 */

	String REPORT_DESIGN_NAME = "ReportDesign"; //$NON-NLS-1$

	/**
	 * Code for an element that does not appear in any name space.
	 */

	String NO_NAME_SPACE = "NONE";

	/**
	 * Code for an element that does not take a name.
	 */

	int NO_NAME = 0;

	/**
	 * Code for an element with an optional name.
	 */

	int OPTIONAL_NAME = 1;

	/**
	 * Code for an element with a required name.
	 */

	int REQUIRED_NAME = 2;

}
