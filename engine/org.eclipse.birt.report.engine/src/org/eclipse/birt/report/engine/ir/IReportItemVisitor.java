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

package org.eclipse.birt.report.engine.ir;

/**
 * A visitor class against the report design.
 * 
 * @version $Revision: 1.3 $ $Date: 2005/05/08 06:59:45 $
 */
public interface IReportItemVisitor {

	/**
	 * visit free-form container
	 * 
	 * @param container
	 *            the free-form container to be visited
	 */
	void visitFreeFormItem(FreeFormItemDesign container, Object value);

	/**
	 * visit list item
	 * 
	 * @param list
	 *            the list item to be viisted
	 */
	void visitListItem(ListItemDesign list, Object value);

	/**
	 * visit text item
	 * 
	 * @param text
	 *            the text item to be visited
	 */
	void visitTextItem(TextItemDesign text, Object value);

	/**
	 * visit label item
	 * 
	 * @param label
	 *            the label item to be visited
	 */
	void visitLabelItem(LabelItemDesign label, Object value);

	/**
	 * visit data item
	 * 
	 * @param data
	 *            the data item to be visited
	 */
	void visitDataItem(DataItemDesign data, Object value);

	/**
	 * visit multi-line data item
	 * 
	 * @param multiLine
	 *            the multi-line item to be visited.
	 */
	void visitMultiLineItem(MultiLineItemDesign multiLine, Object value);

	/**
	 * visit grid item
	 * 
	 * @param grid
	 *            the grid to be visited
	 */
	void visitGridItem(GridItemDesign grid, Object value);

	/**
	 * visit table item
	 * 
	 * @param table
	 *            the table item to be visited
	 */
	void visitTableItem(TableItemDesign table, Object value);
	
	void visitRow(RowDesign row, Object value);
	
	void visitCell(CellDesign cell, Object value);

	/**
	 * visit image item.
	 * 
	 * @param image
	 *            the image item to be visited.
	 */
	void visitImageItem(ImageItemDesign image, Object value);

	/**
	 * visit extended item
	 * 
	 * @param item
	 *            the extended item to be visited
	 */
	void visitExtendedItem(ExtendedItemDesign item, Object value);
}
