
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.report.designer.data.ui.util;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.RunAndRenderTask;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

/**
 * 
 */

public class DummyEngineTask extends RunAndRenderTask {
	private DataSetHandle dataSetHandle;

	public DummyEngineTask(ReportEngine engine, IReportRunnable runnable, ModuleHandle moduleHandle) {
		this(engine, runnable, moduleHandle, null);
	}

	public DummyEngineTask(ReportEngine engine, IReportRunnable runnable, ModuleHandle moduleHandle,
			DataSetHandle handle) {
		super(engine, runnable);
		setEngineTaskParameters(this, moduleHandle);
		this.taskType = IEngineTask.TASK_UNKNOWN;
		this.dataSetHandle = handle;
	}

	public void run() throws EngineException {
		usingParameterValues();
		loadDesign();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.impl.EngineTask#getDataSession()
	 */
	public DataRequestSession getDataSession() throws EngineException {
		DataRequestSession session = super.getDataSession();
		if (dataSetHandle != null) {
			try {
				ModelDteApiAdapter apiAdapter = new ModelDteApiAdapter(executionContext);
				apiAdapter.defineDataSet(dataSetHandle, session);
			} catch (BirtException ex) {
				throw new EngineException(ex);
			}
		}
		return session;
	}

	public ExecutionContext getExecutionContext() {
		return this.executionContext;
	}

	/**
	 * Fetch the report parameter name/value pairs from the rptconfig file. And also
	 * set all the parameters whose value is not null to the Engine task.
	 * 
	 * @param engineTask
	 */
	private void setEngineTaskParameters(DummyEngineTask engineTask, ModuleHandle moduleHandle) {
		List paramsList = moduleHandle.getAllParameters();
		for (int i = 0; i < paramsList.size(); i++) {
			Object parameterObject = paramsList.get(i);
			if (parameterObject instanceof ScalarParameterHandle) {
				ScalarParameterHandle parameterHandle = (ScalarParameterHandle) parameterObject;
				Object value = DataAdapterUtil.getParamValueFromConfigFile(parameterHandle);
				if (value != null) {
					engineTask.setParameter(parameterHandle.getName(), value, parameterHandle.getDisplayName());
				}
			}
		}
	}
}
