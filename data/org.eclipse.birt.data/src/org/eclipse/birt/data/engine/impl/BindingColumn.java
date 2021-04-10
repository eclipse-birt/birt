/*******************************************************************************
 * Copyright (c) 2004, 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.api.IBaseExpression;

/**
 * A simple wrapper for binding column
 */

public class BindingColumn {

	public String columnName;
	public IBaseExpression baseExpr;
	public boolean isAggregation;
	public int type;

	public BindingColumn(String columnName, IBaseExpression baseExpr, boolean isAggregation, int type) {
		this.columnName = columnName;
		this.baseExpr = baseExpr;
		this.isAggregation = isAggregation;
		this.type = type;
	}
}
