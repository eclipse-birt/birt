/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl;

import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.script.IScriptDataSourceEventHandler;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.mozilla.javascript.Scriptable;

/**
 * Encapulates the runtime definition of a scripted data source.
 */
public class ScriptDataSourceRuntime extends DataSourceRuntime {
	private IScriptDataSourceEventHandler scriptEventHandler;

	/**
	 * @param dataSource
	 * @param sharedScope
	 */
	ScriptDataSourceRuntime(IScriptDataSourceDesign dataSource, Scriptable sharedScope, ScriptContext cx) {
		super(dataSource, sharedScope, cx);
		Object[] params = { dataSource, sharedScope };
		logger.entering(ScriptDataSourceRuntime.class.getName(), "ScriptDataSourceRuntime", params);
		if (getEventHandler() instanceof IScriptDataSourceEventHandler) {
			scriptEventHandler = (IScriptDataSourceEventHandler) getEventHandler();
		}

		logger.exiting(ScriptDataSourceRuntime.class.getName(), "ScriptDataSourceRuntime");
		logger.log(Level.FINER, "ScriptDataSourceRuntime starts up");
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.DataSourceRuntime#openOdiDataSource(org.
	 * eclipse.birt.data.engine.odi.IDataSource)
	 */
	@Override
	public void openOdiDataSource(IDataSource odiDataSource) throws DataException {
		// This is when we should run the Open script associated with the script
		// data source
		open();
		super.openOdiDataSource(odiDataSource);
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.DataSourceRuntime#closeOdiDataSource()
	 */
	@Override
	public void closeOdiDataSource() throws DataException {
		// This is when we should run the Open script associated with the script
		// data source
		close();
		super.closeOdiDataSource();
	}

	/** Executes the open script; returns its result */
	private void open() throws DataException {
		if (scriptEventHandler != null) {
			try {
				scriptEventHandler.handleOpen(this);
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
		}
	}

	/** Executes the close script; returns its result */
	private void close() throws DataException {
		if (scriptEventHandler != null) {
			try {
				scriptEventHandler.handleClose(this);
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
		}
	}

	/*
	 * This is not ODA data source; it has no extension ID Return a fixed string
	 *
	 * @see org.eclipse.birt.data.engine.impl.DataSourceRuntime#getExtensionID()
	 */
	@Override
	public String getExtensionID() {
		return "SCRIPT";
	}

	/*
	 * Script data source has no extension property
	 *
	 * @see org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle#
	 * getAllExtensionProperties()
	 */
	@Override
	public Map getAllExtensionProperties() {
		return null;
	}

	/*
	 * Script data source has no extension property
	 *
	 * @see org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle#
	 * getExtensionProperty(java.lang.String)
	 */
	@Override
	public String getExtensionProperty(String name) {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle#
	 * setExtensionProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public void setExtensionProperty(String name, String value) {
		// Script data source has no extension property
	}

}
