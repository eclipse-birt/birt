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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.util.SecurityUtil;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * 
 */
public final class Messages
{

	private static final String ENGINE_EXTENSION = "org.eclipse.birt.chart.engine.extension.i18n.nls"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = UResourceBundle.getBundleInstance( ENGINE_EXTENSION,
			ULocale.getDefault( ),
			SecurityUtil.getClassLoader( Messages.class ) );

	private Messages( )
	{
	}

	public static ResourceBundle getResourceBundle( )
	{
		return RESOURCE_BUNDLE;
	}

	public static ResourceBundle getResourceBundle( ULocale locale )
	{
		return UResourceBundle.getBundleInstance( ENGINE_EXTENSION,
				locale,
				SecurityUtil.getClassLoader( Messages.class ) );
	}

	/**
	 * @param key
	 * @param lcl
	 */
	public static String getString( String key, ULocale lcl )
	{
		try
		{
			return getResourceBundle( lcl ).getString( key );
		}
		catch ( MissingResourceException e )
		{
			return '!' + key + '!';
		}
	}

	public static String getString( String key )
	{
		try
		{
			return getResourceBundle( ).getString( key );
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
		try
		{
			return SecurityUtil.formatMessage( getResourceBundle( lcl ).getString( key ),
					oa );
		}
		catch ( MissingResourceException e )
		{
			return '!' + key + '!';
		}
	}
}
