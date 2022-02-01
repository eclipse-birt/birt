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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetEventHandler;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetMetaDataDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.report.engine.api.script.eventhandler.IScriptedDataSetEventHandler;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.script.internal.instance.DataSetInstance;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.elements.interfaces.IScriptDataSetModel;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class ScriptDataSetScriptExecutor extends DataSetScriptExecutor implements IScriptDataSetEventHandler {

	private static final String OPEN = "OPEN";

	private static final String CLOSE = "CLOSE";

	private static final String FETCH = "FETCH";

	private static final String DESCRIBE = "DESCRIBE";

	private IScriptedDataSetEventHandler scriptedEventHandler;

	private boolean useOpenEventHandler = false;
	private boolean useFetchEventHandler = false;
	private boolean useCloseEventHandler = false;
	private boolean useDescribeEventHandler = false;

	private String fetchScript = null;
	private HashMap<IDataSetInstanceHandle, Scriptable> sharedScopes = new HashMap<IDataSetInstanceHandle, Scriptable>();

	private final String openMethodID, closeMethodID, fetchMethodID, describeMethodID;

	public ScriptDataSetScriptExecutor(ScriptDataSetHandle dataSetHandle, ExecutionContext context)
			throws BirtException {
		super(dataSetHandle, context);
		// Fetch script will be acquire multiple times. Cache it locally.
		// for other script, as they will only be used only, it is not necessary to keep
		// the local cache.
		this.fetchScript = dataSetHandle.getFetch();
		useOpenEventHandler = ScriptTextUtil.isNullOrComments(dataSetHandle.getOpen());
		useFetchEventHandler = ScriptTextUtil.isNullOrComments(dataSetHandle.getFetch());
		useCloseEventHandler = ScriptTextUtil.isNullOrComments(dataSetHandle.getClose());
		useDescribeEventHandler = ScriptTextUtil.isNullOrComments(dataSetHandle.getDescribe());

		openMethodID = ModuleUtil.getScriptUID(dataSetHandle.getPropertyHandle(IScriptDataSetModel.OPEN_METHOD));
		closeMethodID = ModuleUtil.getScriptUID(dataSetHandle.getPropertyHandle(IScriptDataSetModel.CLOSE_METHOD));
		fetchMethodID = ModuleUtil.getScriptUID(dataSetHandle.getPropertyHandle(IScriptDataSetModel.FETCH_METHOD));
		describeMethodID = ModuleUtil
				.getScriptUID(dataSetHandle.getPropertyHandle(IScriptDataSetModel.DESCRIBE_METHOD));

	}

	protected void initEventHandler() {
		super.initEventHandler();
		if (eventHandler != null) {
			try {
				scriptedEventHandler = (IScriptedDataSetEventHandler) eventHandler;
			} catch (ClassCastException e) {
				addClassCastException(context, e, dataSetHandle, IScriptedDataSetEventHandler.class);
			}
		}
	}

	public void handleOpen(IDataSetInstanceHandle dataSet) throws BirtException {
		initEventHandler();
		try {
			if (!this.useOpenEventHandler) {
				ScriptStatus status = handleJS(getScriptScope(dataSet), dataSet.getName(), OPEN,
						((ScriptDataSetHandle) dataSetHandle).getOpen(), openMethodID);
				if (status.didRun())
					return;
			}
			if (scriptedEventHandler != null)
				scriptedEventHandler.open(new DataSetInstance(dataSet));
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public void handleClose(IDataSetInstanceHandle dataSet) {
		initEventHandler();
		try {
			if (!this.useCloseEventHandler) {
				ScriptStatus status = handleJS(getScriptScope(dataSet), dataSet.getName(), CLOSE,
						((ScriptDataSetHandle) dataSetHandle).getClose(), closeMethodID);
				if (status.didRun())
					return;
			}
			if (scriptedEventHandler != null)
				scriptedEventHandler.close(new DataSetInstance(dataSet));
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public boolean handleFetch(IDataSetInstanceHandle dataSet, IDataRow row) {
		initEventHandler();
		try {
			if (!useFetchEventHandler) {
				ScriptStatus status = handleJS(getScriptScope(dataSet), dataSet.getName(), FETCH, this.fetchScript,
						fetchMethodID);
				if (status.didRun()) {
					Object result = status.result();
					if (result instanceof Boolean)
						return ((Boolean) result).booleanValue();
					else
						throw new DataException(ResourceConstants.EXPECT_BOOLEAN_RETURN_TYPE,
								new Object[] { "Fetch", result });
				}
			}
			if (scriptedEventHandler != null)
				return scriptedEventHandler.fetch(new DataSetInstance(dataSet), new UpdatableDataSetRow(row));
		} catch (Exception e) {
			addException(context, e);
		}
		return false;
	}

	public boolean handleDescribe(IDataSetInstanceHandle dataSet, IScriptDataSetMetaDataDefinition metaData)
			throws BirtException {
		initEventHandler();
		try {
			if (!this.useDescribeEventHandler) {
				ScriptStatus status = handleJS(getScriptScope(dataSet), dataSet.getName(), DESCRIBE,
						((ScriptDataSetHandle) dataSetHandle).getDescribe(), describeMethodID);
				if (status.didRun()) {
					Object result = status.result();
					if (result instanceof Boolean)
						return ((Boolean) result).booleanValue();
					else
						throw new DataException(ResourceConstants.EXPECT_BOOLEAN_RETURN_TYPE,
								new Object[] { "Describe", result });
				}
			}
			if (scriptedEventHandler != null)
				return scriptedEventHandler.describe(new DataSetInstance(dataSet),
						new ScriptedDataSetMetaData(metaData));
		} catch (Exception e) {
			addException(context, e);
		}
		return false;
	}

	private Scriptable getScriptScope(IDataSetInstanceHandle dataSet) throws DataException {
		Scriptable result = this.sharedScopes.get(dataSet);
		if (result != null)
			return result;

		result = (Scriptable) Context.javaToJS(new DataSetInstance(dataSet), this.scope);
		result.setParentScope(this.scope);
		result.setPrototype(dataSet.getScriptScope());
		this.sharedScopes.put(dataSet, result);
		return result;
	}

}
