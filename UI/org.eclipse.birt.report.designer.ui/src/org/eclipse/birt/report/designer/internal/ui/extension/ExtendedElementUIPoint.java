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

package org.eclipse.birt.report.designer.internal.ui.extension;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.ui.IPreferenceConstants;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemUI;
import org.eclipse.jface.util.Assert;

/**
 * The object used to cache the UI extension points
 */

public class ExtendedElementUIPoint
{

	private String extensionName;
	private IReportItemUI reportItemUI = null;
	private Map attributesMap = new HashMap( 5 );

	/**
	 * Construct an new instance with the given extension name. All default
	 * value will be initialized.
	 * 
	 * @param extensionName
	 *            the extension name of the extended element
	 */
	ExtendedElementUIPoint( String extensionName )
	{
		this.extensionName = extensionName;

		//Default value
		setAttribute( IExtensionConstants.EDITOR_SHOW_IN_DESIGNER, Boolean.TRUE );
		setAttribute( IExtensionConstants.EDITOR_SHOW_IN_MASTERPAGE,
				Boolean.TRUE );
		setAttribute( IExtensionConstants.EDITOR_CAN_RESIZE, Boolean.TRUE );
		setAttribute( IExtensionConstants.PALETTE_CATEGORY,
				IPreferenceConstants.PALETTE_CONTENT );
	}

	/**
	 * Gets the extension name of the element
	 * 
	 * @return Returns the extension name;
	 */
	public String getExtensionName( )
	{
		return extensionName;
	}

	/**
	 * Gets the UI instance of the element
	 * 
	 * @return Returns the UI instance;
	 */
	public IReportItemUI getReportItemUI( )
	{
		return reportItemUI;
	}

	/**
	 * Gets the corresponding attribute of the key of the extended element
	 * 
	 * @param key
	 *            the key of the attribute.It cannot be null
	 * @return Returns the corresponding attribute, or null if the key is
	 *         invalid or the corresponding attribute hasn't been set
	 */
	public Object getAttribute( String key )
	{
		Assert.isLegal( key != null );
		return attributesMap.get( key );
	}

	/**
	 * Sets the UI instance of the element
	 * 
	 * @param reportItemUI
	 *            the UI instance to set.It cannot be null
	 */
	void setReportItemUI( IReportItemUI reportItemUI )
	{
		Assert.isLegal( reportItemUI != null );
		this.reportItemUI = reportItemUI;
	}

	/**
	 * Sets the corresponding attribute of the key of the extended element
	 * 
	 * @param key
	 *            the key of the attribute.It cannot be null
	 */
	void setAttribute( String key, Object value )
	{
		Assert.isLegal( key != null );
		attributesMap.put( key, value );
	}
}