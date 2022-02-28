/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.mozilla.javascript.Scriptable;

/**
 * Encapulates the runtime definition of a generic extended data source.
 */
public class OdaDataSourceRuntime extends DataSourceRuntime {
	private String extensionID;
	private Map publicProperties;

	private static Logger logger = Logger.getLogger(OdaDataSourceRuntime.class.getName());

	OdaDataSourceRuntime(IOdaDataSourceDesign dataSource, Scriptable sharedScope, ScriptContext cx) {
		super(dataSource, sharedScope, cx);

		Object[] params = { dataSource, sharedScope };
		logger.entering(OdaDataSourceRuntime.class.getName(), "OdaDataSourceRuntime", params);
		// Copy updatable properties
		publicProperties = new HashMap();
		publicProperties.putAll(dataSource.getPublicProperties());

		extensionID = dataSource.getExtensionID();
		logger.exiting(OdaDataSourceRuntime.class.getName(), "OdaDataSourceRuntime");
		logger.log(Level.FINER, "OdaDataSourceRuntime starts up");
	}

	/**
	 *
	 */
	public IOdaDataSourceDesign getSubdesign() {
		return (IOdaDataSourceDesign) getDesign();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle#
	 * getExtensionID()
	 */
	@Override
	public String getExtensionID() {
		return extensionID;
	}

	/**
	 * @return
	 */
	public Map getPublicProperties() {
		// Return runtime copy of public properties, which may have been updated
		return this.publicProperties;
	}

	/**
	 * @return
	 */
	public Map getPrivateProperties() {
		return getSubdesign().getPrivateProperties();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle#
	 * getAllExtensionProperties()
	 */
	@Override
	public Map getAllExtensionProperties() {
		return this.publicProperties;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle#
	 * getExtensionProperty(java.lang.String)
	 */
	@Override
	public String getExtensionProperty(String name) {
		return (String) publicProperties.get(name);
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle#
	 * setExtensionProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public void setExtensionProperty(String name, String value) {
		publicProperties.put(name, value);
	}

}
