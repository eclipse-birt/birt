/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script.internal.series.data;

import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.script.api.series.data.IGanttData;

/**
 * 
 */

public class GanttDataImpl extends SeriesDataImpl implements IGanttData {

	public GanttDataImpl(SeriesDefinition sd) {
		super(sd);
	}

	public String getFinishExpr() {
		return getExprByIndex(2);
	}

	public String getStartExpr() {
		return getExprByIndex(1);
	}

	public String getTaskNameExpr() {
		return getExprByIndex(0);
	}

	public void setFinishExpr(String expr) {
		setExprsByIndex(2, expr);
	}

	public void setStartExpr(String expr) {
		setExprsByIndex(1, expr);
	}

	public void setTaskNameExpr(String expr) {
		setExprsByIndex(0, expr);
	}

}
