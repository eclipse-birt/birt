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
 * @version $Revision: 1.5 $ $Date: 2005/11/17 16:50:43 $
 */
public interface IReportItemVisitor {

	/**
	 * visit free-form container
	 * 
	 * @param container
	 *            the free-form container to be visited
	 */
	Object visitFreeFormItem(FreeFormItemDesign container, Object value);

	/**
	 * visit list item
	 * 
	 * @param list
	 *            the list item to be viisted
	 */
	Object visitListItem(ListItemDesign list, Object value);

	/**
	 * visit text item
	 * 
	 * @param text
	 *            the text item to be visited
	 */
	Object visitTextItem(TextItemDesign text, Object value);

	/**
	 * visit label item
	 * 
	 * @param label
	 *            the label item to be visited
	 */
	Object visitLabelItem(LabelItemDesign label, Object value);

	/**
	 * visit data item
	 * 
	 * @param data
	 *            the data item to be visited
	 */
	Object visitDataItem(DataItemDesign data, Object value);

	/**
	 * visit multi-line data item
	 * 
	 * @param multiLine
	 *            the multi-line item to be visited.
	 */
	Object visitMultiLineItem(MultiLineItemDesign multiLine, Object value);

	/**
	 * visit grid item
	 * 
	 * @param grid
	 *            the grid to be visited
	 */
	Object visitGridItem(GridItemDesign grid, Object value);

	/**
	 * visit table item
	 * 
	 * @param table
	 *            the table item to be visited
	 */
	Object visitTableItem(TableItemDesign table, Object value);
	
	Object visitRow(RowDesign row, Object value);
	
	Object visitCell(CellDesign cell, Object value);

	/**
	 * visit image item.
	 * 
	 * @param image
	 *            the image item to be visited.
	 */
	Object visitImageItem(ImageItemDesign image, Object value);

	/**
	 * visit extended item
	 * 
	 * @param item
	 *            the extended item to be visited
	 */
	Object visitExtendedItem(ExtendedItemDesign item, Object value);

	/**
	 * visit template design.
	 * 
	 * @param template
	 *            template item
	 * @param value
	 *            paramter values used by this visitor
	 * @return the value after the visit.
	 */
	Object visitTemplate( TemplateDesign template, Object value );
}
