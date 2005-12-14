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
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * 
 * column content object
 * 
 * @version $Revision: 1.3 $ $Date: 2005/12/07 07:21:33 $
 */
public class Column implements IColumn
{

	protected DimensionType width;

	protected String styleClass;

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
	
	/**
	 * object document column version
	 */
	static final protected int VERSION = 0;

	final static int FIELD_NONE = -1;
	final static int FIELD_WIDTH = 0;
	final static int FIELD_STYLECLASS = 1;
	
	protected void writeFields( ObjectOutputStream out ) throws IOException
	{		
		if ( width != null )
		{
			out.writeInt( FIELD_WIDTH );
			width.writeContent( out );
		}
		if ( styleClass != null )
		{
			out.writeInt( FIELD_STYLECLASS );
			out.writeUTF( styleClass );
		}
	}

	protected void readField( int version, int filedId, ObjectInputStream in )
			throws IOException, ClassNotFoundException
	{
		switch ( filedId )
		{
			case FIELD_WIDTH :
				width = new DimensionType( );
				width.readContent( in );
				break;
			case FIELD_STYLECLASS :
				styleClass = in.readUTF( );
				break;
		}
	}
	
	public void readContent( ObjectInputStream in ) throws IOException, ClassNotFoundException
	{
		int version = in.readInt( );
		int filedId = in.readInt( );
		while ( filedId != FIELD_NONE )
		{
			readField( version, filedId, in );
			filedId = in.readInt( );
		}
	}

	public void writeContent( ObjectOutputStream out ) throws IOException
	{
		out.writeInt( VERSION );
		writeFields( out );
		out.writeInt( FIELD_NONE );
	}
}