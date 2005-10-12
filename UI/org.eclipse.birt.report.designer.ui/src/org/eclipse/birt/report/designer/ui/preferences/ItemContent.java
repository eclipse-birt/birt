/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

/**
 * Class used as a trivial case of an item of the table Serves as the business
 * object for the TableViewer.
 * 
 * An item has the following properties: Default name, Custom name and
 * Description.
 */

public class ItemContent
{

	private String defaultName = ""; //$NON-NLS-1$
	private String customName = ""; //$NON-NLS-1$
	private String description = ""; //$NON-NLS-1$

	public ItemContent( String string )
	{
		super( );
		this.setCustomName( string );
	}

	public String getDefaultName( )
	{
		return defaultName;
	}

	public void setDefaultName( String string )
	{
		defaultName = string.trim( );
	}

	public String getCustomName( )
	{
		return customName;
	}

	public void setCustomName( String string )
	{
		customName = string.trim( );
	}

	public String getDescription( )
	{
		return description;
	}

	public void setDescription( String string )
	{
		description = string.trim( );
	}

}
