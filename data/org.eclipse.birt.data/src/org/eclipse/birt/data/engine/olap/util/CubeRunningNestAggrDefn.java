
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
