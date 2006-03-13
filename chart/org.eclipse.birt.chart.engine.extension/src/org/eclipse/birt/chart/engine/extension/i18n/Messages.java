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

package org.eclipse.birt.chart.engine.extension.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ibm.icu.util.ULocale;

/**
 * 
 */
public final class Messages
{

	public static final String ENGINE_EXTENSION = "org.eclipse.birt.chart.engine.extension.i18n.messages"; //$NON-NLS-1$

	private Messages( )
	{
	}

	/**
	 * @param key
	 * @param lcl
	 */
	public static String getString( String key, ULocale lcl )
	{
		final ResourceBundle rb = ResourceBundle.getBundle( ENGINE_EXTENSION,
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
		final ResourceBundle rb = ResourceBundle.getBundle( ENGINE_EXTENSION,
				lcl.toLocale( ) );
		try
		{
			return MessageFormat.format( rb.getString( key ), oa );
		}
		catch ( MissingResourceException e )
		{
			return '!' + key + '!';
		}
	}
}
