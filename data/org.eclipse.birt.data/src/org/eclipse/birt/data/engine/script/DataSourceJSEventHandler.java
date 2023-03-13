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
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.script.IBaseDataSourceEventHandler;
import org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle;
import org.mozilla.javascript.Scriptable;

public class DataSourceJSEventHandler implements IBaseDataSourceEventHandler {
	private IBaseDataSourceDesign design;
	private JSMethodRunner runner;
	private ScriptContext cx;

	public DataSourceJSEventHandler(ScriptContext cx, IBaseDataSourceDesign dataSourceDesign) {
		assert dataSourceDesign != null;
		this.design = dataSourceDesign;
		this.cx = cx;
	}

	protected IBaseDataSourceDesign getBaseDesign() {
		return design;
	}

	protected JSMethodRunner getRunner(Scriptable scope) {
		if (runner == null) {
			String scopeName = "DataSource[" + design.getName() + "]";
			runner = new JSMethodRunner(cx, scope, scopeName);
		}
		return runner;
	}

	@Override
	public void handleBeforeOpen(IDataSourceInstanceHandle dataSource) throws BirtException {
		String script = getBaseDesign().getBeforeOpenScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSource.getScriptScope()).runScript("beforeOpen", script);
		}
	}

	@Override
	public void handleBeforeClose(IDataSourceInstanceHandle dataSource) throws BirtException {
		String script = getBaseDesign().getBeforeCloseScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSource.getScriptScope()).runScript("beforeClose", script);
		}
	}

	@Override
	public void handleAfterOpen(IDataSourceInstanceHandle dataSource) throws BirtException {
		String script = getBaseDesign().getAfterOpenScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSource.getScriptScope()).runScript("afterOpen", script);
		}
	}

	@Override
	public void handleAfterClose(IDataSourceInstanceHandle dataSource) throws BirtException {
		String script = getBaseDesign().getAfterCloseScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSource.getScriptScope()).runScript("afterClose", script);
		}
	}

}
