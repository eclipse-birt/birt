
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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort;

/**
 * 
 */

public class AggrSortDefinition
{
	private String[] aggrLevels;
	private String aggrName;
	private String[] axisQualifierLevel;
	private Object[] axisQualifierValue;
	private String targetLevel;
	private boolean direction;
	
	/**
	 * 
	 * @param aggrLevels
	 * @param aggrName
	 * @param axisQualifierLevel
	 * @param axisQualifierValue
	 * @param targetLevel
	 * @param direction
	 */
	public AggrSortDefinition( String[] aggrLevels, String aggrName,
			String[] axisQualifierLevel, Object[] axisQualifierValue,
			String targetLevel, boolean direction )
	{
		this.aggrLevels = aggrLevels;
		this.aggrName = aggrName;
		this.axisQualifierLevel = axisQualifierLevel;
		this.axisQualifierValue = axisQualifierValue;
		this.targetLevel = targetLevel;
		this.direction = direction;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getAggrLevels( )
	{
		return this.aggrLevels;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAggrName( )
	{
		return this.aggrName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getAxisQualifierLevel()
	{
		return this.axisQualifierLevel;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object[] getAxisQualifierValue()
	{
		return this.axisQualifierValue;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTargetLevel()
	{
		return this.targetLevel;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getDirection()
	{
		return this.direction;
	}
	
}
