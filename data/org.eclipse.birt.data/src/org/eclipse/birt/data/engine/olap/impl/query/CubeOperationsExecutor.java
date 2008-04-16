/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.impl.query;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;

/**
 * A cube operation executor to execute all the prepared cube operations one by one
 * 
 */
public class CubeOperationsExecutor
{
	private ICubeQueryDefinition cubeQueryDefinition;
	private IPreparedCubeOperation[] cubeOperations;

	public CubeOperationsExecutor( 
			ICubeQueryDefinition cubeQueryDefinition,
			IPreparedCubeOperation[] cubeOperations) throws DataException
	{
		this.cubeQueryDefinition = cubeQueryDefinition;
		this.cubeOperations = cubeOperations;
	}

	/**
	 * execute all the cube operations
	 * 
	 * @param source:
	 *            the common execution result of cubeQueryDefn
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws BirtException
	 */
	public IAggregationResultSet[] execute( IAggregationResultSet[] source,
			StopSign stopSign ) throws IOException,
			BirtException
	{
		IAggregationResultSet[] currentResult = source;
		for ( IPreparedCubeOperation co : cubeOperations )
		{
			currentResult = co.execute( cubeQueryDefinition,
					currentResult,
					stopSign );

		}
		return currentResult;
	}
}
