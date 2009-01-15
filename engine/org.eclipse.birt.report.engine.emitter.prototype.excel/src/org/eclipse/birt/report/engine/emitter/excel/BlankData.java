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

	/**
	 * 
	 */
	private static final long serialVersionUID = 6843853284940969059L;

	private SheetData data;
	
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
    	if ( data != null )
    	{
    		return data.getRowSpan( );
    	}
    	return 0;
    }
    
    public void setRowSpan( int rowSpan )
    {
    	if ( data != null )
    	{
    		data.setRowSpan( rowSpan );
    	}
    }
    public int getRowSpanInDesign( )
    {
    	if ( data != null )
    	{
    		return data.getRowSpanInDesign( );
    	}
    	return 0;
    }
    
	public void decreasRowSpanInDesign( )
	{
		if ( data != null )
		{
			data.decreasRowSpanInDesign( );
		}
	}
}
