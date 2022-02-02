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
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.data.engine.api.script.IDataSetInstanceHandle;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetEventHandler;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetMetaDataDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * This class handles script data set events by executing the Javascript event
 * code. NOTE: functionality of this class will be moved to Engine. This class
 * is temporary
 */

public class ScriptDataSetJSEventHandler extends DataSetJSEventHandler implements IScriptDataSetEventHandler {
	public ScriptDataSetJSEventHandler(ScriptContext cx, IScriptDataSetDesign design) {
		super(cx, design);
	}

	protected IScriptDataSetDesign getScriptDataSetDesign() {
		return (IScriptDataSetDesign) getBaseDesign();
	}

	public void handleOpen(IDataSetInstanceHandle dataSet) throws BirtException {
		String script = getScriptDataSetDesign().getOpenScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSet.getScriptScope()).runScript("open", script);
		}
	}

	public void handleClose(IDataSetInstanceHandle dataSet) throws BirtException {
		String script = getScriptDataSetDesign().getCloseScript();
		if (script != null && script.length() > 0) {
			getRunner(dataSet.getScriptScope()).runScript("close", script);
		}
	}

	public boolean handleFetch(IDataSetInstanceHandle dataSet, IDataRow row) throws BirtException {
		String script = getScriptDataSetDesign().getFetchScript();
		if (script != null && script.length() > 0) {
			Object result = getRunner(dataSet.getScriptScope()).runScript("fetch", script);

			if (result instanceof Boolean)
				return ((Boolean) result).booleanValue();
			else
				throw new DataException(ResourceConstants.EXPECT_BOOLEAN_RETURN_TYPE, new Object[] { "Fetch", result });
		}
		return false;
	}

	public boolean handleDescribe(IDataSetInstanceHandle dataSet, IScriptDataSetMetaDataDefinition metaData)
			throws BirtException {
		String script = getScriptDataSetDesign().getDescribeScript();
		if (script != null && script.length() > 0) {
			Object result = getRunner(dataSet.getScriptScope()).runScript("describe", script);

			if (result instanceof Boolean)
				return ((Boolean) result).booleanValue();
			else
				throw new DataException(ResourceConstants.EXPECT_BOOLEAN_RETURN_TYPE,
						new Object[] { "Describe", result });
		}
		return false;
	}
}
