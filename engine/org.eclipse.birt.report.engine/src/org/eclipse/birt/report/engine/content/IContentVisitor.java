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

package org.eclipse.birt.report.engine.content;

/**
 * Defines the visitor interface used mainly by a buffered emitter
 * 
 */
public interface IContentVisitor
{

	Object visit(IContent content, Object value);
	
	Object visitContent(IContent content, Object value);
	
	
	Object visitPage( IPageContent page , Object value);

	/**
	 * visit content( free-form and list band)
	 * 
	 * @param content
	 */
	Object visitContainer( IContainerContent container , Object value);

	/**
	 * visit table content object
	 * 
	 * @param table
	 *            the table object
	 */
	Object visitTable( ITableContent table , Object value);

	/**
	 * visit table band
	 * 
	 * @param tableBand
	 */
	Object visitTableBand( ITableBandContent tableBand, Object value );

	/**
	 * visit list content
	 * @param list
	 * @param value
	 */
	Object visitList( IListContent list, Object value );

	/**
	 * visit list band content
	 * @param listBand
	 * @param value
	 */
	Object visitListBand( IListBandContent listBand, Object value );

	/**
	 * visit the row content object
	 * 
	 * @param row
	 *            the row object
	 */
	Object visitRow( IRowContent row , Object value);

	/**
	 * visit cell content object
	 * 
	 * @param cell
	 *            the cell object
	 */
	Object visitCell( ICellContent cell , Object value);

	/**
	 * visit the text content object
	 * 
	 * @param text
	 *            the text object
	 */
	Object visitText( ITextContent text , Object value);
	
	Object visitLabel(ILabelContent label, Object value);
	
	Object visitAutoText(IAutoTextContent autoText, Object value);
	
	Object visitData(IDataContent data, Object value);

	/**
	 * visit image content
	 * 
	 * @param image
	 */
	Object visitImage( IImageContent image , Object value);

	/**
	 * visit exteded item
	 * 
	 * @param content
	 */
	Object visitForeign( IForeignContent foreign , Object value);
	
	Object visitGroup( IGroupContent group, Object value );
	
	Object visitListGroup(IListGroupContent group, Object value);
	
	Object visitTableGroup(ITableGroupContent group, Object value);
}
