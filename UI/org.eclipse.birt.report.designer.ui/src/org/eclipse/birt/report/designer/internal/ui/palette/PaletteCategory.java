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

package org.eclipse.birt.report.designer.internal.ui.palette;

import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.core.runtime.Assert;

/**
 *  The palette entry used to describe the category on the palette view
 */

public class PaletteCategory extends PaletteDrawer
{

	/** The category name */
	private String name;

	/**
	 * 
	 * 
	 * @param name
	 * @param displayLabellabel
	 * @param icon
	 */
	public PaletteCategory( String name, String displayLabel,
			ImageDescriptor icon )
	{
		super( displayLabel, icon );
		Assert.isNotNull( name );
		this.name = name;
	}

	/**
	 * Gets the category name
	 * 
	 * @return Returns the category name
	 */
	public String getCategoryName( )
	{
		return name;
	}

}