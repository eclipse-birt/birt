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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.ibm.icu.text.MessageFormat;

/**
 * Class that handle externalized string used by the Birt viewer.
 * <p>
 */
public class BirtResources
{

	/**
	 * List of resource bundles keyed by the locale, which is cached for the
	 * whole application.
	 */

	private static Map resourceMap = new HashMap( );

	/**
	 * Thread-local variable for the current thread.
	 */

	private static ThreadLocal threadLocal = new ThreadLocal( );


	/**
	 * Sets the locale of current user-thread. This method should be called
	 * before access to any localized message. If the locale is
	 * <code>null</code>, the default locale will be set.
	 * 
	 * @param locale
	 *            locale of the current thread.
	 */

	public static void setLocale( Locale locale )
	{
		if ( locale == null )
			threadLocal.set( Locale.getDefault( ) );
		else
			threadLocal.set( locale );
	}

	/**
	 * Gets the locale of current user-thread.
	 * 
	 * @return the locale of the current thread.
	 */

	public static Locale getLocale( )
	{
		Locale locale = (Locale) threadLocal.get( );
		if ( locale == null )
			locale = Locale.getDefault( );
		return locale;
	}

	/**
	 * Gets the localized message with the resource key.
	 * 
	 * @param key
	 *            the resource key
	 * @return the localized message for that key. Returns the key itself if the
	 *         message was not found.
	 */

	public static String getMessage( String key )
	{
		ViewerResourceHandle resourceHandle = getResourceHandle( );
		if ( resourceHandle != null )
			return resourceHandle.getMessage( key );

		return key;
	}

	/**
	 * Gets the localized message with the resource key and arguments.
	 * 
	 * @param key
	 *            the resource key
	 * @param arguments
	 *            the set of arguments to place the place-holder of message
	 * @return the localized message for that key and the locale set in the
	 *         constructor. Returns the key itself if the message was not found.
	 */

	public static String getMessage( String key, Object[] arguments )
	{
		ViewerResourceHandle resourceHandle = getResourceHandle( );
		if ( resourceHandle != null )
			return resourceHandle.getMessage( key, arguments );

		return key;
	}

	/**
	 * Returns the resource handle with the locale of this thread. The resource
	 * handle will be cached.
	 * 
	 * @return the resource handle with the locale of this thread
	 */

	private static ViewerResourceHandle getResourceHandle( )
	{
		Locale locale = getLocale( );

		ViewerResourceHandle resourceHandle = (ViewerResourceHandle) resourceMap
				.get( locale );
		if ( resourceHandle != null )
			return resourceHandle;

		synchronized ( resourceMap )
		{
			if ( resourceMap.get( locale ) != null )
				return (ViewerResourceHandle) resourceMap.get( locale );

			resourceHandle = new ViewerResourceHandle( locale );
			resourceMap.put( locale, resourceHandle );
		}

		return resourceHandle;
	}
	

}
