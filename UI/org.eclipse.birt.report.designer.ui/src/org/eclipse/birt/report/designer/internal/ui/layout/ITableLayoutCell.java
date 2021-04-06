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

import org.eclipse.draw2d.IFigure;

/**
 * Cell layout interface for the table layout
 */
public interface ITableLayoutCell {

	/**
	 * Gets the row number.
	 * 
	 * @return
	 */
	int getRowNumber();

	/**
	 * Gets the column number.
	 * 
	 * @return
	 */
	int getColumnNumber();

	/**
	 * Gets the row span.
	 * 
	 * @return
	 */
	int getRowSpan();

	/**
	 * Gets the cloumn span.
	 * 
	 * @return
	 */
	int getColSpan();

	/**
	 * Gets the cell figure.
	 * 
	 * @return
	 */
	IFigure getFigure();
}
