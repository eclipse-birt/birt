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
import org.eclipse.birt.chart.script.api.series.data.ISimpleData;

/**
 * 
 */

public class SimpleDataImpl extends SeriesDataImpl implements ISimpleData {

	public SimpleDataImpl(SeriesDefinition sd) {
		super(sd);
	}

	public String getExpr() {
		return getExprByIndex(0);
	}

	public void setExpr(String expr) {
		setExprsByIndex(0, expr);

	}

}
