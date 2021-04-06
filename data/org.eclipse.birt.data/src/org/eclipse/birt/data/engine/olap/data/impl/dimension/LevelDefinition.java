
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
package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.util.logging.Logger;

import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;

/**
 * Defines a level. A level definition includes key columns and attribute
 * columns.
 */

public class LevelDefinition implements ILevelDefn {
	private String name = null;
	private String[] keyColumns = null;
	private String[] attributeColumns = null;
	private String timeType = null;
	private static Logger logger = Logger.getLogger(LevelDefinition.class.getName());

	/**
	 * 
	 * @param name
	 * @param keyColumns
	 * @param attributeColumns
	 */
	public LevelDefinition(String name, String[] keyColumns, String[] attributeColumns) {
		Object[] params = { name, keyColumns, attributeColumns };
		logger.entering(LevelDefinition.class.getName(), "LevelDefinition", params);
		this.name = name;
		this.keyColumns = keyColumns;
		this.attributeColumns = attributeColumns;
		logger.exiting(LevelDefinition.class.getName(), "LevelDefinition");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.api.ILevelDefinition#getAttributeColumns()
	 */
	public String[] getAttributeColumns() {
		return attributeColumns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.api.ILevelDefinition#getKeyColumns()
	 */
	public String[] getKeyColumns() {
		return keyColumns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.api.ILevelDefinition#getLevelName()
	 */
	public String getLevelName() {
		return name;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn#getTimeType()
	 */
	public String getTimeType() {
		return timeType;
	}

}
