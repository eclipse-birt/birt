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
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * 
 * column content object
 * 
 * @version $Revision: 1.5 $ $Date: 2006/01/11 06:29:02 $
 */
public class Column implements IColumn
{

	protected DimensionType width;

	protected String styleClass;
	
	protected InstanceID instanceId;
	
	protected String visibleFormat;

	/**
	 * constructor use by serialize and deserialize
	 */
	public Column( )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IColumn#getStyle()
	 */
	public IStyle getStyle( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IColumn#getWidth()
	 */
	public DimensionType getWidth( )
	{
		return width;
	}

	public void setWidth( DimensionType width )
	{
		this.width = width;
	}

	public String getStyleClass( )
	{
		return styleClass;
	}

	public void setStyleClass( String styleClass )
	{
		this.styleClass = styleClass;
	}	

	public InstanceID getInstanceID( )
	{
		return instanceId;
	}

	public void setInstanceID( InstanceID id )
	{
		this.instanceId = id;
	}

	public String getVisibleFormat( )
	{
		return visibleFormat;
	}

	public void setVisibleFormat( String visibleFormat )
	{
		this.visibleFormat = visibleFormat;
	}	
	
	/**
	 * object document column version
	 */
	static final protected int VERSION = 0;

	final static int FIELD_NONE = -1;
	final static int FIELD_WIDTH = 0;
	final static int FIELD_STYLECLASS = 1;
	final static int FIELD_INSTANCE_ID = 2;
	final static int FIELD_VISIBLE_FORMAT = 3;

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		if ( width != null )
		{
			IOUtil.writeInt( out, FIELD_WIDTH );
			width.writeObject( out );
		}
		if ( styleClass != null )
		{
			IOUtil.writeInt( out, FIELD_STYLECLASS );
			IOUtil.writeString( out, styleClass );
		}
		if ( instanceId != null )
		{
			IOUtil.writeInt( out, FIELD_INSTANCE_ID );
			IOUtil.writeString( out, instanceId.toString( ) );
		}
		if ( visibleFormat != null )
		{
			IOUtil.writeInt( out, FIELD_VISIBLE_FORMAT );
			IOUtil.writeString( out, visibleFormat );
		}
	}

	protected void readField( int version, int filedId, DataInputStream in )
			throws IOException
	{
		switch ( filedId )
		{
			case FIELD_WIDTH :
				width = new DimensionType( );
				width.readObject( in );
				break;
			case FIELD_STYLECLASS :
				styleClass = IOUtil.readString( in );
				break;
			case FIELD_INSTANCE_ID :
				String value = IOUtil.readString( in );
				instanceId = InstanceID.parse( value );
				break;
			case FIELD_VISIBLE_FORMAT :
				visibleFormat = IOUtil.readString( in );
				break;
		}
	}

	public void readObject( DataInputStream in ) throws IOException
	{
		int version = IOUtil.readInt( in );
		int filedId = IOUtil.readInt( in );
		while ( filedId != FIELD_NONE )
		{
			readField( version, filedId, in );
			filedId = IOUtil.readInt( in );
		}
	}

	public void writeObject( DataOutputStream out ) throws IOException
	{
		IOUtil.writeInt( out,  VERSION );
		writeFields( out );
		IOUtil.writeInt( out,  FIELD_NONE );
	}
}