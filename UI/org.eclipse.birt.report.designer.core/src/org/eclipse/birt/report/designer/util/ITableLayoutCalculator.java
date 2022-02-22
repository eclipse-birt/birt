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

package org.eclipse.birt.report.designer.util;

/**
 * Provide html style layout algorighm
 */

public interface ITableLayoutCalculator {

	/**
	 * Calculate column width
	 *
	 * @return array of colum width
	 */
	float[] getFloatColWidth() throws NumberFormatException;

	/**
	 * Calculate column width
	 *
	 * @return array of colum width
	 */
	int[] getIntColWidth();

	/**
	 * Calculate row height
	 *
	 * @return array of row height
	 */
	float[] getFloatRowHeight() throws NumberFormatException;

	/**
	 * Calculate row height
	 *
	 * @return array of row height
	 */
	float[] getIntRowHeight();

}
