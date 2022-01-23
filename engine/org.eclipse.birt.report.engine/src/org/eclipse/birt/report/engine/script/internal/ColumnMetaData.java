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
package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.script.IColumnMetaData;
import org.eclipse.birt.report.engine.api.script.ScriptException;

public class ColumnMetaData implements IColumnMetaData {

	private IResultMetaData meta;

	public ColumnMetaData(IResultMetaData meta) {
		this.meta = meta;
	}

	public int getColumnCount() {
		return meta.getColumnCount();
	}

	public String getColumnName(int index) throws ScriptException {
		try {
			return meta.getColumnName(index);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public String getColumnAlias(int index) throws ScriptException {
		try {
			return meta.getColumnAlias(index);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public int getColumnType(int index) throws ScriptException {
		try {
			return meta.getColumnType(index);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public String getColumnTypeName(int index) throws ScriptException {
		try {
			return meta.getColumnTypeName(index);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public String getColumnNativeTypeName(int index) throws ScriptException {
		try {
			return meta.getColumnNativeTypeName(index);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public String getColumnLabel(int index) throws ScriptException {
		try {
			return meta.getColumnLabel(index);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public boolean isComputedColumn(int index) throws ScriptException {
		try {
			return meta.isComputedColumn(index);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

}
