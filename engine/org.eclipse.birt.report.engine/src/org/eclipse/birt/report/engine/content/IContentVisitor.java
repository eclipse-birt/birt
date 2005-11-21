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
 * @version $Revision: 1.1 $ $Date: 2005/11/11 06:26:46 $
 */
public interface IContentVisitor
{

	void visit(IContent content, Object value);
	
	void visitContent(IContent content, Object value);
	
	
	void visitPage( IPageContent page , Object value);

	/**
	 * visit content( free-form and list band)
	 * 
	 * @param content
	 */
	void visitContainer( IContainerContent container , Object value);

	/**
	 * visit table content object
	 * 
	 * @param table
	 *            the table object
	 */
	void visitTable( ITableContent table , Object value);

	/**
	 * visit table band
	 * 
	 * @param tableBand
	 */
	void visitTableBand( ITableBandContent tableBand , Object value);

	/**
	 * visit the row content object
	 * 
	 * @param row
	 *            the row object
	 */
	void visitRow( IRowContent row , Object value);

	/**
	 * visit cell content object
	 * 
	 * @param cell
	 *            the cell object
	 */
	void visitCell( ICellContent cell , Object value);

	/**
	 * visit the text content object
	 * 
	 * @param text
	 *            the text object
	 */
	void visitText( ITextContent text , Object value);
	
	void visitLabel(ILabelContent label, Object value);
	
	void visitData(IDataContent data, Object value);

	/**
	 * visit image content
	 * 
	 * @param image
	 */
	void visitImage( IImageContent image , Object value);

	/**
	 * visit exteded item
	 * 
	 * @param content
	 */
	void visitForeign( IForeignContent foreign , Object value);
}