
/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.impl;



import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class OptimizationRollbackHelper
{
	public static IQueryDefinition cloneQueryDefinition( IQueryDefinition qd ) throws DataException
	{
		return qd;
	}
	

	public static IOdaDataSetDesign cloneDataSetDesign( IOdaDataSetDesign ds ) throws DataException
	{
		return ds;
	}
}
