
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.api.query;

import java.util.Collection;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;

public interface ICubeElementFactory {

	String CUBE_ELEMENT_FACTORY_CLASS_NAME = "org.eclipse.birt.data.engine.olap.impl.query.CubeElementFactory";

	/**
	 *
	 * @param name
	 * @return
	 */
	ICubeQueryDefinition createCubeQuery(String name);

	/**
	 *
	 * @param name
	 * @return
	 */
	ISubCubeQueryDefinition createSubCubeQuery(String name);

	/**
	 *
	 * @param filterExpr
	 * @param targetLevel
	 * @param axisQulifierLevel
	 * @param axisQulifierValue
	 * @return
	 */
	ICubeFilterDefinition creatCubeFilterDefinition(IBaseExpression filterExpr, ILevelDefinition targetLevel,
			ILevelDefinition[] axisQulifierLevel, Object[] axisQulifierValue);

	/**
	 * @param filterExpr
	 * @param targetLevel
	 * @param axisQulifierLevel
	 * @param axisQulifierValue
	 * @param updateAggr
	 * @return
	 */
	ICubeFilterDefinition creatCubeFilterDefinition(IBaseExpression filterExpr, ILevelDefinition targetLevel,
			ILevelDefinition[] axisQulifierLevel, Object[] axisQulifierValue, boolean updateAggr);

	/**
	 *
	 * @param targetLevels
	 * @param operator
	 * @param memberValues
	 * @return
	 */
	IFilterDefinition creatLevelMemberFilterDefinition(Collection<IScriptExpression> targetLevels, int operator,
			Collection<Collection<IScriptExpression>> memberValues);

	/**
	 *
	 * @param filterExpr
	 * @param targetLevel
	 * @param axisQulifierLevel
	 * @param axisQulifierValue
	 * @param sortDirection
	 * @return
	 */
	ICubeSortDefinition createCubeSortDefinition(IScriptExpression filterExpr, ILevelDefinition targetLevel,
			ILevelDefinition[] axisQulifierLevel, Object[] axisQulifierValue, int sortDirection);

	/**
	 *
	 * @param filterExpr
	 * @param targetLevel
	 * @param axisQulifierLevel
	 * @param axisQulifierValue
	 * @param sortDirection
	 * @return
	 */
	ICubeSortDefinition createCubeSortDefinition(String filterExpr, ILevelDefinition targetLevel,
			ILevelDefinition[] axisQulifierLevel, Object[] axisQulifierValue, int sortDirection);

	/**
	 *
	 * @param dimensionName
	 * @param hierarchyName
	 * @param levelName
	 * @return
	 */
	ILevelDefinition createLevel(String dimensionName, String hierarchyName, String levelName);

	/**
	 * @return cube operation factory to create cube operations
	 */
	ICubeOperationFactory getCubeOperationFactory();

}
