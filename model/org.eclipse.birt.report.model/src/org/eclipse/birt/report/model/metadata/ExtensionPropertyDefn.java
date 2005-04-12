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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Represents the definition of extension property.
 */

public class ExtensionPropertyDefn extends SystemPropertyDefn
{

	private IMessages messages = null;

	/**
	 * Constructs the property definition with <code>IMessages</code> for
	 * extension property.
	 * 
	 * @param messages
	 *            the messages which can return localized message for resource
	 *            key and locale
	 */

	public ExtensionPropertyDefn( IMessages messages )
	{
		this.messages = messages;
	}

	/*
	 * Returns the localized group name, if non-empty string can be found with
	 * resource key and <code> IMessages </code> . Otherwise, return <code> null
	 * </code> .
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#getGroupName()
	 */
	public String getGroupName( )
	{
		if ( groupNameKey != null && messages != null )
			return messages.getMessage( groupNameKey, ThreadResources
					.getLocale( ) );

		return null;
	}

	/*
	 * Returns the localized display name, if non-empty string can be found with
	 * resource key and <code> IMessages </code> . Otherwise, return name of
	 * this property definition.
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getDisplayName()
	 */
	public String getDisplayName( )
	{
		if ( displayNameID != null && messages != null )
		{
			String displayName = messages.getMessage( displayNameID,
					ThreadResources.getLocale( ) );
			if ( !StringUtil.isBlank( displayName ) )
				return displayName;
		}

		return getName( );
	}
}