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

	public void setHelpTextKey(String value) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IDataItem) designElementImpl).setHelpTextKey(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public IAction getAction() {

		return new ActionImpl(((org.eclipse.birt.report.model.api.simpleapi.IDataItem) designElementImpl).getAction());
	}

	public void addAction(IAction action) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IDataItem#
	 * getResultSetColumn()
	 */
	public String getResultSetColumn() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IDataItem) designElementImpl).getResultSetColumn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IDataItem#
	 * setResultSetColumn(java.lang.String)
	 */
	public void setResultSetColumn(String columnName) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IDataItem) designElementImpl).setResultSetColumn(columnName);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}
}
