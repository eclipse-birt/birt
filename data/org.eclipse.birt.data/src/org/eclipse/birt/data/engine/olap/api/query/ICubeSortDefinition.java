
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

import org.eclipse.birt.data.engine.api.ISortDefinition;

/**
 * The sort definition for cube query to define a sort against aggregations.
 */
public interface ICubeSortDefinition extends ISortDefinition {
	/**
	 * The targeting level that this sort definition will act against.
	 * 
	 * @return
	 */
	public ILevelDefinition getTargetLevel();

	/**
	 * Return the Axis qualifier level.
	 * 
	 * @return
	 */
	public ILevelDefinition[] getAxisQualifierLevels();

	/**
	 * Return the Axis qualifier value
	 * 
	 * @return
	 */
	public Object[] getAxisQualifierValues();
}
