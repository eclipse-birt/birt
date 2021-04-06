
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.util;

import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;

/**
 * 
 */

public class CubeNestAggrDefn extends CubeAggrDefn {
	private IBaseExpression basedExpression;

	public CubeNestAggrDefn(String name, IBaseExpression basedExpression, List aggrLevels, String aggrName,
			List arguments, IBaseExpression filterExpression) {
		super(name, aggrLevels, aggrName, null, arguments, filterExpression);
		this.basedExpression = basedExpression;
	}

	/**
	 * Return the expression that featured this aggregation.
	 * 
	 * @return
	 */
	public IBaseExpression getBasedExpression() {
		return basedExpression;
	}

	@Override
	public String getMeasure() {
		return this.getName();
	}

}
