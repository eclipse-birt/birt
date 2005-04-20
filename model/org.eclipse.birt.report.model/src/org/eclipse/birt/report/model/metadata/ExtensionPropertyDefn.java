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

	private String groupName = null;

	/**
	 * The default display name, which is used when the localized string is not
	 * found with I18N feature.
	 */

	private String defaultDisplayName = null;

	/**
	 * The default display name for property group, which is used when the
	 * localized string is not found with I18N feature.
	 */

	private String groupDefauleDisplayName = null;

	/**
	 * Sets the group name of this property definition.
	 * 
	 * @param groupName
	 *            the group name to set
	 */

	public void setGroupName( String groupName )
	{
		this.groupName = groupName;
	}

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
		if ( groupNameKey != null )
		{
			if ( messages != null )
			{
				String displayName = messages.getMessage( groupNameKey,
						ThreadResources.getLocale( ) );
				if ( !StringUtil.isBlank( displayName ) )
					return displayName;
			}
		}

		if ( groupDefauleDisplayName != null )
			return groupDefauleDisplayName;

		return groupName;
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

		if ( defaultDisplayName != null )
			return defaultDisplayName;

		return getName( );
	}

	/**
	 * Sets the default display name.
	 * 
	 * @param defaultDisplayName
	 *            the default display name to set
	 */

	public void setDefaultDisplayName( String defaultDisplayName )
	{
		this.defaultDisplayName = defaultDisplayName;
	}

	/**
	 * Sets the default display name for property group
	 * 
	 * @param groupDefauleDisplayName
	 *            the default display name for property group to set
	 */

	public void setGroupDefauleDisplayName( String groupDefauleDisplayName )
	{
		this.groupDefauleDisplayName = groupDefauleDisplayName;
	}

}