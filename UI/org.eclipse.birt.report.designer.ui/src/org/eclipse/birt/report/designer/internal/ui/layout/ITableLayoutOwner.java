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

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPartViewer;

/**
 * The class is the owner of the table layout.
 */
public interface ITableLayoutOwner {
	/**
	 * The owner if is active
	 * 
	 * @return
	 */
	boolean isActive();

	/**
	 * Gets the layout figure
	 * 
	 * @return
	 */
	IFigure getFigure();

	/**
	 * Need the layout layout again
	 */
	void reLayout();

	/**
	 * gets the viewer
	 * 
	 * @return
	 */
	// The methos maybe change
	EditPartViewer getViewer();

	/**
	 * Gets the heigh infomation form the model
	 * 
	 * @param number
	 * @return
	 */
	DimensionInfomation getRowHeight(int number);

	/**
	 * Gets the column from the model
	 * 
	 * @param number
	 * @return
	 */
	DimensionInfomation getColumnWidth(int number);

	/**
	 * Gets the column count
	 * 
	 * @return
	 */
	int getColumnCount();

	/**
	 * Gets the row count
	 * 
	 * @return
	 */
	int getRowCount();

	/**
	 * Gets the children
	 * 
	 * @return
	 */
	List getChildren();

	/**
	 * Gets the define width.
	 * 
	 * @return
	 */
	String getDefinedWidth();

	/**
	 * If the force set to t he model
	 * 
	 * @return
	 */
	boolean isForceWidth();

	/**
	 * Gets the define height,return null if the owner don't support the height;
	 * 
	 * @return
	 */
	String getDefinedHeight();

	/**
	 * Gets the ori column width
	 * 
	 * @param columNumber
	 * @return
	 */
	String getRawWidth(int columNumber);

	/**
	 * Through the row infomation to cale the height value.
	 * 
	 * @param number
	 * @return
	 */
	int getRowHeightValue(int number);

	/**
	 * Through the column infomation to cale the width value.
	 * 
	 * @param number
	 * @return
	 */
	int getColumnWidthValue(int number);

	/**
	 * Gets the allow min row hight.If no special request, return
	 * FixTableLayout.DEFAULT_ROW_HEIGHT;
	 * 
	 * @return
	 */
	int getFixAllowMinRowHight();

	/**
	 * value and unit DimensionInfomation
	 */
	public static class DimensionInfomation {
		private double measure;
		private String units = ""; //$NON-NLS-1$
		private boolean isSet = false;

		public DimensionInfomation(double measure, String units) {
			this(measure, units, false);
		}

		public DimensionInfomation(double measure, String units, boolean isSet) {
			this.measure = measure;
			this.units = units;
			this.isSet = isSet;
		}

		public String getUnits() {
			return units;
		}

		public double getMeasure() {
			return measure;
		}

		public boolean isSet() {
			return isSet;
		}
	}
}
