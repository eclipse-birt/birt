/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

	@Override
	public String getFinishExpr() {
		return getExprByIndex(2);
	}

	@Override
	public String getStartExpr() {
		return getExprByIndex(1);
	}

	@Override
	public String getTaskNameExpr() {
		return getExprByIndex(0);
	}

	@Override
	public void setFinishExpr(String expr) {
		setExprsByIndex(2, expr);
	}

	@Override
	public void setStartExpr(String expr) {
		setExprsByIndex(1, expr);
	}

	@Override
	public void setTaskNameExpr(String expr) {
		setExprsByIndex(0, expr);
	}

}
