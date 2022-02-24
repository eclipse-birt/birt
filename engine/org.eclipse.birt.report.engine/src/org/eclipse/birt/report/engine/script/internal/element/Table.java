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
import org.eclipse.birt.report.engine.api.script.element.IColumn;
import org.eclipse.birt.report.engine.api.script.element.ITable;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class Table extends Listing implements ITable {

	public Table(TableHandle table) {
		super(table);
	}

	public Table(org.eclipse.birt.report.model.api.simpleapi.ITable tabelImpl) {
		super(null);
		designElementImpl = tabelImpl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITable#getColumnCount()
	 */

	public int getColumnCount() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ITable) designElementImpl).getColumnCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#repeatHeader()
	 */

	public boolean repeatHeader() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ITable) designElementImpl).repeatHeader();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITable#setRepeatHeader(
	 * boolean)
	 */

	public void setRepeatHeader(boolean value) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.ITable) designElementImpl).setRepeatHeader(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#getCaption()
	 */

	public String getCaption() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ITable) designElementImpl).getCaption();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITable#setCaption(java.lang
	 * .String)
	 */

	public void setCaption(String caption) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.ITable) designElementImpl).setCaption(caption);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#getCaptionKey()
	 */

	public String getCaptionKey() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ITable) designElementImpl).getCaptionKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITable#setCaptionKey(java.
	 * lang.String)
	 */

	public void setCaptionKey(String captionKey) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.ITable) designElementImpl).setCaptionKey(captionKey);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#getColumn(int)
	 */

	public IColumn getColumn(int index) {
		return new Column(((org.eclipse.birt.report.model.api.simpleapi.ITable) designElementImpl).getColumn(index));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#getSummary()
	 */
	public String getSummary() {
		return ((org.eclipse.birt.report.model.api.simpleapi.ITable) designElementImpl).getSummary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.ITable#setSummary(java.lang
	 * .String)
	 */
	public void setSummary(String summary) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.ITable) designElementImpl).setSummary(summary);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

}
