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
 * @version $Revision: 1.2 $ $Date: 2005/05/08 06:08:26 $
 */
public interface IReportItemVisitor
{

	/**
	 * visit free-form container
	 * 
	 * @param container the free-form container to be visited
	 */
	void visitFreeFormItem( FreeFormItemDesign container );

	/**
	 * visit list item
	 * 
	 * @param list the list item to be viisted
	 */
	void visitListItem( ListItemDesign list );

	/**
	 * visit text item
	 * 
	 * @param text the text item to be visited
	 */
	void visitTextItem( TextItemDesign text );

	/**
	 * visit label item
	 * 
	 * @param label the label item to be visited
	 */
	void visitLabelItem( LabelItemDesign label );

	/**
	 * visit data item
	 * 
	 * @param data the data item to be visited
	 */
	void visitDataItem( DataItemDesign data );
	
	/**
	 * visit multi-line data item
	 * 
	 * @param multiLine the multi-line item to be visited.
	 */
	void visitMultiLineItem(MultiLineItemDesign multiLine);

	/**
	 * visit grid item
	 * 
	 * @param grid the grid to be visited
	 */
	void visitGridItem( GridItemDesign grid );

	/**
	 * visit table item
	 * 
	 * @param table the table item to be visited
	 */
	void visitTableItem( TableItemDesign table );

	/**
	 * visit image item.
	 * 
	 * @param image the image item to be visited.
	 */
	void visitImageItem( ImageItemDesign image );

	/**
	 * visit extended item
	 * 
	 * @param item the extended item to be visited
	 */
	void visitExtendedItem( ExtendedItemDesign item );
}
