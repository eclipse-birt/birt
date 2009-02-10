
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow;


/**
 * 
 */

public class FacttableRow implements IFacttableRow
{
	private MeasureInfo[] measureInfo;
	private Object[] measureValues;
	
	/**
	 * 
	 * @param measureInfo
	 */
	FacttableRow( MeasureInfo[] measureInfo )
	{
		this.measureInfo = measureInfo;
	}
	
	/**
	 * 
	 * @param measureValues
	 */
	void setMeasure( Object[] measureValues )
	{
		this.measureValues = measureValues;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow#getMeasureValue(java.lang.String)
	 */
	public Object getMeasureValue( String measureName ) throws DataException
	{
		for ( int i = 0; i < measureInfo.length; i++ )
		{
			if(measureInfo[i].getMeasureName().equals( measureName ))
			{
				return measureValues[i];
			}
		}
		return null;
	}

	public Object getLevelValue( String dimensionName, String levelName )
			throws DataException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object getLevelAttributeValue( String dimensionName,
			String levelName, String attribute ) throws DataException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] getLevelKeyValue( String dimensionName, String levelName )
			throws DataException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
