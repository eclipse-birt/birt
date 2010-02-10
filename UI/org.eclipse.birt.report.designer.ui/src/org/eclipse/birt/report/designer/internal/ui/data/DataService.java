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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;

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
	
	public List getSelectValueList( Expression expression,
			DataSetHandle dataSetHandle, boolean useDataSetFilter )
			throws BirtException
	{
		if ( provider != null )
		{
			return provider.getSelectValueList( expression,
					dataSetHandle,
					useDataSetFilter );
		}
		return Collections.EMPTY_LIST;
	}

	public List getSelectValueFromBinding( Expression expression,
			DataSetHandle dataSetHandle, Iterator binding,
			Iterator groupIterator, boolean useDataSetFilter )
			throws BirtException
	{
		if ( provider != null )
		{
			return provider.getSelectValueFromBinding( expression,
					dataSetHandle,
					binding,
					groupIterator,
					useDataSetFilter );
		}
		return Collections.EMPTY_LIST;
	}
}
