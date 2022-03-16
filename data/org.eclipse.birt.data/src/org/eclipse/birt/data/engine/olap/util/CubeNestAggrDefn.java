
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
