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

package org.eclipse.birt.chart.reportitem.ui.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * Messages
 */
public class Messages
{

	private static final String REPORT_ITEM_UI = "org.eclipse.birt.chart.reportitem.ui.i18n.nls";//$NON-NLS-1$

	private static final ResourceBundle REPORT_ITEM_BUNDLE = UResourceBundle.getBundleInstance( REPORT_ITEM_UI,
			ULocale.getDefault( ),
			Messages.class.getClassLoader( ) );

	private Messages( )
	{
	}

	public static ResourceBundle getResourceBundle( )
	{
		return REPORT_ITEM_BUNDLE;
	}

	public static ResourceBundle getResourceBundle( ULocale locale )
	{
		return UResourceBundle.getBundleInstance( REPORT_ITEM_UI,
				locale,
				Messages.class.getClassLoader( ) );
	}

	/**
	 * @param key
	 */
	public static String getString( String key )
	{
		try
		{
			return REPORT_ITEM_BUNDLE.getString( key );
		}
		catch ( MissingResourceException e )
		{
			return '!' + key + '!';
		}
	}

	/**
	 * 
	 * @param key
	 *            key
	 * @param oas
	 *            arguments
	 */
	public static String getString( String key, Object[] oas )
	{
		try
		{
			return MessageFormat.format( REPORT_ITEM_BUNDLE.getString( key ),
					oas );
		}
		catch ( MissingResourceException e )
		{
			return '!' + key + '!';
		}
	}

	/**
	 * 
	 * @param key
	 *            key
	 * @param oa
	 *            single argument
	 */
	public static String getString( String key, Object oa )
	{
		try
		{
			return MessageFormat.format( REPORT_ITEM_BUNDLE.getString( key ),
					new Object[]{
						oa
					} );
		}
		catch ( MissingResourceException e )
		{
			return '!' + key + '!';
		}
	}
}