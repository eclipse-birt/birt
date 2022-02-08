/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.olap.api.query.IDerivedMeasureDefinition;

public class DerivedMeasureDefinition extends MeasureDefinition implements IDerivedMeasureDefinition {
	//
	private IBaseExpression expr;

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param type
	 * @param expr
	 */
	public DerivedMeasureDefinition(String name, int type, IBaseExpression expr) {
		super(name);
		super.setDataType(type);
		this.expr = expr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.api.query.IDerivedMeasureDefinition#
	 * getExpression()
	 */
	public IBaseExpression getExpression() {
		return this.expr;
	}
}
