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
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Represents one resource bundle for specific locale. This class is just the
 * convenient wrapper for <code>ResourceBundle</code>.
 * 
 * @see ThreadResources
 */

public class ResourceHandle
{

	/**
	 * The actual resource bundle.
	 */

	protected ResourceBundle resourceBundle;

	/**
	 * Constructs the resource handle with a specific resource bundle, which is
	 * associated with locale.
	 * 
	 * @param resourceBundle
	 *            the resource bundle
	 */

	public ResourceHandle( ResourceBundle resourceBundle )
	{
		this.resourceBundle = resourceBundle;
	}

	/**
	 * Gets the localized message given the message key.
	 * 
	 * @param key
	 *            the resource key
	 * @return the localized message for that key. If the resource key is not
	 *         found in in this resource bundle, the key is returned.
	 * @throws IllegalArgumentException
	 *             if the key is <code>null</code>.
	 * @see ResourceBundle#getString( String )
	 */

	public String getMessage( String key )
	{
		if ( key == null )
			//return ("This message indicates key is null, please solve it");
			throw new IllegalArgumentException(
					"The resource key shouldn't be null." ); //$NON-NLS-1$

		try
		{
			return resourceBundle.getString( key );
		}
		catch ( MissingResourceException e )
		{
			return key;
		}
	}

	/**
	 * Gets the localized message with arguments.
	 * 
	 * @param key
	 *            the resource key
	 * @param arguments
	 *            the set of arguments to place the place-holder in the message
	 * @return the localized message for that key.If the resource key is not
	 *         found in in this resource bundle, the key is returned.
	 * @throws IllegalArgumentException
	 *             if the key is <code>null</code>.
	 */

	public String getMessage( String key, Object[] arguments )
	{
		String message = getMessage( key );
		return MessageFormat.format( message, arguments );
	}
}