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
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.ir.DataItemDesign;

public class DataContent extends TextContent implements IDataContent
{

	protected Object value;

	protected String labelText;

	protected String labelKey;

	protected String helpKey;

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

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( value != null )
		{
			boolean needSave = true;
			if ( this.generateBy instanceof DataItemDesign )
			{
				DataItemDesign design = (DataItemDesign) generateBy;
				if ( design.getMap( ) == null )
				{
					needSave = false;
				}
			}
			if ( needSave )
			{
				IOUtil.writeInt( out, FIELD_VALUE );
				IOUtil.writeObject( out, value );
			}
		}
		if ( labelText != null )
		{
			IOUtil.writeInt( out, FIELD_LAVELTEXT );
			IOUtil.writeString( out, labelText );
		}
		if ( labelKey != null )
		{
			IOUtil.writeInt( out, FIELD_LABELKEY );
			IOUtil.writeString( out, labelKey );
		}
		if ( helpKey != null )
		{
			IOUtil.writeInt( out, FIELD_HELPKEY );
			IOUtil.writeString( out, helpKey );
		}
	}

	protected void readField( int version, int filedId, DataInputStream in )
			throws IOException
	{
		switch ( filedId )
		{
			case FIELD_VALUE :
				value = IOUtil.readObject( in );
				break;
			case FIELD_LAVELTEXT :
				labelText = IOUtil.readString( in );
				break;
			case FIELD_LABELKEY :
				labelKey = IOUtil.readString( in );
				break;
			case FIELD_HELPKEY :
				helpKey = IOUtil.readString( in );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}

}
