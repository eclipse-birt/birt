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
package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IDataSource;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

public class DataSource implements IDataSource {

	private org.eclipse.birt.report.model.api.simpleapi.IDataSource dataSourceImpl;

	public DataSource(DataSourceHandle dataSource) {
		dataSourceImpl = SimpleElementFactory.getInstance().createDataSource(dataSource);
	}

	public DataSource(org.eclipse.birt.report.model.api.simpleapi.IDataSource dataSource) {
		dataSourceImpl = dataSource;
	}

	@Override
	public String getExtensionID() {
		return dataSourceImpl.getExtensionID();
	}

	@Override
	public String getPrivateDriverProperty(String name) {
		return dataSourceImpl.getPrivateDriverProperty(name);
	}

	@Override
	public void setPrivateDriverProperty(String name, String value) throws ScriptException {
		try {
			dataSourceImpl.setPrivateDriverProperty(name, value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

}
