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
package org.eclipse.birt.report.engine.script.internal.instance;

import java.util.Map;

import org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle;
import org.eclipse.birt.report.engine.api.script.instance.IDataSourceInstance;

public class DataSourceInstance implements IDataSourceInstance {

	private IDataSourceInstanceHandle dataSource;

	public DataSourceInstance(IDataSourceInstanceHandle dataSource) {
		this.dataSource = dataSource;
	}

	public String getName() {
		return dataSource.getName();
	}

	public String getExtensionID() {
		return dataSource.getExtensionID();
	}

	/**
	 * @see org.eclipse.birt.report.engine.api.script.instance.IDataSourceInstance#getAllExtensionProperties()
	 */
	public Map getAllExtensionProperties() {
		return dataSource.getAllExtensionProperties();
	}

	/**
	 * @see org.eclipse.birt.report.engine.api.script.instance.IDataSourceInstance#getExtensionProperty(java.lang.String)
	 */
	public String getExtensionProperty(String name) {
		return dataSource.getExtensionProperty(name);
	}

	/**
	 * @see org.eclipse.birt.report.engine.api.script.instance.IDataSourceInstance#setExtensionProperty(java.lang.String,
	 *      java.lang.String)
	 */
	public void setExtensionProperty(String name, String value) {
		dataSource.setExtensionProperty(name, value);
	}

}
