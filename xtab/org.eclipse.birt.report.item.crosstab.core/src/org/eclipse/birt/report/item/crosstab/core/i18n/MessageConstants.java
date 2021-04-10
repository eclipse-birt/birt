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

package org.eclipse.birt.report.item.crosstab.core.i18n;

/**
 * Message constants for crosstab internationalization.
 */

public interface MessageConstants {

	// crosstab exceptions
	/**
	 * Error code indicating two dimension view refer the same cube dimension
	 * element in a crosstab.
	 */
	String CROSSTAB_EXCEPTION_DUPLICATE_DIMENSION = "Error.CrosstabException.DUPLICATE_DIMENSION"; //$NON-NLS-1$

	/**
	 * Error code indicating tow measure view refer the same cube measure element in
	 * a crosstab.
	 */
	String CROSSTAB_EXCEPTION_DUPLICATE_MEASURE = "Error.CrosstabException.DUPLICATE_MEASURE"; //$NON-NLS-1$

	/**
	 * Error code indicating two level view refer the same cube level element in a
	 * dimension view.
	 */
	String CROSSTAB_EXCEPTION_DUPLICATE_LEVEL = "Error.CrosstabException.DUPLICATE_LEVEL"; //$NON-NLS-1$

	/**
	 * Error code indicating not find a dimension view that refers a cube dimension
	 * element in a crosstab.
	 */
	String CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND = "Error.CrosstabException.DIMENSION_NOT_FOUND"; //$NON-NLS-1$

	/**
	 * Error code indicating not find a measure view that refers a cube measure
	 * element in a crosstab.
	 */
	String CROSSTAB_EXCEPTION_MEASURE_NOT_FOUND = "Error.CrosstabException.MEASURE_NOT_FOUND"; //$NON-NLS-1$

	/**
	 * Error code indicating not find a level view that refers a cube level element
	 * in a dimension view.
	 */
	String CROSSTAB_EXCEPTION_LEVEL_NOT_FOUND = "Error.CrosstabException.LEVEL_NOT_FOUND"; //$NON-NLS-1$
}
