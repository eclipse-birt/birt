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
