/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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
package org.eclipse.birt.report.data.adapter.internal.adapter;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.mozilla.javascript.Context;

public class ScriptDataSourceAdapter extends ScriptDataSourceDesign {
	public static final String CASSANDRA_DATA_SOURCE_VALUE = "me.prettyprint.hector";//$NON-NLS-1$
	public static final String SCRIPT_TYPE = "script_type"; //$NON-NLS-1$
	public static final String CASSANDRA_DATA_SOURCE_SCRIPT = "Cassandra"; //$NON-NLS-1$

	/**
	 * Creates adaptor based on Model DataSourceHandle.
	 * 
	 * @param source model handle
	 */
	public ScriptDataSourceAdapter(ScriptDataSourceHandle source, DataSessionContext context) throws BirtException {
		super(source.getQualifiedName());

		// TODO: event handler!!!!

		// Adapt base class properties
		DataAdapterUtil.adaptBaseDataSource(source, this);
		if (source.getProperty(SCRIPT_TYPE) != null
				&& source.getProperty(SCRIPT_TYPE).equals(CASSANDRA_DATA_SOURCE_VALUE)) {
			validateScriptDataSource(source, context);
		}
		// Adapt script data source elements
		setOpenScript(source.getOpen());
		setCloseScript(source.getClose());
	}

	private void validateScriptDataSource(ScriptDataSourceHandle source, DataSessionContext context)
			throws AdapterException {
		ScriptContext scriptContext = null;
		try {
			scriptContext = context.getDataEngineContext().getScriptContext();
			IDataScriptEngine scriptEngine = (IDataScriptEngine) scriptContext
					.getScriptEngine(IDataScriptEngine.ENGINE_NAME);
			Context cx = scriptEngine.getJSContext(scriptContext);
			cx.getApplicationClassLoader().loadClass("me.prettyprint.hector.api.factory.HFactory");
		} catch (BirtException e1) {
			try {
				retryCustomClassLoader(context);
			} catch (ClassNotFoundException e) {
				throw new AdapterException(ResourceConstants.DATASOURCE_CASSANDRA_ERROR, e);
			} catch (BirtException e) {
				throw new AdapterException(ResourceConstants.DATASOURCE_CASSANDRA_ERROR, e);
			}
		} catch (ClassNotFoundException e) {
			try {
				retryCustomClassLoader(context);
			} catch (ClassNotFoundException ex) {
				throw new AdapterException(ResourceConstants.DATASOURCE_CASSANDRA_ERROR, e);
			} catch (BirtException ex) {
				throw new AdapterException(ResourceConstants.DATASOURCE_CASSANDRA_ERROR, e);
			}
		}
	}

	private void retryCustomClassLoader(DataSessionContext context) throws ClassNotFoundException, BirtException {
		ClassLoader customClassLoader = context.getDataEngineContext().getClassLoader();
		customClassLoader.loadClass("me.prettyprint.hector.api.factory.HFactory");
	}
}
