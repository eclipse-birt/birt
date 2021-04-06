
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public IBaseExpression getExpression() {
		return this.expr;
	}
}
