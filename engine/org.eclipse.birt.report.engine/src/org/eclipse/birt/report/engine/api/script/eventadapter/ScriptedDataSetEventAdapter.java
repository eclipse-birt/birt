/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
