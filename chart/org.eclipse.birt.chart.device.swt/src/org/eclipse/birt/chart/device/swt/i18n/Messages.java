/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.swt.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ibm.icu.util.ULocale;

/**
 * Provides useful methods to retrieve localized text for the
 * org.eclipse.birt.chart.device.extension plug-in classes
 */
public final class Messages
{

	public static final String DEVICE_SWT= new String( "org.eclipse.birt.chart.device.swt.i18n.messages" ); //$NON-NLS-1$

	private Messages( )
	{
	}

	/**
	 * @param key
	 * @param lcl
	 */
	public static String getString( String key, ULocale lcl )
	{
		final ResourceBundle rb = ResourceBundle.getBundle( DEVICE_SWT,
				lcl.toLocale( ) );
		try
		{
			return rb.getString( key );
		}
		catch ( MissingResourceException e )
		{
			return '!' + key + '!';
		}
	}

	/**
	 * @param key
	 * @param oa
	 * @param lcl
	 */
	public static String getString( String key, Object[] oa, ULocale lcl )
	{
		final ResourceBundle rb = ResourceBundle.getBundle( DEVICE_SWT,
				lcl.toLocale( ) );
		try
		{
			return MessageFormat.format( rb.getString( key ), oa );
		}
		catch ( MissingResourceException e )
		{
			e.printStackTrace( );
			return '!' + key + '!';
		}
	}
}