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

import java.util.Map;

import org.eclipse.birt.data.engine.impl.DataSourceRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * Implements BIRT Javascript's Data Source object. This native Java class is
 * made available to the Rhino engine via it's NativeJavaObject wrapper
 */

public class JSDataSourceImpl {
	private DataSourceRuntime dataSource;

	public JSDataSourceImpl(DataSourceRuntime dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Implements DataSource.name
	 */
	public String getName() {
		return dataSource.getName();
	}

	public void setName(String name) {
		// Name is not updatabe by script; ignore
	}

	/**
	 * Implements DataSource.extensionID
	 */
	public String getExtensionID() {
		return dataSource.getExtensionID();
	}

	public void setExtensionID(String s) {
		// ExtensionID is not updatable; ignore
	}

	/**
	 * Implements DataSource.getExtensionProperty(name)
	 */
	public String getExtensionProperty(String name) {
		return dataSource.getExtensionProperty(name);
	}

	/**
	 * Implements DataSource.setExtensionProperty(name, value)
	 */
	public void setExtensionProperty(String name, String value) {
		dataSource.setExtensionProperty(name, value);
	}

	/**
	 * Implements DataSource.extensionProperties
	 */
	public Scriptable getExtensionProperties() {
		Map props = dataSource.getAllExtensionProperties();

		if (props != null) {
			// Data Source's publicproperties is a String->Collection map
			return new JSStringMap(props);
		} else {
			return null;
		}

	}

	public Map getAllExtensionProperties() {
		return dataSource.getAllExtensionProperties();
	}

	/**
	 * Implements DataSource.isOpen
	 */
	public Boolean getIsOpen() {
		return dataSource.isOpen();
	}
}
