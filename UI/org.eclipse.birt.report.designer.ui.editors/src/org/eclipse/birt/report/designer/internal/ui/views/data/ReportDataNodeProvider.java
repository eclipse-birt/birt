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

package org.eclipse.birt.report.designer.internal.ui.views.data;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.model.api.ModuleHandle;

/**
 * Root node - Report design node provider - Implements the getChildren -
 * Implements the getNodeDiplayName
 * 
 * 
 */
public class ReportDataNodeProvider extends DefaultNodeProvider
{

	/**
	 * Gets the children of the given model. The default children element
	 * include following: Body,Styles,MasterPage
	 * 
	 * @param model
	 *            the given report design
	 * @return the result list that contains the model
	 */
	public Object[] getChildren( Object model )
	{
		ModuleHandle handle = ( (ModuleHandle) model );

		ArrayList list = new ArrayList( );

		list.add( handle.getDataSources( ) );

		list.add( handle.getDataSets( ) );

		list.add( handle.getParameters( ) );

		return list.toArray( );
	}

	
}