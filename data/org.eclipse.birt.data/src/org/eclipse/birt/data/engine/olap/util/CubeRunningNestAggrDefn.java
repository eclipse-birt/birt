
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
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;

/**
 *
 */

public class CubeRunningNestAggrDefn extends CubeNestAggrDefn {
	private List<DimLevel> fullLevels;

	private List<IScriptExpression> notLevelArguments;

	public CubeRunningNestAggrDefn(String name, IBaseExpression basedExpression, List aggrLevels, String aggrName,
			List arguments, IBaseExpression filterExpression, List fullLevels, List notLevelArguments) {
		super(name, basedExpression, aggrLevels, aggrName, arguments, filterExpression);
		this.fullLevels = fullLevels;
		this.notLevelArguments = notLevelArguments;
	}

	/**
	 * @return the notLevelArguments
	 */
	public List<IScriptExpression> getNotLevelArguments() {
		return notLevelArguments;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.util.CubeAggrDefn#
	 * getAggrLevelsInAggregationResult()
	 */
	@Override
	public List<DimLevel> getAggrLevelsInAggregationResult() {
		return fullLevels;
	}

}
