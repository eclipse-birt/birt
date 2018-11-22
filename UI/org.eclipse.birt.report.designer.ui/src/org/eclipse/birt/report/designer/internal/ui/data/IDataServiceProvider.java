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
import org.eclipse.birt.data.engine.api.DataEngineContext.DataEngineFlowMode;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.timeFunction.IArgumentInfo;
import org.eclipse.birt.report.data.adapter.api.timeFunction.ITimeFunction;
import org.eclipse.birt.report.designer.internal.ui.data.function.layout.IArgumentLayout;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

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
	
	List getSelectValueList( Expression expression,
			ModuleHandle moduleHandle, DataSetHandle dataSetHandle,
			boolean useDataSetFilter )
			throws BirtException;

	List getSelectValueList( Expression expression, DataSetHandle dataSetHandle,
			DataEngineFlowMode flowMode ) throws BirtException;

	List getSelectValueFromBinding( Expression expression,
			ModuleHandle moduleHandle, DataSetHandle dataSetHandle, 
			Iterator binding,
			Iterator groupIterator, boolean useDataSetFilter )
			throws BirtException;
	
	void updateColumnCache( DataSetHandle dataSetHandle, boolean holdEvent )
			throws BirtException;

	void registerSession( DataSetHandle handle, DataRequestSession session )
			throws BirtException;
	
	void registerSession( CubeHandle handle, DataRequestSession session)
			throws BirtException;
	
	void registerSession( DataSourceHandle handle, DataRequestSession session )
			throws BirtException;
	
	/**
	 * set free the resource registered in session.
	 * @param session
	 * @throws BirtException
	 */
	void unRegisterSession( DataRequestSession session ) throws BirtException;
	
	List<IArgumentLayout> getArgumentLayout(ITimeFunction function,List<IArgumentInfo> infos);

	
}
