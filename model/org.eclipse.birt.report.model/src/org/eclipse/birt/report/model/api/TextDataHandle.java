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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TextDataItem;

/**
 * Represents a multi-line data item element. The multi-line data item displays
 * blocks of text retrieved from the database, from a file, or from an
 * expression. The text can be plain text, HTML, RTF or an expression. The
 * format of the text can be fixed at design time, or can be dynamically
 * selected at run time to match the format of the incoming text.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.TextDataItem
 */

public class TextDataHandle extends ReportItemHandle
{

	/**
	 * Constructs the handle with the report design and the element it holds.
	 * The application generally does not create handles directly. Instead, it
	 * uses one of the navigation methods available on other element handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public TextDataHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns the expression that gives the text that the multi-line data item
	 * displays.
	 * 
	 * @return the value expression
	 */

	public String getValueExpr( )
	{
		return getStringProperty( TextDataItem.VALUE_EXPR_PROP );
	}

	/**
	 * Sets the expression that gives the text that this multi-line data item
	 * displays.
	 * 
	 * @param expr
	 *            the new expression for the value expression
	 * @throws SemanticException
	 *             if the expression contains errors, or the property is locked.
	 */

	public void setValueExpr( String expr ) throws SemanticException
	{
		setProperty( TextDataItem.VALUE_EXPR_PROP, expr );
	}

	/**
	 * Returns the expression that that defines the type of text the multi-line
	 * data item holds. The content type can be one of Auto (default);
	 * Plain: Plain text; HTML: HTML format; RTF: Rich Text Format;
	 * Expression: an expression that returns one of the above strings.
	 * 
	 * @return the expression for the text type
	 */

	public String getContentTypeExpr( )
	{
		return getStringProperty( TextDataItem.CONTENT_TYPE_EXPR_PROP );
	}

	/**
	 * Sets the expression that defines the text type this multi-line data item
	 * holds. The content type can be one of Auto (default); Plain: Plain
	 * text; HTML: HTML format; RTF: Rich Text Format; Expression: an
	 * expression that returns one of the above strings.
	 * 
	 * @param expr
	 *            the new expression for the text type
	 * @throws SemanticException
	 *             if the expression contains errors, or the property is locked.
	 */

	public void setContentTypeExpr( String expr ) throws SemanticException
	{
		setProperty( TextDataItem.CONTENT_TYPE_EXPR_PROP, expr );
	}
}
