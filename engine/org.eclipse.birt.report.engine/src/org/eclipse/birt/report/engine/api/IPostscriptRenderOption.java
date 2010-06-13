/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;


public interface IPostscriptRenderOption extends IRenderOption
{
	public static final String PS_LEVEL = "psLevel";

	public static final String OPTION_PAPER_SIZE = "OptionPagerSize";

	public static final String OPTION_PAPER_TRAY = "OptionPageTray";

	public static final String OPTION_DUPLEX = "OptionDuplex";

	public static final String OPTION_COPIES = "OptionCopies";

	public static final String OPTION_COLLATE = "OptionCollate";

	public static final String OPTION_RESOLUTION = "OptionResolution";

	public static final String OPTION_GRAY = "OptionGray";

	public static final String OPTION_SCALE = "OptionScale";

	public static final int TRAYCODE_TRAY1 = 0;
	public static final int TRAYCODE_TRAY2 = 1;
	public static final int TRAYCODE_TRAY3 = 2;
	public static final int TRAYCODE_TRAY4 = 3;
	public static final int TRAYCODE_AUTO = -1;
	public static final int TRAYCODE_MANUAL = -2;

	/**
	 * Sets postscript level.
	 * 
	 * @param level
	 */
	void setPostscriptLevel( int level );

	/**
	 * Gets postscript level.
	 */
	int getPostscriptLevel( );

	void setPaperSize( String paperSize );

	String getPaperSize( );

	void setPaperTray( int paperTrayCode );

	int getPaperTray( );

	void setDuplex( String duplex );

	String getDuplex( );

	void setCopies( int copies );

	int getCopies( );

	void setCollate( boolean collate );

	boolean getCollate( );

	void setResolution( int resolution );
	
	int getResolution( );

	void setGray( boolean gray );

	boolean getGray( );

	void setScale( int scale );

	int getScale( );
}
