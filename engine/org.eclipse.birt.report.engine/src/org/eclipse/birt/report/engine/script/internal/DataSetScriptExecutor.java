/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
package org.eclipse.birt.report.engine.script.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.eventhandler.IDataSetEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.IScriptedDataSetEventHandler;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.script.internal.instance.DataSetInstance;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.elements.interfaces.ISimpleDataSetModel;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class DataSetScriptExecutor extends DtEScriptExecutor implements IBaseDataSetEventHandler {

	private static final String ON_FETCH = "onFetch";

	protected DataSetHandle dataSetHandle;

	protected IDataSetEventHandler eventHandler;

	private boolean useOnFetchEventHandler = false;
	private boolean useAfterCloseEventHandler = false;
	private boolean useAfterOpenEventHandler = false;
	private boolean useBeforeOpenEventHandler = false;
	private boolean useBeforeCloseEventHandler = false;

	private boolean flag = false;
	private final String beforeOpenMethodID, beforeCloseMethodID, afterOpenMethodID, afterCloseMethodID,
			onFetchMethodID, className;

	private Map<IDataSetInstanceHandle, Scriptable> scopeCache = new HashMap<IDataSetInstanceHandle, Scriptable>();

	public DataSetScriptExecutor(DataSetHandle dataSetHandle, ExecutionContext context) throws BirtException {
		super(context);
		this.dataSetHandle = dataSetHandle;
		className = dataSetHandle.getEventHandlerClass();
		useOnFetchEventHandler = ScriptTextUtil.isNullOrComments(dataSetHandle.getOnFetch());
		useAfterCloseEventHandler = ScriptTextUtil.isNullOrComments(dataSetHandle.getAfterClose());
		useAfterOpenEventHandler = ScriptTextUtil.isNullOrComments(dataSetHandle.getAfterOpen());
		useBeforeOpenEventHandler = ScriptTextUtil.isNullOrComments(dataSetHandle.getBeforeOpen());
		useBeforeCloseEventHandler = ScriptTextUtil.isNullOrComments(dataSetHandle.getBeforeClose());

		beforeOpenMethodID = ModuleUtil
				.getScriptUID(dataSetHandle.getPropertyHandle(ISimpleDataSetModel.BEFORE_OPEN_METHOD));
		beforeCloseMethodID = ModuleUtil
				.getScriptUID(dataSetHandle.getPropertyHandle(ISimpleDataSetModel.BEFORE_CLOSE_METHOD));
		afterOpenMethodID = ModuleUtil
				.getScriptUID(dataSetHandle.getPropertyHandle(ISimpleDataSetModel.AFTER_OPEN_METHOD));
		afterCloseMethodID = ModuleUtil
				.getScriptUID(dataSetHandle.getPropertyHandle(ISimpleDataSetModel.AFTER_CLOSE_METHOD));
		onFetchMethodID = ModuleUtil.getScriptUID(dataSetHandle.getPropertyHandle(ISimpleDataSetModel.ON_FETCH_METHOD));
	}

	protected void initEventHandler() {
		if (className != null && !flag) {
			try {
				eventHandler = (IDataSetEventHandler) getInstance(className, context);
				flag = true;
			} catch (ClassCastException e) {
				addClassCastException(context, e, dataSetHandle, IScriptedDataSetEventHandler.class);
			} catch (EngineException e) {
				addException(context, e, dataSetHandle);
			}
		}
	}

	public void handleBeforeOpen(IDataSetInstanceHandle dataSet) {
		initEventHandler();
		if (reportContext == null)
			return;
		try {
			if (!this.useBeforeOpenEventHandler) {
				Scriptable scope = getScriptScope(dataSet);
				ScriptStatus status = handleJS(scope, dataSet.getName(), BEFORE_OPEN, dataSetHandle.getBeforeOpen(),
						beforeOpenMethodID);
				if (status.didRun())
					return;
			}
			if (eventHandler != null)
				eventHandler.beforeOpen(new DataSetInstance(dataSet), reportContext);
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public void handleBeforeClose(IDataSetInstanceHandle dataSet) {
		initEventHandler();
		if (reportContext == null)
			return;
		try {
			if (!this.useBeforeCloseEventHandler) {
				Scriptable scope = getScriptScope(dataSet);
				ScriptStatus status = handleJS(scope, dataSet.getName(), BEFORE_CLOSE, dataSetHandle.getBeforeClose(),
						beforeCloseMethodID);
				if (status.didRun())
					return;
			}
			if (eventHandler != null)
				eventHandler.beforeClose(new DataSetInstance(dataSet), reportContext);
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public void handleAfterOpen(IDataSetInstanceHandle dataSet) {
		initEventHandler();
		if (reportContext == null)
			return;
		try {
			if (!this.useAfterOpenEventHandler) {
				Scriptable scope = getScriptScope(dataSet);
				ScriptStatus status = handleJS(scope, dataSet.getName(), AFTER_OPEN, dataSetHandle.getAfterOpen(),
						afterOpenMethodID);
				if (status.didRun())
					return;
			}
			if (eventHandler != null)
				eventHandler.afterOpen(new DataSetInstance(dataSet), reportContext);
		} catch (Exception e) {
			addException(context, e);
		}
	}

	/**
	 * 
	 * @param dataSet
	 * @return
	 * @throws DataException
	 */
	private Scriptable getScriptScope(IDataSetInstanceHandle dataSet) throws DataException {
		Scriptable scope = scopeCache.get(dataSet);
		if (scope != null) {
			return scope;
		}
		synchronized (scopeCache) {
			Scriptable shared = this.scope;
			scope = (Scriptable) Context.javaToJS(new DataSetInstance(dataSet), shared);
			scope.setParentScope(shared);
			scope.setPrototype(dataSet.getScriptScope());
			scopeCache.put(dataSet, scope);
		}
		return scope;
	}

	public void handleAfterClose(IDataSetInstanceHandle dataSet) {
		initEventHandler();
		if (reportContext == null)
			return;
		try {
			if (!this.useAfterCloseEventHandler) {
				Scriptable scope = getScriptScope(dataSet);
				ScriptStatus status = handleJS(scope, dataSet.getName(), AFTER_CLOSE, dataSetHandle.getAfterClose(),
						afterCloseMethodID);
				if (status.didRun())
					return;
			}
			if (eventHandler != null)
				eventHandler.afterClose(reportContext);
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public void handleOnFetch(IDataSetInstanceHandle dataSet, IDataRow row) {
		initEventHandler();
		if (reportContext == null)
			return;
		try {
			if (!this.useOnFetchEventHandler) {
				Scriptable scope = getScriptScope(dataSet);
				ScriptStatus status = handleJS(scope, dataSet.getName(), ON_FETCH, dataSetHandle.getOnFetch(),
						onFetchMethodID);
				if (status.didRun())
					return;
			}
			if (eventHandler != null)
				eventHandler.onFetch(new DataSetInstance(dataSet), new DataSetRow(row), reportContext);
		} catch (Exception e) {
			addException(context, e);
		}
	}

	protected ScriptStatus handleJS(Scriptable scope, String name, String method, String script, String id) {
		return handleJS(scope, DATA_SET, name, method, script, id);
	}

}
