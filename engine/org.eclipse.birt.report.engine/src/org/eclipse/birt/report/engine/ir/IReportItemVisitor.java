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

/**
 * A visitor class against the report design.
 * 
 */
public interface IReportItemVisitor {

	/**
	 * visit free-form container
	 * 
	 * @param container the free-form container to be visited
	 */
	Object visitFreeFormItem(FreeFormItemDesign container, Object value);

	Object visitListing(ListingDesign listing, Object value);

	Object visitBand(BandDesign band, Object value);

	/**
	 * visit list item
	 * 
	 * @param list the list item to be viisted
	 */
	Object visitListItem(ListItemDesign list, Object value);

	Object visitListBand(ListBandDesign band, Object value);

	/**
	 * visit text item
	 * 
	 * @param text the text item to be visited
	 */
	Object visitTextItem(TextItemDesign text, Object value);

	/**
	 * visit label item
	 * 
	 * @param label the label item to be visited
	 */
	Object visitLabelItem(LabelItemDesign label, Object value);

	/**
	 * visit auto text item
	 * 
	 * @param autoText the auto text item to be visited
	 */
	Object visitAutoTextItem(AutoTextItemDesign autoText, Object value);

	/**
	 * visit data item
	 * 
	 * @param data the data item to be visited
	 */
	Object visitDataItem(DataItemDesign data, Object value);

	/**
	 * visit multi-line data item
	 * 
	 * @param multiLine the multi-line item to be visited.
	 */
	Object visitDynamicTextItem(DynamicTextItemDesign multiLine, Object value);

	/**
	 * visit grid item
	 * 
	 * @param grid the grid to be visited
	 */
	Object visitGridItem(GridItemDesign grid, Object value);

	/**
	 * visit table item
	 * 
	 * @param table the table item to be visited
	 */
	Object visitTableItem(TableItemDesign table, Object value);

	Object visitTableBand(TableBandDesign band, Object value);

	Object visitRow(RowDesign row, Object value);

	Object visitCell(CellDesign cell, Object value);

	/**
	 * visit image item.
	 * 
	 * @param image the image item to be visited.
	 */
	Object visitImageItem(ImageItemDesign image, Object value);

	/**
	 * visit extended item
	 * 
	 * @param item the extended item to be visited
	 */
	Object visitExtendedItem(ExtendedItemDesign item, Object value);

	/**
	 * visit template design.
	 * 
	 * @param template template item
	 * @param value    paramter values used by this visitor
	 * @return the value after the visit.
	 */
	Object visitTemplate(TemplateDesign template, Object value);

	Object visitGroup(GroupDesign group, Object value);

	Object visitListGroup(ListGroupDesign group, Object value);

	Object visitTableGroup(TableGroupDesign group, Object value);
}
