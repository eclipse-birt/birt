
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
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

/**
 * 
 */

public class OdaQueryOptimizationUtil
{
	public static QuerySpecification optimizeExecution(ScriptContext context,
			String dataSourceId,
			IOdaDataSetDesign dataSetDesign, IQueryDefinition query )
	{
		return null;
	}
}
