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

package org.eclipse.birt.report.resource;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class that handle externalized string used by the Birt viewer.
 * <p>
 */
public class BirtResources
{
	/**
	 * Resource bundle.
	 */
	private static ResourceBundle bundle = null;
	
	/**
	 * Initialize resources bundle.
	 */
	public synchronized static void initResource( Locale locale )
	{
		try
		{
			bundle = ResourceBundle.getBundle( BirtResources.class.getName( ), locale );
		}
		catch ( MissingResourceException x )
		{
			bundle = null;
		}
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 * 
	 * @param key resource key
	 * @return resource string
	 */
	public static String getString( String key )
	{
		try
		{
			return ( bundle != null ) ? bundle.getString( key ) : key;
		}
		catch ( MissingResourceException e )
		{
			return key;
		}
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 * 
	 * @param key resource key
	 * @param arguments list of arguments
	 * @return locale string
	 */
	public static String getFormattedString( String key, Object[] arguments )
	{
		return MessageFormat.format( getString( key ), arguments );
	}
}
