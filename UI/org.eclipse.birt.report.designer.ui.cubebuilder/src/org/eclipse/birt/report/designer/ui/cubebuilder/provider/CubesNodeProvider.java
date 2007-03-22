/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.provider;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.action.NewCubeAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstancts;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;


public class CubesNodeProvider extends DefaultNodeProvider
{

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry and adds the action to the given menu.
	 * 
	 * @param menu
	 *            the menu
	 * @param object
	 *            the object
	 */
	public void createContextMenu( TreeViewer sourceViewer, Object object,
			IMenuManager menu )
	{
		NewCubeAction action = new NewCubeAction( Messages.getString( "cube.new" ) );//$NON-NLS-1$
		menu.add( action );
		super.createContextMenu( sourceViewer, object, menu );

	}

	public Object[] getChildren( Object model )
	{
		return ( (SlotHandle) model ).getElementHandle( )
				.getModuleHandle( )
				.getVisibleCubes( )
				.toArray( );
	}

	/**
	 * Gets the display name of the node.
	 * 
	 * @param model
	 *            the object
	 */
	public String getNodeDisplayName( Object object )
	{
		return Messages.getString( "DefaultNodeProvider.Tree.Cubes" );
	}

	public Image getNodeIcon( Object model )
	{
		return UIHelper.getImage( BuilderConstancts.IMAGE_CUBES );
	}
}