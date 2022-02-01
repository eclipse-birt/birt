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

import org.eclipse.birt.report.engine.api.script.instance.IColumnInstance;
import org.eclipse.birt.report.engine.api.script.instance.ITableInstance;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A class representing the runtime state of a table
 */
public class TableInstance extends ReportItemInstance implements ITableInstance {

	public TableInstance(ITableContent table, ExecutionContext context, RunningState runningState) {
		super(table, context, runningState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ITableInstance#getCaption(
	 * )
	 */
	public String getCaption() {
		return ((ITableContent) content).getCaption();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.ITableInstance#setCaption(
	 * java.lang.String)
	 */
	public void setCaption(String caption) {
		((ITableContent) content).setCaption(caption);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITableInstance#
	 * getCaptionKey()
	 */
	public String getCaptionKey() {
		return ((ITableContent) content).getCaptionKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITableInstance#
	 * setCaptionKey(java.lang.String)
	 */
	public void setCaptionKey(String captionKey) {
		((ITableContent) content).setCaptionKey(captionKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITableInstance#
	 * getRepeatHeader()
	 */
	public boolean getRepeatHeader() {
		return ((ITableContent) content).isHeaderRepeat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.ITableInstance#
	 * setRepeatHeader(boolean)
	 */
	public void setRepeatHeader(boolean repeat) {
		((ITableContent) content).setHeaderRepeat(repeat);
	}

	/**
	 * Get the summary.
	 * 
	 */
	public String getSummary() {
		return ((ITableContent) content).getSummary();
	}

	/**
	 * Set the summary
	 * 
	 */
	public void setSummary(String summary) {
		((ITableContent) content).setSummary(summary);
	}

	public int getColumnCount() {
		return ((ITableContent) content).getColumnCount();
	}

	public IColumnInstance getColumn(int index) {
		if (index >= getColumnCount() || index < 0) {
			throw new RuntimeException("Invalid column index : " + index);
		}
		IColumn column = ((ITableContent) content).getColumn(index);
		return new ColumnInstance(column, runningState);
	}
}
