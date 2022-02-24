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
package org.eclipse.birt.report.engine.api.script.eventadapter;

import org.eclipse.birt.report.engine.api.script.IScriptedDataSetMetaData;
import org.eclipse.birt.report.engine.api.script.IUpdatableDataSetRow;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.eventhandler.IScriptedDataSetEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;

/**
 * Default (empty) implementation of the IScriptedDataSetEventHandler interface
 */
public class ScriptedDataSetEventAdapter extends DataSetEventAdapter implements IScriptedDataSetEventHandler {

	public void open(IDataSetInstance dataSet) throws ScriptException {

	}

	public boolean fetch(IDataSetInstance dataSet, IUpdatableDataSetRow row) throws ScriptException {
		return false;
	}

	public void close(IDataSetInstance dataSet) throws ScriptException {

	}

	public boolean describe(IDataSetInstance dataSet, IScriptedDataSetMetaData metaData) throws ScriptException {
		return false;
	}

}
