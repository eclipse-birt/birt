/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.model;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * Interface to provide the function to calculate table visual size
 */
public interface ITableAdapterHelper extends IModelAdapterHelper {

	/**
	 * Calculates the column visual width
	 * 
	 * @param columnNumber
	 * @return
	 */
	public int caleVisualWidth(int columnNumber);

	/**
	 * Calculates the row visual width
	 * 
	 * @param rowNumber
	 * @return
	 */
	public int caleVisualHeight(int rowNumber);

	/**
	 * Gets the row Minimum height
	 * 
	 * @param rowNumber
	 * @return
	 */
	public int getMinHeight(int rowNumber);

	/**
	 * Gets the column Minimum width
	 * 
	 * @param columnNumber
	 * @return
	 */
	public int getMinWidth(int columnNumber);

	/**
	 * Returns the client area size for associated figure.
	 * 
	 * @return
	 */
	public Dimension getClientAreaSize();

}