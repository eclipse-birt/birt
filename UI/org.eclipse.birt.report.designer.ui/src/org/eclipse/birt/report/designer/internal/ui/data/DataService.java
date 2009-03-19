/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.data;

import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;

/**
 * DataService
 */
public class DataService
{

	private static DataService instance = null;

	private IDataServiceProvider provider;

	private DataService( )
	{
		Object adapter = ElementAdapterManager.getAdapter( this,
				IDataServiceProvider.class );

		if ( adapter instanceof IDataServiceProvider )
		{
			provider = (IDataServiceProvider) adapter;
		}
	}

	public synchronized static DataService getInstance( )
	{
		if ( instance == null )
		{
			instance = new DataService( );
		}

		return instance;
	}

	public boolean available( )
	{
		return provider != null;
	}

	public void createDataSet( )
	{
		if ( provider != null )
		{
			provider.createDataSet( );
		}
	}
}
