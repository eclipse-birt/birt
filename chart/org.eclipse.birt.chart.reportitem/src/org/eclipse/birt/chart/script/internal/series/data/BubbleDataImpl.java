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
import org.eclipse.birt.chart.script.api.series.data.IBubbleData;

/**
 * 
 */

public class BubbleDataImpl extends SeriesDataImpl implements IBubbleData {

	public BubbleDataImpl(SeriesDefinition sd) {
		super(sd);
	}

	public String getBubbleSizeExpr() {
		return getExprByIndex(1);
	}

	public String getOrthogonalValueExpr() {
		return getExprByIndex(0);
	}

	public void setBubbleSizeExpr(String expr) {
		setExprsByIndex(1, expr);

	}

	public void setOrthogonalValueExpr(String expr) {
		setExprsByIndex(0, expr);
	}

}
