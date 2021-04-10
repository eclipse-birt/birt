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

package org.eclipse.birt.report.designer.internal.ui.layout;

/**
 * Interface to provide calculating data for table layout.
 */

public interface TableLayoutDataProvider {

	/**
	 * Returns the available container space.
	 * 
	 * @return
	 */
	int getAvailableWidth();

	/**
	 * Returns the recommending column width, this with should always greater than
	 * or equal to minimum column width.
	 * 
	 * @return
	 */
	int[] getHintColumnWidth();

	/**
	 * Returns the minimum column width required.
	 * 
	 * @return
	 */
	int[] getMinColumnWidth();

	/**
	 * Returns the maximum column width, this width should always greater than or
	 * equal to minimum column width.
	 * 
	 * @return
	 */
	int[] getMaxColumnWidth();
}