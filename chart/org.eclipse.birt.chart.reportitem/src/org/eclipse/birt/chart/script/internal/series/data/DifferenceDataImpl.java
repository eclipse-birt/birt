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
import org.eclipse.birt.chart.script.api.series.data.IDifferenceData;

/**
 * 
 */

public class DifferenceDataImpl extends SeriesDataImpl implements IDifferenceData {

	public DifferenceDataImpl(SeriesDefinition sd) {
		super(sd);
	}

	public String getHighExpr() {
		return getExprByIndex(0);
	}

	public String getLowExpr() {
		return getExprByIndex(1);
	}

	public void setHighExpr(String expr) {
		setExprsByIndex(0, expr);
	}

	public void setLowExpr(String expr) {
		setExprsByIndex(1, expr);
	}
}
