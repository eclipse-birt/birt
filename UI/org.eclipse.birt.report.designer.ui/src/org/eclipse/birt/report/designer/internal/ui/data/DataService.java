/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.data;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext.DataEngineFlowMode;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.timeFunction.IArgumentInfo;
import org.eclipse.birt.report.data.adapter.api.timeFunction.ITimeFunction;
import org.eclipse.birt.report.designer.internal.ui.data.function.layout.IArgumentLayout;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * DataService
 */
public class DataService {

	private static DataService instance = null;

	private IDataServiceProvider provider;

	private DataService() {
		Object adapter = ElementAdapterManager.getAdapter(this, IDataServiceProvider.class);

		if (adapter instanceof IDataServiceProvider) {
			provider = (IDataServiceProvider) adapter;
		}
	}

	public synchronized static DataService getInstance() {
		if (instance == null) {
			instance = new DataService();
		}

		return instance;
	}

	public boolean available() {
		return provider != null;
	}

	public void createDataSet() {
		if (provider != null) {
			provider.createDataSet();
		}
	}

	public void registerSession(DataSetHandle handle, DataRequestSession session) throws BirtException {
		if (provider != null) {
			provider.registerSession(handle, session);
		}
	}

	public void registerSession(CubeHandle handle, DataRequestSession session) throws BirtException {
		if (provider != null) {
			provider.registerSession(handle, session);
		}
	}

	public void registerSession(DataSourceHandle handle, DataRequestSession session) throws BirtException {
		if (provider != null) {
			provider.registerSession(handle, session);
		}
	}

	public void unRegisterSession(DataRequestSession session) throws BirtException {
		if (provider != null) {
			provider.unRegisterSession(session);
		}
	}

	public List getSelectValueList(Expression expression, DataSetHandle dataSetHandle, boolean useDataSetFilter)
			throws BirtException {
		if (provider != null) {
			return provider.getSelectValueList(expression, dataSetHandle, useDataSetFilter);
		}
		return Collections.EMPTY_LIST;
	}

	public List getSelectValueList(Expression expression, DataSetHandle dataSetHandle, DataEngineFlowMode flowMode)
			throws BirtException {
		if (provider != null) {
			return provider.getSelectValueList(expression, dataSetHandle, flowMode);
		}
		return Collections.EMPTY_LIST;
	}

	public List getSelectValueList(Expression expression, ModuleHandle moduleHandle, DataSetHandle dataSetHandle,
			boolean useDataSetFilter) throws BirtException {
		if (provider != null) {
			return provider.getSelectValueList(expression, moduleHandle, dataSetHandle, useDataSetFilter);
		}
		return Collections.EMPTY_LIST;
	}

	public List getSelectValueFromBinding(Expression expression, DataSetHandle dataSetHandle, Iterator binding,
			Iterator groupIterator, boolean useDataSetFilter) throws BirtException {
		if (provider != null) {
			return provider.getSelectValueFromBinding(expression, dataSetHandle, binding, groupIterator,
					useDataSetFilter);
		}
		return Collections.EMPTY_LIST;
	}

	public List getSelectValueFromBinding(Expression expression, ModuleHandle moduleHandle, DataSetHandle dataSetHandle,
			Iterator binding, Iterator groupIterator, boolean useDataSetFilter) throws BirtException {
		if (provider != null) {
			return provider.getSelectValueFromBinding(expression, moduleHandle, dataSetHandle, binding, groupIterator,
					useDataSetFilter);
		}
		return Collections.EMPTY_LIST;
	}

	public void updateColumnCache(DataSetHandle dataSetHandle, boolean holdEvent) throws BirtException {
		provider.updateColumnCache(dataSetHandle, holdEvent);
	}

	public List<IArgumentLayout> getArgumentLayout(ITimeFunction function, List<IArgumentInfo> infos) {
		return provider.getArgumentLayout(function, infos);
	}
}
