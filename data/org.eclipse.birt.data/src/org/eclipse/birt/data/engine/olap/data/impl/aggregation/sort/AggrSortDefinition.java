
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

import java.util.logging.Logger;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;

/**
 * 
 */

public class AggrSortDefinition implements ITargetSort {
	private DimLevel[] aggrLevels;
	private String aggrName;
	private DimLevel[] axisQualifierLevel;
	private Object[] axisQualifierValue;
	private DimLevel targetLevel;
	private int direction;
	private static Logger logger = Logger.getLogger(AggrSortDefinition.class.getName());

	/**
	 * 
	 * @param aggrLevels
	 * @param aggrName
	 * @param axisQualifierLevel
	 * @param axisQualifierValue
	 * @param targetLevel
	 * @param direction
	 * @throws DataException
	 */
	public AggrSortDefinition(DimLevel[] aggrLevels, String aggrName, DimLevel[] axisQualifierLevel,
			Object[] axisQualifierValue, DimLevel targetLevel, int direction) throws DataException {
		Object[] params = { aggrLevels, aggrName, axisQualifierLevel, axisQualifierValue, targetLevel,
				Integer.valueOf(direction) };
		logger.entering(AggrSortDefinition.class.getName(), "AggrSortDefinition", //$NON-NLS-1$
				params);
		checkAxisAgrument(axisQualifierLevel, axisQualifierValue);
		this.aggrLevels = aggrLevels;
		this.aggrName = aggrName;
		this.axisQualifierLevel = axisQualifierLevel;
		this.axisQualifierValue = axisQualifierValue;
		this.targetLevel = targetLevel;
		this.direction = direction;
		logger.exiting(AggrSortDefinition.class.getName(), "AggrSortDefinition");//$NON-NLS-1$
	}

	/**
	 * @param axisQualifierLevel
	 * @param axisQualifierValue
	 * @throws DataException
	 */
	private void checkAxisAgrument(DimLevel[] axisLevels, Object[] axisValues) throws DataException {
		if (axisLevels != null && axisValues != null && axisLevels.length == axisValues.length) {
			for (int i = 0; i < axisLevels.length; i++) {
				if (axisLevels[i] == null)
					throw new DataException(ResourceConstants.AXIS_LEVEL_CANNOT_BE_NULL);
				if (axisValues[i] == null)
					throw new DataException(ResourceConstants.AXIS_VALUE_CANNOT_BE_NULL, axisLevels[i].getLevelName());
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public DimLevel[] getAggrLevels() {
		return this.aggrLevels;
	}

	/**
	 * 
	 * @return
	 */
	public String getAggrName() {
		return this.aggrName;
	}

	/**
	 * 
	 * @return
	 */
	public DimLevel[] getAxisQualifierLevel() {
		return this.axisQualifierLevel;
	}

	/**
	 * 
	 * @return
	 */
	public Object[] getAxisQualifierValue() {
		return this.axisQualifierValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.ITargetSort#
	 * getTargetLevel()
	 */
	public DimLevel getTargetLevel() {
		return this.targetLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.ITargetSort#
	 * getDirection()
	 */
	public int getSortDirection() {
		return this.direction;
	}

}
