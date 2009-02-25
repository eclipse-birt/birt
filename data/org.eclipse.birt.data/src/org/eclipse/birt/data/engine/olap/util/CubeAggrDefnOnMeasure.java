
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

public class CubeAggrDefnOnMeasure extends CubeAggrDefn
{
	private String measure;
	
	public CubeAggrDefnOnMeasure( String name, String measure, List aggrLevels, String aggrName,
			List arguments, IBaseExpression filterExpression )
	{
		super( name, aggrLevels, aggrName, arguments, filterExpression );
		this.measure = measure;
	}

	/**
	 * Return the measure that featured this aggregation.
	 * @return
	 */
	public String getMeasure( )
	{
		return measure;
	}
}
