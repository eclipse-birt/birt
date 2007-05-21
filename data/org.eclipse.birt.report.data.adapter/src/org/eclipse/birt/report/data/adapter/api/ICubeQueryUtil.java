
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
package org.eclipse.birt.report.data.adapter.api;

import java.util.List;

import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;

/**
 * 
 */

public interface ICubeQueryUtil
{
	/**
	 * Utility method to acquire referable bindings, either in cube filter or
	 * cube sort.
	 * 
	 * @param targetLevel
	 * @param bindings
	 * @param isSort
	 * @return
	 * @throws AdapterException
	 */
	public List getReferableBindings( String targetLevel,
			ICubeQueryDefinition cubeQueryDefn, boolean isSort ) throws AdapterException;
	
	/**
	 * Return a list of ILevelDefinition instances that referenced by 
	 * 
	 * @param targetLevel
	 * @param bindingExpr
	 * @param queryDefn
	 * @return
	 * @throws AdapterException
	 */
	public List getReferencedLevels( String targetLevel,
			String bindingExpr, ICubeQueryDefinition queryDefn ) throws AdapterException;

	/**
	 * Return the measure name referenced by the expression.
	 * 
	 * @param expr
	 * @return
	 * @throws AdapterException
	 */
	public String getReferencedMeasureName( String expr ) throws AdapterException;
}
