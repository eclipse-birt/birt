
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

public class CubeRunningNestAggrDefn extends CubeNestAggrDefn
{
	private List fullLevels;
	
	public CubeRunningNestAggrDefn( String name,
			IBaseExpression basedExpression, List aggrLevels, String aggrName,
			List arguments, IBaseExpression filterExpression, List fullLevels )
	{
		super( name, basedExpression, aggrLevels, aggrName, arguments, filterExpression );
		this.fullLevels = fullLevels;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.CubeAggrDefn#getAggrLevelsInAggregationResult()
	 */
	@Override
	public List getAggrLevelsInAggregationResult( )
	{
		return fullLevels;
	}
	


}
