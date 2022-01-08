/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.ir.DimensionType;

public class ColumnInstance implements IColumnInstance {

	private IColumn column;
	private RunningState runningState;

	public ColumnInstance(IColumn column, RunningState runningState) {
		this.column = column;
		this.runningState = runningState;
	}

	public IScriptStyle getStyle() {
		return new StyleInstance(column.getStyle(), runningState);
	}

	public String getWidth() {
		DimensionType width = column.getWidth();
		if (width != null) {
			return width.toString();
		}
		return null;
	}

	public void setWidth(String width) {
		column.setWidth(DimensionType.parserUnit(width));
	}
}
