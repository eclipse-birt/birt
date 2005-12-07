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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.ir.DataItemDesign;

public class DataContent extends TextContent implements IDataContent
{

	protected Object value;

	protected String labelText;

	protected String labelKey;

	protected String helpKey;

	/**
	 * constructor use by serialize and deserialize
	 */
	public DataContent( )
	{

	}

	public int getContentType( )
	{
		return DATA_CONTENT;
	}

	public DataContent( ReportContent report )
	{
		super( report );
	}

	public DataContent( IContent content )
	{
		super( content );
	}

	public Object getValue( )
	{
		return value;
	}

	public void setValue( Object value )
	{
		this.value = value;
	}

	public String getLabelText( )
	{
		return labelText;
	}

	public void setLabelText( String text )
	{
		this.labelText = text;
	}

	public String getLabelKey( )
	{
		return this.labelKey;
	}

	public void setLabelKey( String key )
	{
		this.labelKey = key;
	}

	public String getHelpText( )
	{
		if ( helpText == null )
		{
			if ( generateBy instanceof DataItemDesign )
			{
				return ( (DataItemDesign) generateBy ).getHelpText( );
			}
		}
		return helpText;
	}

	public String getHelpKey( )
	{
		if ( helpKey == null )
		{
			if ( generateBy instanceof DataItemDesign )
			{
				return ( (DataItemDesign) generateBy ).getHelpTextKey( );
			}
		}
		return helpKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitData( this, value );
	}
	
	static final protected int FIELD_VALUE = 300;
	static final protected int FIELD_LAVELTEXT = 301;
	static final protected int FIELD_LABELKEY = 302;
	static final protected int FIELD_HELPKEY = 303;

	protected void writeFields( ObjectOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( value != null )
		{
			out.writeInt( FIELD_VALUE );
			out.writeObject( value );
		}
		if ( labelText != null )
		{
			out.writeInt( FIELD_LAVELTEXT );
			out.writeUTF( labelText );
		}
		if ( labelKey != null )
		{
			out.writeInt( FIELD_LABELKEY );
			out.writeUTF( labelKey );
		}
		if ( helpKey != null )
		{
			out.writeInt( FIELD_HELPKEY );
			out.writeUTF( helpKey );
		}
	}

	protected void readField( int version, int filedId, ObjectInputStream in )
			throws IOException, ClassNotFoundException
	{
		switch ( filedId )
		{
			case FIELD_VALUE :
				value = in.readObject( );
				break;
			case FIELD_LAVELTEXT :
				labelText = in.readUTF( );
				break;
			case FIELD_LABELKEY :
				labelKey = in.readUTF( );
				break;
			case FIELD_HELPKEY :
				helpKey = in.readUTF( );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}

}
