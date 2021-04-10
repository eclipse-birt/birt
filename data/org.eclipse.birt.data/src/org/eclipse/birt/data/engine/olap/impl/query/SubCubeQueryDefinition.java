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
	public String getStartingLevelOnColumn() {
		return startingLevelOnColumn;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition#
	 * getStartingLevelOnRow()
	 */
	public String getStartingLevelOnRow() {
		return startingLevelOnRow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition#
	 * setStartingLevelOnColumn(java.lang.String)
	 */
	public void setStartingLevelOnColumn(String level) {
		this.startingLevelOnColumn = level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition#
	 * setStartingLevelOnRow(java.lang.String)
	 */
	public void setStartingLevelOnRow(String level) {
		this.startingLevelOnRow = level;
	}

}
