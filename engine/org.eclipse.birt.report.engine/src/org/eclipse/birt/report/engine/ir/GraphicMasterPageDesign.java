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

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;

/**
 * 
 */
public class GraphicMasterPageDesign extends MasterPageDesign {

	protected int columns = 1;

	/**
	 * spacing between columns, must be a absoluted dimension.
	 */
	protected DimensionType columnSpacing;

	/**
	 * page content
	 */
	ArrayList contents = new ArrayList();

	/**
	 * get all content in this master page
	 * 
	 * @return contents
	 */
	public ArrayList getContents() {
		return this.contents;
	}

	/**
	 * get content count
	 * 
	 * @return total content
	 */
	public int getContentCount() {
		return this.contents.size();
	}

	/**
	 * get content at index.
	 * 
	 * @param index item index
	 * @return item.
	 */
	public ReportItemDesign getContent(int index) {
		assert (index >= 0 && index < contents.size());
		return (ReportItemDesign) contents.get(index);
	}

	/**
	 * add report item into page content.
	 * 
	 * @param item item to be added
	 */
	public void addContent(ReportItemDesign item) {
		this.contents.add(item);
	}

	/**
	 * @return Returns the columns.
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * @param columns The columns to set.
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * @return Returns the columnSpacing.
	 */
	public DimensionType getColumnSpacing() {
		return columnSpacing;
	}

	/**
	 * @param columnSpacing The columnSpacing to set.
	 */
	public void setColumnSpacing(DimensionType columnSpacing) {
		this.columnSpacing = columnSpacing;
	}

}
