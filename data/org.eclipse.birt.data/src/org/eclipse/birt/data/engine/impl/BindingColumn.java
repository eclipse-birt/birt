/*******************************************************************************
 * Copyright (c) 2004, 2012 Actuate Corporation.
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
