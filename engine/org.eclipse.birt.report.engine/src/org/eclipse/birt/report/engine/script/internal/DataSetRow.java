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
package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.report.engine.api.script.IDataSetRow;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IDataSetInstance;
import org.eclipse.birt.report.engine.script.internal.instance.DataSetInstance;

public class DataSetRow implements IDataSetRow {

	protected IDataRow row;

	public DataSetRow(IDataRow row) {
		this.row = row;
	}

	public IDataSetInstance getDataSet() {
		return new DataSetInstance(row.getDataSet());
	}

	public Object getColumnValue(int index) throws ScriptException {
		try {
			return row.getColumnValue(index);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public Object getColumnValue(String name) throws ScriptException {
		try {
			return row.getColumnValue(name);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

}
