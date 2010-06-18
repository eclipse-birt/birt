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

package org.eclipse.birt.report.engine.emitter.postscript;

import org.eclipse.birt.report.engine.api.IPostscriptRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;


public class PostscriptRenderOption extends RenderOption
		implements
			IPostscriptRenderOption
{

	/**
	 * Sets postscript level.
	 * 
	 * @param level
	 */
	public void setPostscriptLevel( int level )
	{
		options.put( PS_LEVEL, new Integer( level ) );
	}

	/**
	 * Gets postscript level.
	 */
	public int getPostscriptLevel( )
	{
		return getIntOption( PS_LEVEL, 1 );
	}

	public void setPaperSize( String paperSize )
	{
		options.put( OPTION_PAPER_SIZE, paperSize );
	}

	public String getPaperSize( )
	{
		return getStringOption( OPTION_PAPER_SIZE );
	}

	public void setPaperTray( int paperTrayCode )
	{
		options.put( OPTION_PAPER_TRAY, paperTrayCode );
	}

	public int getPaperTray( )
	{
		return getIntOption( OPTION_PAPER_TRAY,
				IPostscriptRenderOption.TRAYCODE_AUTO );
	}

	public void setDuplex( String duplex )
	{
		options.put( OPTION_DUPLEX, duplex );
	}

	public String getDuplex( )
	{
		return getStringOption( OPTION_DUPLEX );
	}

	public void setCopies( int copies )
	{
		options.put( OPTION_COPIES, copies );
	}

	public int getCopies( )
	{
		return getIntOption( OPTION_COPIES, 1 );
	}

	public void setCollate( boolean collate )
	{
		options.put( OPTION_COLLATE, collate );
	}

	public boolean getCollate( )
	{
		return getBooleanOption( OPTION_COLLATE, false );
	}

	public void setResolution( String resolution )
	{
		options.put( OPTION_RESOLUTION, resolution );
	}

	public String getResolution( )
	{
		return getStringOption( OPTION_RESOLUTION );
	}

	public void setColor( boolean color )
	{
		options.put( OPTION_COLOR, color );
	}

	public boolean getColor( )
	{
		return getBooleanOption( OPTION_COLOR, true );
	}

	public void setScale( int scale )
	{
		options.put( OPTION_SCALE, scale );
	}

	public int getScale( )
	{
		return getIntOption( OPTION_SCALE, 100 );
	}
}
