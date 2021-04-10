/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.api.query;

/**
 * ISubCubeQueryDefinition is an interface to define a sub query in cube query.
 * It provide the start level on row/column to indicate starting from which
 * level should the sub cube cursor includes a full set of level member
 */
public interface ISubCubeQueryDefinition extends IBaseCubeQueryDefinition {

	/**
	 * @return the starting level on row edge
	 */
	public String getStartingLevelOnRow();

	/**
	 * @return the starting level on column edge
	 */
	public String getStartingLevelOnColumn();

	/**
	 * 
	 * @param level
	 */
	public void setStartingLevelOnColumn(String level);

	/**
	 * 
	 * @param level
	 */
	public void setStartingLevelOnRow(String level);
}
