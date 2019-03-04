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

package org.eclipse.birt.report.designer.internal.ui.views.property.widgets;

/**
 * Store the TimeDialog infomation
 */
public class TimeDialogInfo implements DialogInfo
{

	private static final long serialVersionUID = 1L;

	private long time;

	private String timeZoneID = ""; //$NON-NLS-1$

	private String format = TimeFormat.DATE_TIME;

	/**
	 * Sets the time
	 * 
	 * @param time
	 */
	public void setTime( long time )
	{
		this.time = time;
	}

	/**
	 * Sets the TimeZone ID
	 * 
	 * @param id
	 */
	public void setTimeZoneID( String id )
	{
		this.timeZoneID = id;
	}

	/**
	 * Gets the time
	 * 
	 * @return time
	 */
	public long getTime( )
	{
		return time;
	}

	/**
	 * Gets the TimeZone ID
	 * 
	 * @return time zone id
	 */
	public String getTimeZoneID( )
	{
		return timeZoneID;
	}

	/**
	 * Sets the format
	 * 
	 * @param format
	 */
	public void setFormat( String format )
	{
		this.format = format;
	}

	/**
	 * Gets the format
	 * 
	 * @return the format
	 */
	public String getFormat( )
	{
		return format;
	}

}