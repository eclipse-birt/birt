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

package org.eclipse.birt.report.designer.data.ui.providers;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.data.ui.actions.NewDataSetAction;
import org.eclipse.birt.report.designer.internal.ui.data.IDataServiceProvider;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;

/**
 * DefaultDataServiceProvider
 */
public class DefaultDataServiceProvider implements IDataServiceProvider
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.data.IDataServiceProvider
	 * #createDataSet()
	 */
	public void createDataSet( )
	{
		new NewDataSetAction( ).run( );
	}

	public List getSelectValueList( Expression expression,
			DataSetHandle dataSetHandle, boolean useDataSetFilter )
			throws BirtException
	{
		return DistinctValueSelector.getSelectValueList( expression,
				dataSetHandle,
				useDataSetFilter );
	}

	public List getSelectValueFromBinding( Expression expression,
			DataSetHandle dataSetHandle, Iterator binding,
			Iterator groupIterator, boolean useDataSetFilter )
			throws BirtException
	{
		return DistinctValueSelector.getSelectValueFromBinding( expression,
				dataSetHandle,
				binding,
				groupIterator,
				useDataSetFilter );
	}

}
