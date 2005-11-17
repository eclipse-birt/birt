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

package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.ir.TextItemDesign;

public class ForeignContent extends AbstractContent implements IForeignContent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7041526628380142925L;
	protected String rawType;
	protected Object rawValue;

	/**
	 * constructor.
	 * use by serialize and deserialize
	 */
	public ForeignContent( )
	{

	}

	public ForeignContent( ReportContent report )
	{
		super( report );
	}

	public ForeignContent( IContent content )
	{
		super( content );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitForeign( this, value );
	}

	public String getRawType( )
	{
		return rawType;
	}

	/**
	 * @return Returns the content. Caller knows how to cast this object
	 */
	public Object getRawValue( )
	{
		return rawValue;
	}

	/**
	 * @param rawType
	 *            The rawType to set.
	 */
	public void setRawType( String rawType )
	{
		this.rawType = rawType;
	}

	/**
	 * @param rawValue
	 *            The rawValue to set.
	 */
	public void setRawValue( Object rawValue )
	{
		this.rawValue = rawValue;
	}

	/**
	 * @param contentType
	 * @param content
	 * @return
	 */
	public static String getTextRawType( String contentType, Object content )
	{
		if ( TextItemDesign.PLAIN_TEXT.equals( contentType ) )
		{
			return IForeignContent.TEXT_TYPE;
		}
		if ( TextItemDesign.HTML_TEXT.equals( contentType ) )
		{
			return IForeignContent.HTML_TYPE;
		}
		String text = content == null ? "" : content.toString( ).trim( );
		if ( text.length( ) > 6 )
		{
			if ( "<html>".equalsIgnoreCase( text.substring( 0, 6 ) ) )
			{
				return IForeignContent.HTML_TYPE;
			}
		}
		return IForeignContent.TEXT_TYPE;
	}
}
