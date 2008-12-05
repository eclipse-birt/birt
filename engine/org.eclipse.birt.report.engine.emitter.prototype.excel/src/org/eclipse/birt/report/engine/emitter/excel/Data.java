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

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.emitter.excel.layout.XlsContainer;



public class Data extends SheetData implements Serializable, Cloneable
{

	private static final long serialVersionUID = -316995334044186083L;

	private static int ID = 0;

	// String txt;

	int  id;



	BookmarkDef bookmark;

	boolean isTxtData = true;
	

	Logger log = Logger.getLogger( Data.class.getName( ) );

	public Data( final Object txt, final int datatype, XlsContainer container )
	{
		this( txt, null, datatype, container );
	}

	public Data( final Object txt, final StyleEntry s, final int datatype,
			XlsContainer container )
	{
		this( txt, s, datatype, container, 0 );
	}

	public Data( final Object txt, final StyleEntry s, final int datatype,
			XlsContainer container, int rowSpanOfDesign )
	{
		this.txt = txt;
		this.style = s;
		this.datatype = datatype;
		id = ID++;
		this.container = container;
		this.rowSpanInDesign = 0;
	}

	protected void setNotTxtData( )
	{
		this.isTxtData = false;
	}

	public Object getText( )
	{
		if ( txt == null )
			return " ";
		return txt;
	}

	public void formatTxt( )
	{
		if ( txt == null )
		{
			return;
		}
		else if ( datatype == SheetData.DATE )
		{
			txt = ExcelUtil.formatDate( txt );
		}
		else if ( datatype == SheetData.NUMBER )
		{
			Number number = (Number) txt;
			if ( ExcelUtil.isBigNumber( number ) )
			{
				txt = ExcelUtil.formatNumberAsScienceNotation( number );
			}
			else if ( number.toString( ).length( ) > 31 )
			{
				if ( ExcelUtil.displayedAsScientific( number ) )
				{
					txt = ExcelUtil.formatNumberAsScienceNotation( number );
				}
				else
				{
					txt = ExcelUtil.formatNumberAsDecimal( number );
				}
			}
		}
	}

	public boolean isBigNumber( )
	{
		if ( txt == null )
		{
			return false;
		}
		else if ( datatype == Data.NUMBER )
		{
			return ExcelUtil.isBigNumber( txt );
		}
		return false;
	}

	public boolean isInfility( )
	{
		if ( txt == null )
		{
			return false;
		}
		else if ( datatype == SheetData.NUMBER )
		{
			return ExcelUtil.isInfinity( txt );
		}
		return false;
	}

	public Object getValue( )
	{
		return txt;
	}

	public int hashCode( )
	{
		return id;
	}
//TODO:remove this method
	// shallow copy is necessary and sufficient
	protected Object clone( )
	{
		Object o = null;
		try
		{
			o = super.clone( );
		}
		catch ( final CloneNotSupportedException e )
		{
			log.log( Level.WARNING, "clone data failed" );
			// e.printStackTrace( );
		}
		return o;
	}

	public boolean equals( final Object o )
	{
		if ( o == this )
		{
			return true;
		}
		if ( !( o instanceof Data ) )
		{
			return false;
		}
		final Data data = (Data) o;
		if ( data.id == id )
		{
			return true;
		}
		return false;
	}



	public BookmarkDef getBookmark( )
	{
		return bookmark;
	}

	public void setBookmark( BookmarkDef bookmark )
	{
		this.bookmark = bookmark;
	}

}