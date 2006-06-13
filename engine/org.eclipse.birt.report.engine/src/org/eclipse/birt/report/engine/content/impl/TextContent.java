/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.ir.TextItemDesign;

public class TextContent extends AbstractContent implements ITextContent
{

	transient protected String text;

	public int getContentType( )
	{
		return TEXT_CONTENT;
	}

	public TextContent( ReportContent report )
	{
		super( report );
	}

	public TextContent( IContent content )
	{
		super( content );
	}

	public String getText( )
	{
		return text;
	}

	public void setText( String text )
	{
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public Object accept( IContentVisitor visitor, Object value )
	{
		return visitor.visitText( this, value );
	}

	public void setTextKey( String key )
	{
		if ( generateBy instanceof TextItemDesign )
			( (TextItemDesign) generateBy ).setText( key, text );
	}

	public String getTextKey( )
	{
		if ( generateBy instanceof TextItemDesign )
			return ( (TextItemDesign) generateBy ).getTextKey( );
		return null;
	}

	public void setTextType( String type )
	{
		if ( generateBy instanceof TextItemDesign )
			( (TextItemDesign) generateBy ).setTextType( type );
	}

	public String getTextType( )
	{
		if ( generateBy instanceof TextItemDesign )
			return ( (TextItemDesign) generateBy ).getTextType( );
		return null;
	}
}
