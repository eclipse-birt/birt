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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;

/**
 * IDataServiceProvider
 */
public interface IDataServiceProvider
{

	/**
	 * Creats a new data set in default context.
	 */
	void createDataSet( );

	List getSelectValueList( Expression expression,
			DataSetHandle dataSetHandle, boolean useDataSetFilter )
			throws BirtException;

	List getSelectValueFromBinding( Expression expression,
			DataSetHandle dataSetHandle, Iterator binding,
			Iterator groupIterator, boolean useDataSetFilter )
			throws BirtException;
}
