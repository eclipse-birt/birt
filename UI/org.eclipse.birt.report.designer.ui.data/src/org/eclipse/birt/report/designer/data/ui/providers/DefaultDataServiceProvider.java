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

package org.eclipse.birt.report.designer.data.ui.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext.DataEngineFlowMode;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.timeFunction.BaseTimeFunction;
import org.eclipse.birt.report.data.adapter.api.timeFunction.IArgumentInfo;
import org.eclipse.birt.report.data.adapter.api.timeFunction.IBuildInBaseTimeFunction;
import org.eclipse.birt.report.data.adapter.api.timeFunction.ITimeFunction;
import org.eclipse.birt.report.designer.data.ui.actions.NewDataSetAction;
import org.eclipse.birt.report.designer.data.ui.dataset.AppContextPopulator;
import org.eclipse.birt.report.designer.data.ui.dataset.AppContextResourceReleaser;
import org.eclipse.birt.report.designer.data.ui.dataset.ExternalUIUtil;
import org.eclipse.birt.report.designer.data.ui.function.layout.ArgumentLayout;
import org.eclipse.birt.report.designer.internal.ui.data.IDataServiceProvider;
import org.eclipse.birt.report.designer.internal.ui.data.function.layout.IArgumentLayout;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * DefaultDataServiceProvider
 */
public class DefaultDataServiceProvider implements IDataServiceProvider {
	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.data.IDataServiceProvider
	 * #createDataSet()
	 */
	@Override
	public void createDataSet() {
		new NewDataSetAction().run();
	}

	@Override
	public List getSelectValueList(Expression expression, DataSetHandle dataSetHandle, boolean useDataSetFilter)
			throws BirtException {
		return DistinctValueSelector.getSelectValueList(expression, dataSetHandle, useDataSetFilter);
	}

	@Override
	public List getSelectValueList(Expression expression, DataSetHandle dataSetHandle, DataEngineFlowMode flowMode)
			throws BirtException {
		return DistinctValueSelector.getSelectValueList(expression, dataSetHandle, flowMode);
	}

	@Override
	public List getSelectValueFromBinding(Expression expression, DataSetHandle dataSetHandle, Iterator binding,
			Iterator groupIterator, boolean useDataSetFilter) throws BirtException {
		return DistinctValueSelector.getSelectValueFromBinding(expression, dataSetHandle, binding, groupIterator,
				useDataSetFilter);
	}

	@Override
	public List getSelectValueFromBinding(Expression expression, ModuleHandle moduleHandle, DataSetHandle dataSetHandle,
			Iterator binding, Iterator groupIterator, boolean useDataSetFilter) throws BirtException {
		return DistinctValueSelector.getSelectValueFromBinding(expression, moduleHandle, dataSetHandle, binding,
				groupIterator, useDataSetFilter);
	}

	@Override
	public List getSelectValueList(Expression expression, ModuleHandle moduleHandle, DataSetHandle dataSetHandle,
			boolean useDataSetFilter) throws BirtException {
		return DistinctValueSelector.getSelectValueList(expression, moduleHandle, dataSetHandle, useDataSetFilter);
	}

	@Override
	public void registerSession(DataSetHandle handle, DataRequestSession session) throws BirtException {
		AppContextPopulator.populateApplicationContext(handle, session);
	}

	@Override
	public void registerSession(CubeHandle handle, DataRequestSession session) throws BirtException {
		if (session.getDataSessionContext().getAppContext() == null) {
			session.getDataSessionContext().setAppContext(new HashMap());
		}
		AppContextPopulator.populateApplicationContext(handle, session.getDataSessionContext().getAppContext());
	}

	@Override
	public void registerSession(DataSourceHandle handle, DataRequestSession session) throws BirtException {
		if (session.getDataSessionContext().getAppContext() == null) {
			session.getDataSessionContext().setAppContext(new HashMap());
		}
		AppContextPopulator.populateApplicationContext(handle, session.getDataSessionContext().getAppContext());
	}

	@Override
	public void unRegisterSession(DataRequestSession session) throws BirtException {
		if (session != null) {
			AppContextResourceReleaser.release(session.getDataSessionContext().getAppContext());
		}
	}

	@Override
	public void updateColumnCache(DataSetHandle dataSetHandle, boolean holdEvent) throws BirtException {
		ExternalUIUtil.updateColumnCache(dataSetHandle, holdEvent);
	}

	@Override
	public List<IArgumentLayout> getArgumentLayout(ITimeFunction function, List<IArgumentInfo> infos) {
		IArgumentLayout layout1;
		List<IArgumentLayout> layoutarguments = new ArrayList<>();

		layout1 = new ArgumentLayout(IArgumentInfo.PERIOD_1, ArgumentLayout.ALIGN_INLINE_NONE);
		function = (BaseTimeFunction) function;
		if (function.getName().equals(IBuildInBaseTimeFunction.PREVIOUS_MONTH)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.PREVIOUS_QUARTER)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.PREVIOUS_YEAR)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.WEEK_TO_DATE_LAST_YEAR)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.PREVIOUS_WEEK_TO_DATE)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));

			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD2, ArgumentLayout.ALIGN_INLINE_BEFORE));
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.PERIOD_2, ArgumentLayout.LIGN_INLINEL_AFTER));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));

			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD2, ArgumentLayout.ALIGN_INLINE_BEFORE));
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.PERIOD_2, ArgumentLayout.LIGN_INLINEL_AFTER));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.TRAILING_N_MONTHS)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.TRAILING_N_DAYS)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1, ArgumentLayout.ALIGN_INLINE_BEFORE));
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.PERIOD_1, ArgumentLayout.ALIGN_INLINE_NONE));

			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD2, ArgumentLayout.ALIGN_INLINE_BEFORE));
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.PERIOD_2, ArgumentLayout.LIGN_INLINEL_AFTER));
		} else if (function.getName().equals(IBuildInBaseTimeFunction.NEXT_N_PERIODS)) {
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.N_PERIOD1, ArgumentLayout.ALIGN_INLINE_BEFORE));
			layoutarguments.add(new ArgumentLayout(IArgumentInfo.PERIOD_1, ArgumentLayout.ALIGN_INLINE_NONE));
		}
		return layoutarguments;
	}
}
