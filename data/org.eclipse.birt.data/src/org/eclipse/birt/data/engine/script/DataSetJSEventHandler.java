/*
 *************************************************************************
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.script;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.mozilla.javascript.Scriptable;

/**
 * This class handles data set events by executing the Javascript event code.
 * NOTE: functionality of this class will be moved to Engine. This class is
 * temporary
 */
public class DataSetJSEventHandler implements IBaseDataSetEventHandler {
	protected IBaseDataSetDesign design;
	protected JSMethodRunner runner;
	private ScriptContext cx;

	public DataSetJSEventHandler(ScriptContext cx, IBaseDataSetDesign dataSetDesign) {
		this.design = dataSetDesign;
		this.cx = cx;
	}

	protected IBaseDataSetDesign getBaseDesign() {
		return design;
	}

	protected JSMethodRunner getRunner(Scriptable scope) {
		if (runner == null) {
			String scopeName = "DataSet[" + design.getName() + "]";
			runner = new JSMethodRunner(cx, scope, scopeName);
		}
		return runner;
	}

	public void handleBeforeOpen(IDataSetInstanceHandle dataSet) throws BirtException {
		String script = getBaseDesign().getBeforeOpenScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSet.getScriptScope()).runScript("beforeOpen", script);
		}
	}

	public void handleBeforeClose(IDataSetInstanceHandle dataSet) throws BirtException {
		String script = getBaseDesign().getBeforeCloseScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSet.getScriptScope()).runScript("beforeClose", script);
		}
	}

	public void handleAfterOpen(IDataSetInstanceHandle dataSet) throws BirtException {
		String script = getBaseDesign().getAfterOpenScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSet.getScriptScope()).runScript("afterOpen", script);
		}
	}

	public void handleAfterClose(IDataSetInstanceHandle dataSet) throws BirtException {
		String script = getBaseDesign().getAfterCloseScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSet.getScriptScope()).runScript("afterClose", script);
		}
	}

	public void handleOnFetch(IDataSetInstanceHandle dataSet, IDataRow row) throws BirtException {
		String script = getBaseDesign().getOnFetchScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSet.getScriptScope()).runScript("onFetch", script);
		}
	}
}
