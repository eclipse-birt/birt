/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.NamedObject;

/**
 * Default implementation of the
 * {@link org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition}
 * interface.
 *
 */
public class SubCubeQueryDefinition extends NamedObject implements ISubCubeQueryDefinition {
	private String startingLevelOnColumn, startingLevelOnRow;

	/**
	 *
	 * @param name
	 * @param startingLevelOnColumn
	 * @param startingLevelOnRow
	 */
	public SubCubeQueryDefinition(String name, String startingLevelOnColumn, String startingLevelOnRow) {
		super(name);
		this.startingLevelOnColumn = startingLevelOnColumn;
		this.startingLevelOnRow = startingLevelOnRow;
	}

	/**
	 *
	 * @param name
	 */
	public SubCubeQueryDefinition(String name) {
		super(name);
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition#
	 * getStartingLevelOnColumn()
	 */
	@Override
	public String getStartingLevelOnColumn() {
		return startingLevelOnColumn;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition#
	 * getStartingLevelOnRow()
	 */
	@Override
	public String getStartingLevelOnRow() {
		return startingLevelOnRow;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition#
	 * setStartingLevelOnColumn(java.lang.String)
	 */
	@Override
	public void setStartingLevelOnColumn(String level) {
		this.startingLevelOnColumn = level;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition#
	 * setStartingLevelOnRow(java.lang.String)
	 */
	@Override
	public void setStartingLevelOnRow(String level) {
		this.startingLevelOnRow = level;
	}

}
