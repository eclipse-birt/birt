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
package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.report.engine.api.script.IUpdatableDataSetRow;
import org.eclipse.birt.report.engine.api.script.ScriptException;

public class UpdatableDataSetRow extends DataSetRow implements IUpdatableDataSetRow {
	UpdatableDataSetRow(IDataRow row) {
		super(row);
	}

	@Override
	public void setColumnValue(int index, Object value) throws ScriptException {
		try {
			row.setColumnValue(index, value);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public void setColumnValue(String name, Object value) throws ScriptException {
		try {
			row.setColumnValue(name, value);
		} catch (BirtException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

}
