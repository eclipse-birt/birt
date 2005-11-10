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

package org.eclipse.birt.chart.engine.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Provides useful methods to retrieve localized text for the
 * org.eclipse.birt.chart.device.extension plug-in classes
 */
public final class Messages
{

	/**
	 * Bundle name
	 */
	public static final String ENGINE = new String( "org.eclipse.birt.chart.engine.i18n.messages" ); //$NON-NLS-1$

	private Messages( )
	{
	}

	/**
	 * @param key
	 * @return
	 */
	public static String getString( String key )
	{
		return getString( key, Locale.getDefault( ) );
	}

	/**
	 * @param key
	 * @param lcl
	 * @return
	 */
	public static String getString( String key, Locale lcl )
	{
		final ResourceBundle rb = ResourceBundle.getBundle( ENGINE, lcl );
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
	 * 
	 * @param key key
	 * @param oa single argument
	 */
	public static String getString( String key, Object oa, Locale lcl )
	{
		return getString( key, new Object[]{oa}, lcl );
	}
	/**
	 * @param key
	 * @param oa
	 * @param lcl
	 * @return
	 */
	public static String getString( String key, Object[] oa, Locale lcl )
	{
		final ResourceBundle rb = ResourceBundle.getBundle( ENGINE, lcl );
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
