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
	public float[] getFloatColWidth() throws NumberFormatException;

	/**
	 * Calculate column width
	 * 
	 * @return array of colum width
	 */
	public int[] getIntColWidth();

	/**
	 * Calculate row height
	 * 
	 * @return array of row height
	 */
	public float[] getFloatRowHeight() throws NumberFormatException;

	/**
	 * Calculate row height
	 * 
	 * @return array of row height
	 */
	public float[] getIntRowHeight();

}