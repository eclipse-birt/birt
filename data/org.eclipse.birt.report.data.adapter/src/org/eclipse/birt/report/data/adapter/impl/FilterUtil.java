/*******************************************************************************
 * Copyright (c) 2004,2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.adapter.impl;

import org.eclipse.birt.report.data.adapter.api.IFilterUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;


public class FilterUtil implements IFilterUtil
{

	/*
	 * 
	 */
	public int[] populatedPushdownableFilterOperators( DataSetHandle dataSet, int[] candidateOperator )
	{
		throw new UnsupportedOperationException( );
	}

}
