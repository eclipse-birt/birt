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
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Represents the extension element definition for peer extension support. The
 * details of peer extension, please refer to
 * {@link org.eclipse.birt.report.model.extension.PeerExtensibilityProvider}.
 * This class is only used for those extension element definition from
 * third-party, not the BIRT-defined standard elements. The extension element
 * definition must include an instance of
 * {@link org.eclipse.birt.report.model.api.extension.IReportItemFactory}. The
 * included IElmentFactory gives the information about the internal model
 * properties of the extension element, how to instantiate
 * {@link org.eclipse.birt.report.model.api.extension.IReportItem}and other
 * information.
 */

public final class PeerExtensionElementDefn extends ExtensionElementDefn
{

	/**
	 * The element factory of the extended element.
	 */

	protected IReportItemFactory reportItemFactory = null;

	/**
	 * Constructs the peer extension element definition with the element
	 * definition name and report item factory.
	 * 
	 * @param name
	 *            the name of the extension element definition
	 * @param reportItemFactory
	 *            the report item factory of the extension element
	 */

	public PeerExtensionElementDefn( String name,
			IReportItemFactory reportItemFactory )
	{
		assert name != null;
		assert reportItemFactory != null;
		this.name = name;
		this.reportItemFactory = reportItemFactory;
	}

	/**
	 * Gets the report item factory of this extension element definition.
	 * 
	 * @return the report item factory of the extension element definition
	 */

	public IReportItemFactory getReportItemFactory( )
	{
		return reportItemFactory;
	}

	/*
	 * Returns the localized display name, if non-empty string can be found with
	 * resource key and <code> IMessages </code> . Otherwise, return name of
	 * this element definition.
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ObjectDefn#getDisplayName()
	 */

	public String getDisplayName( )
	{
		if ( displayNameKey != null && reportItemFactory != null )
		{
			IMessages messages = reportItemFactory.getMessages( );

			if ( messages != null )
			{
				String displayName = messages.getMessage( displayNameKey,
						ThreadResources.getLocale( ) );

				if ( !StringUtil.isBlank( displayName ) )
					return displayName;
			}
		}

		return getName( );
	}
}
