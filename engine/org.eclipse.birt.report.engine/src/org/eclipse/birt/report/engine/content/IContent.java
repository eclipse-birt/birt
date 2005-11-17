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

import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * object created by report generator.
 * 
 * the content of report document.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/11/11 06:26:46 $
 */
public interface IContent extends IElement, CSSStylableElement
{

	InstanceID getInstanceID();
	
	void setInstanceID(InstanceID id);
	
	IReportContent getReport( );
	
	void setReport(IReportContent report);

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
	 * bound properties caculated by the layout manager. may be NULL for
	 * unformated content.
	 * 
	 * @return
	 */
	IBounds getBounds( );

	void setBounds( IBounds bounds );

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
}