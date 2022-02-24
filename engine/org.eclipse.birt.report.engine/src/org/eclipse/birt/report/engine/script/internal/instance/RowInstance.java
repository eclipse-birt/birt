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

import org.eclipse.birt.report.engine.api.script.instance.IRowInstance;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * A class representing the runtime state of a detail row
 */
public class RowInstance extends ReportElementInstance implements IRowInstance {

	private IRowContent row;

	public RowInstance(IRowContent row, ExecutionContext context, RunningState runningState) {
		super(row, context, runningState);
		this.row = row;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IRowInstance#
	 * getBookmarkValue()
	 */
	public String getBookmarkValue() {
		return row.getBookmark();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IRowInstance#setBookmark(
	 * java.lang.String)
	 */
	public void setBookmark(String bookmark) {
		row.setBookmark(bookmark);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IRowInstance#getHeight()
	 */
	public String getHeight() {
		if (row.getHeight() != null)
			return row.getHeight().toString();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IRowInstance#setHeight(
	 * java.lang.String)
	 */
	public void setHeight(String height) {
		row.setHeight(DimensionType.parserUnit(height));
	}

	/*
	 * public IRowData getRowData( ) { return data; }
	 */
}
