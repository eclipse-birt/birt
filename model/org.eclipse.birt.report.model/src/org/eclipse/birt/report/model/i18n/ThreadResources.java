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

package org.eclipse.birt.report.model.i18n;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Provides access to a resource bundle associated with this thread. The
 * application calls <code>setThreadLocale</code> to set the locale for the
 * thread, then calls the <code>getMessage</code> methods.
 * 
 * @see ResourceHandle
 */

public class ThreadResources
{

	/**
	 * List of resource bundles keyed by the locale.
	 */

	private static Map resourceMap = new HashMap( );

	/**
	 * List of resource bundles keyed by the current user-thread.
	 */

	private static ThreadLocal resources = new ThreadLocal( );

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
			resources.set( Locale.getDefault( ) );
		else
			resources.set( locale );
	}

	/**
	 * Gets the locale of current user-thread.
	 * 
	 * @return Locale of the current thread.
	 */

	public static Locale getLocale( )
	{
		return (Locale) resources.get( );
	}

	/**
	 * Gets a message given the message key. An assertion will be raised if the
	 * message key does not exist in the resource bundle. The locale must have
	 * previously been set for this thread.
	 * 
	 * @param key
	 *            the message key
	 * @return the localized message for that key and the locale set in the
	 *         constructor. Returns the key itself if the message was not found.
	 * @see ResourceBundle#getString( String )
	 * @see ResourceHandle#getMessage( String )
	 */

	public static String getMessage( String key )
	{
		ResourceHandle resourceHandle = getResourceHandle( );
		assert resourceHandle != null;

		return resourceHandle.getMessage( key );
	}

	/**
	 * Gets a message that has placeholders. An assertion will be raised if the
	 * message key does not exist in the resource bundle. The locale must have
	 * previously been set for this thread.
	 * 
	 * @param key
	 *            the message key
	 * @param arguments
	 *            the set of arguments to be plugged into the message
	 * @return the localized message for that key and the locale set in the
	 *         constructor. Returns the key itself if the message was not found.
	 * @see ResourceBundle#getString( String )
	 * @see MessageFormat#format( String, Object[] )
	 * @see ResourceHandle#getMessage( String, Object[] )
	 */

	public static String getMessage( String key, Object[] arguments )
	{
		ResourceHandle resourceHandle = getResourceHandle( );
		assert resourceHandle != null;

		return resourceHandle.getMessage( key, arguments );
	}

	/**
	 * Returns the resource handle with the locale of this thread. The resource
	 * handle will be cached.
	 * 
	 * @return the resource handle with the locale of this thread
	 */

	private static ResourceHandle getResourceHandle( )
	{
		Locale locale = (Locale) resources.get( );
		assert locale != null;

		ResourceHandle resourceHandle = (ResourceHandle) resourceMap
				.get( locale );
		if ( resourceHandle != null )
			return resourceHandle;

		String className = ThreadResources.class.getName( );
		String bundleName = className.substring( 0,
				className.lastIndexOf( '.' ) + 1 )
				+ ResourceHandle.BUNDLE_NAME;
		ResourceBundle resourceBundle = ResourceBundle.getBundle( bundleName,
				locale );
		assert resourceBundle != null : "ResourceBundle : " + bundleName + " for " + locale + " not found"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

		resourceHandle = new ResourceHandle( resourceBundle );

		synchronized ( resourceMap )
		{
			resourceMap.put( locale, resourceHandle );
		}

		return resourceHandle;
	}

}