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
import org.eclipse.birt.report.engine.api.script.element.IAction;
import org.eclipse.birt.report.engine.api.script.element.IDataItem;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class DataItem extends ReportItem implements IDataItem {

	public DataItem(DataItemHandle data) {
		super(data);
	}

	public DataItem(org.eclipse.birt.report.model.api.simpleapi.IDataItem dataItem) {
		super(dataItem);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IDataItem#getValueExpr()
	 */

	@Override
	public String getHelpText() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IDataItem) designElementImpl).getHelpText();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IDataItem#setHelpText(java.
	 * lang.String)
	 */

	@Override
	public void setHelpText(String value) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IDataItem) designElementImpl).setHelpText(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IDataItem#getHelpTextKey()
	 */

	@Override
	public String getHelpTextKey() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IDataItem) designElementImpl).getHelpTextKey();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IDataItem#setHelpTextKey(
	 * java.lang.String)
	 */

	@Override
	public void setHelpTextKey(String value) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IDataItem) designElementImpl).setHelpTextKey(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public IAction getAction() {

		return new ActionImpl(((org.eclipse.birt.report.model.api.simpleapi.IDataItem) designElementImpl).getAction());
	}

	@Override
	public void addAction(IAction action) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IDataItem#
	 * getResultSetColumn()
	 */
	@Override
	public String getResultSetColumn() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IDataItem) designElementImpl).getResultSetColumn();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IDataItem#
	 * setResultSetColumn(java.lang.String)
	 */
	@Override
	public void setResultSetColumn(String columnName) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IDataItem) designElementImpl).setResultSetColumn(columnName);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}
}
