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
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle;
import org.eclipse.birt.data.engine.api.script.IScriptDataSourceEventHandler;

/**
 * This class handles script data source events by executing the Javascript
 * event code. NOTE: functionality of this class will be moved to Engine. This
 * class is temporary
 */
public class ScriptDataSourceJSEventHandler extends DataSourceJSEventHandler implements IScriptDataSourceEventHandler {

	public ScriptDataSourceJSEventHandler(ScriptContext cx, IScriptDataSourceDesign design) {
		super(cx, design);
	}

	protected IScriptDataSourceDesign getScriptDataSourceDesign() {
		return (IScriptDataSourceDesign) getBaseDesign();
	}

	@Override
	public void handleOpen(IDataSourceInstanceHandle dataSource) throws BirtException {
		String script = getScriptDataSourceDesign().getOpenScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSource.getScriptScope()).runScript("open", script);
		}
	}

	@Override
	public void handleClose(IDataSourceInstanceHandle dataSource) throws BirtException {
		String script = getScriptDataSourceDesign().getCloseScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSource.getScriptScope()).runScript("close", script);
		}
	}

}
