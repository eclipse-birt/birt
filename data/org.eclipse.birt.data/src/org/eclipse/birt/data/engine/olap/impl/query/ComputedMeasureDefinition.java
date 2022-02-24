
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.olap.api.query.IComputedMeasureDefinition;

/**
 * This class is an implementation of IComputedMeasureDefinition.
 */

public class ComputedMeasureDefinition extends MeasureDefinition implements IComputedMeasureDefinition {
	//
	private IBaseExpression expr;

	/**
	 * Constructor.
	 *
	 * @param name
	 * @param type
	 * @param expr
	 */
	public ComputedMeasureDefinition(String name, int type, IBaseExpression expr) {
		super(name);
		super.setDataType(type);
		this.expr = expr;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.api.query.IComputedMeasureDefinition#
	 * getExpression()
	 */
	@Override
	public IBaseExpression getExpression() {
		return this.expr;
	}
}
