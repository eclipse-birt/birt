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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * object created by report generator.
 * 
 * the content of report document.
 * 
 * @version $Revision: 1.7 $ $Date: 2006/04/27 09:52:25 $
 */
public interface IContent extends IElement, CSSStylableElement
{
	final static int SERIALIZE_CONTENT = -1;
	final static int CELL_CONTENT = 1;
	final static int CONTAINER_CONTENT = 2;
	final static int DATA_CONTENT = 3;
	final static int FOREIGN_CONTENT = 4;
	final static int IMAGE_CONTENT = 5;
	final static int LABEL_CONTENT = 6;
	final static int PAGE_CONTENT = 7;
	final static int ROW_CONTENT = 8;
	final static int TABLE_BAND_CONTENT = 9;
	final static int TABLE_CONTENT = 10;
	final static int TEXT_CONTENT = 11;	
	final static int AUTOTEXT_CONTENT =12;

	/**
	 * the content type, must be one of the predefines.
	 * @return
	 */
	int getContentType();

	/**
	 * get the instance id of the content.
	 * the instance id is the unique id of the content.
	 * @return
	 */
	InstanceID getInstanceID();
	
	/**
	 * set the instace id of the content.
	 * the instance id can only be set by the content
	 * generator.
	 * @param id
	 */
	void setInstanceID(InstanceID id);
	
	/**
	 * if the report is saved into the report document,
	 * return the offset of that content.
	 * It may be -1 if it was not saved.
	 * @return
	 */
	long getOffset();
	/**
	 * the offset can only be setted by the content writer
	 * or content loader
	 * @param offset
	 */
	void setOffset(long offset);
	
	/**
	 * return the report which contains/create this content.
	 * @return
	 */
	IReportContent getReportContent( );
	
	/**
	 * set the report content.
	 * @param report
	 */
	void setReportContent(IReportContent report);

	/**
	 * unique id of the content.
	 * 
	 * @return
	 */
	String getName( );

	void setName( String name );

	/**
	 * the design object which create this content.
	 * 
	 * @return
	 */
	Object getGenerateBy( );

	void setGenerateBy( Object generateBy );

	/**
	 * @return inline style
	 */
	IStyle getInlineStyle( );

	void setInlineStyle( IStyle style );

	String getStyleClass( );

	void setStyleClass( String styleClass );

	/**
	 * use visitor to process the object.
	 * 
	 * @param visitor
	 */
	void accept( IContentVisitor visitor, Object value );

	/**
	 * @return the bookmark value
	 */
	String getBookmark( );

	void setBookmark( String bookmark );

	/**
	 * @return hyperlink actions
	 */
	IHyperlinkAction getHyperlinkAction( );

	void setHyperlinkAction( IHyperlinkAction hyperlink );

	/**
	 * @return Returns the helpText.
	 */
	String getHelpText( );

	void setHelpText( String help );

	/**
	 * sepcified value, the actual height is defined in IBounds
	 * 
	 * @return the height of the report item
	 */
	DimensionType getHeight( );

	void setHeight( DimensionType height );

	/**
	 * specified value, the real value is defined in IBounds
	 * 
	 * @return the width of the report item
	 */
	DimensionType getWidth( );

	void setWidth( DimensionType width );

	/**
	 * specified value, the real value is defined in IBounds
	 * 
	 * @return the x position of the repor titem.
	 */
	DimensionType getX( );

	void setX( DimensionType x );

	/**
	 * specified value, the real value is defined in IBounds
	 * 
	 * @return Returns the y position of the repor titem.
	 */
	DimensionType getY( );

	void setY( DimensionType y );
	
	String getTOC();
	void setTOC(String toc);
	
	void writeContent( DataOutputStream out ) throws IOException;
	void readContent( DataInputStream in ) throws IOException;
}