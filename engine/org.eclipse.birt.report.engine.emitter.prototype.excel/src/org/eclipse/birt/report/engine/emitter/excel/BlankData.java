/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;



public class BlankData extends Data
{

	public static enum Type {
		VERTICAL, HORIZONTAL, NONE
	};

	private SheetData data;

	private Type type;

	public BlankData( SheetData data )
	{
		super( data );
		this.data = data;
	}

	public boolean isBlank()
	{
		return true;
	}
	
    public SheetData getData()
    {
    	return data;
    }

    public int getRowSpan( )
    {
		return data.getRowSpan( );
    }
    
    public void setRowSpan( int rowSpan )
    {
		data.setRowSpan( rowSpan );
    }

    public int getRowSpanInDesign( )
    {
		return data.getRowSpanInDesign( );
    }
    
	public void decreasRowSpanInDesign( )
	{
		data.decreasRowSpanInDesign( );
	}

	public float getHeight( )
	{
		return data.getHeight( );
	}

	public void setHeight( float height )
	{
		data.setHeight( height );
	}

	public int getStartX( )
	{
		return data.getStartX( );
	}

	public int getEndX( )
	{
		return data.getEndX( );
	}

	public Type getType( )
	{
		return type;
	}

	public void setType( Type type )
	{
		this.type = type;
	}
}
