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
import org.eclipse.birt.chart.script.api.series.data.IStockData;

/**
 * 
 */

public class StockDataImpl extends SeriesDataImpl implements IStockData {

	public StockDataImpl(SeriesDefinition sd) {
		super(sd);
	}

	public String getCloseExpr() {
		return getExprByIndex(3);
	}

	public String getHighExpr() {
		return getExprByIndex(0);
	}

	public String getLowExpr() {
		return getExprByIndex(1);
	}

	public String getOpenExpr() {
		return getExprByIndex(2);
	}

	public void setCloseExpr(String expr) {
		setExprsByIndex(3, expr);
	}

	public void setHighExpr(String expr) {
		setExprsByIndex(0, expr);
	}

	public void setLowExpr(String expr) {
		setExprsByIndex(1, expr);
	}

	public void setOpenExpr(String expr) {
		setExprsByIndex(2, expr);
	}

}
