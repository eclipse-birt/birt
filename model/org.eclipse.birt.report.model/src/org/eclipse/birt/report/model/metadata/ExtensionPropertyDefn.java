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

import org.eclipse.birt.report.model.extension.IMessages;
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
		assert messages != null;

		this.messages = messages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#getGroupName()
	 */
	public String getGroupName( )
	{
		if ( groupNameKey != null )
			return messages.getMessage( groupNameKey, ThreadResources.getLocale( ) );
		
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getDisplayName()
	 */
	public String getDisplayName( )
	{
		if ( displayNameID != null )
			return messages
				.getMessage( displayNameID, ThreadResources.getLocale( ) );
		
		return ""; //$NON-NLS-1$
	}
}