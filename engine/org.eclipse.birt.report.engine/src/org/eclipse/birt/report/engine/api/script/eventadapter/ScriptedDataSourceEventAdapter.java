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

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.eventhandler.IScriptedDataSourceEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IDataSourceInstance;

/**
 * Default (empty) implementation of the IScriptedDataSourceEventHandler
 * interface
 */
public class ScriptedDataSourceEventAdapter extends DataSourceEventAdapter implements IScriptedDataSourceEventHandler {

	public void open(IDataSourceInstance dataSource) throws ScriptException {

	}

	public void close(IDataSourceInstance dataSource) throws ScriptException {

	}

}
