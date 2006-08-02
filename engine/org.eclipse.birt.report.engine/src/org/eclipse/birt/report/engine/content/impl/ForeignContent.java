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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;

public class ForeignContent extends AbstractContent implements IForeignContent
{

	protected String rawType;
	protected Object rawValue;
	
	protected String altText;
	protected String altTextKey;

	public int getContentType( )
	{
		return FOREIGN_CONTENT;
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
	public Object accept( IContentVisitor visitor, Object value )
	{
		return visitor.visitForeign( this, value );
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
	
	public String getAltText( )
	{
		if ( altText == null )
		{
			if ( generateBy instanceof ExtendedItemDesign )
			{
				return ( (ExtendedItemDesign) generateBy ).getAltText( );
			}
		}
		return altText;
	}

	public String getAltTextKey( )
	{
		if ( altTextKey == null )
		{
			if ( generateBy instanceof ExtendedItemDesign )
			{
				return ( (ExtendedItemDesign) generateBy ).getAltTextKey( );
			}
		}
		return altTextKey;
	}

	public void setAltTextKey( String key )
	{
		altTextKey = key;
	}
	/**
	 * @param altText
	 *            The altText to set.
	 */
	public void setAltText( String altText )
	{
		this.altText = altText;
	}

	static final protected int FIELD_ROW_TYPE = 400;
	static final protected int FIELD_ROWVALUE = 401;
	static final protected int FIELD_ALTTEXT = 402;
	static final protected int FIELD_ALTTEXTKEY = 403;

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( rawType != null )
		{
			IOUtil.writeInt( out,  FIELD_ROW_TYPE );
			IOUtil.writeString( out, rawType );
		}
		if ( rawValue != null )
		{
			IOUtil.writeInt( out,  FIELD_ROWVALUE );
			IOUtil.writeObject( out, rawValue );
		}
		if ( altText != null )
		{
			IOUtil.writeInt( out, FIELD_ALTTEXT );
			IOUtil.writeString( out, altText );
		}
		if ( altTextKey != null )
		{
			IOUtil.writeInt( out, FIELD_ALTTEXTKEY );
			IOUtil.writeString( out, altTextKey );
		}
	}

	protected void readField( int version, int filedId, DataInputStream in )
			throws IOException
	{
		switch ( filedId )
		{
			case FIELD_ROW_TYPE :
				rawType = IOUtil.readString( in  );
				break;
			case FIELD_ROWVALUE :
				rawValue = IOUtil.readObject( in );
				break;
			case FIELD_ALTTEXT :
				altText = IOUtil.readString( in );
				break;
			case FIELD_ALTTEXTKEY :
				altTextKey = IOUtil.readString( in );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}

}
