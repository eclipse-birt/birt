/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

/**
 * Column define.
 * 
 * @see TableItemDesign
 * @see GridItemDesign
 */
public class ColumnDesign extends StyledElementDesign {

	/**
	 * width of this column
	 */
	protected DimensionType width;

	/**
	 * suppressDuplicate
	 */
	protected boolean suppressDuplicate = false;

	/**
	 * Visibility property.
	 */
	protected VisibilityDesign visibility;

	/**
	 * If the is any data item in the detail cell of this column.
	 */
	protected boolean hasDataItemsInDetail = false;

	/**
	 * If this column is a column header.
	 */
	protected boolean isColumnHeader;

	/*
	 * Set the column header state.
	 * 
	 * @param isColumnHeader this column is a column header or not.
	 */
	public void setColumnHeaderState(boolean isColumnHeader) {
		this.isColumnHeader = isColumnHeader;
	}

	/*
	 * Return this column is a column header or not.
	 */
	public boolean isColumnHeader() {
		return isColumnHeader;
	}

	/**
	 * @return Returns the width.
	 */
	public DimensionType getWidth() {
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setWidth(DimensionType width) {
		this.width = width;
	}

	/**
	 * @param suppress The suppressDuplicate to set.
	 */
	public void setSuppressDuplicate(boolean suppress) {
		suppressDuplicate = suppress;
	}

	/**
	 * @return Returns the suppressDuplicate.
	 */
	public boolean getSuppressDuplicate() {
		return suppressDuplicate;
	}

	/**
	 * @return Returns the visibility.
	 */
	public VisibilityDesign getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility The visibility to set.
	 */
	public void setVisibility(VisibilityDesign visibility) {
		this.visibility = visibility;
	}

	/**
	 * @return the hasDataItemsInDetail
	 */
	public boolean hasDataItemsInDetail() {
		return hasDataItemsInDetail;
	}

	/**
	 * @param hasDataItemsInDetail the hasDataItemsInDetail to set
	 */
	public void setHasDataItemsInDetail(boolean hasDataItemsInDetail) {
		this.hasDataItemsInDetail = hasDataItemsInDetail;
	}
}
