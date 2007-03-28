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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import java.util.List;

import org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.jface.action.IMenuManager;

/**
 * Creata the Cross tab menu
 */
// TODO In the SchematicContextMenuProvider class, cover the adapter to the
// ExtendedItemHandle
public class CrosstabMenuBuilder implements IMenuBuilder
{

	// TODO the Crosstab must dedeine a virble goable.
	/**
	 * Constructor
	 */
	public CrosstabMenuBuilder( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder#buildMenu(org.eclipse.jface.action.IMenuManager,
	 *      java.util.List)
	 */
	public void buildMenu( IMenuManager menu, List selectedList )
	{
		if ( selectedList != null
				&& selectedList.size( ) == 1
				&& selectedList.get( 0 ) instanceof ExtendedItemHandle )
		{
			// for ctross tab test
			ExtendedItemHandle handle = (ExtendedItemHandle) selectedList.get( 0 );
			if ( handle.getExtensionName( ).equals( "Crosstab" ) ) //$NON-NLS-1$
			{
				//TODO add the xross repport item action
			}
		}

	}

}
