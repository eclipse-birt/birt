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

package org.eclipse.birt.report.designer.internal.ui.extension.experimental;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * 
 */

public class PaletteEntryExtension
{

	private String itemName;
	private String label;
	private String description;
	private ImageDescriptor icon;
	private ImageDescriptor iconLarge;
	private String category;
	private String command;

	public String getLabel( )
	{
		return label;
	}

	public void setLabel( String label )
	{
		this.label = label;
	}

	public String getDescription( )
	{
		return description;
	}

	public void setDescription( String description )
	{
		this.description = description;
	}

	public ImageDescriptor getIcon( )
	{
		return icon;
	}

	public void setIcon( ImageDescriptor icon )
	{
		this.icon = icon;
	}

	public ImageDescriptor getIconLarge( )
	{
		return iconLarge;
	}

	public void setIconLarge( ImageDescriptor iconLarge )
	{
		this.iconLarge = iconLarge;
	}

	public String getCategory( )
	{
		return category;
	}

	public void setCategory( String category )
	{
		this.category = category;
	}

	public String getCommand( )
	{
		return command;
	}

	public void setCommand( String command )
	{
		this.command = command;
	}

	public Object executeCreate( ) throws Exception
	{
		if ( getCommand( ) != null )
		{
			return CommandUtils.executeCommand( getCommand( ));
		}
		throw new Exception( "create command not specail." );
	}

	public String getItemName( )
	{
		return itemName;
	}

	public void setItemName( String itemName )
	{
		this.itemName = itemName;
	}
}
