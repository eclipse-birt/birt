
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
package org.eclipse.birt.data.engine.olap.data.impl;

/**
 * 
 */

public class DimColumn {
	private String dimensionName;
	private String levelName;
	private String columnName;

	public DimColumn(String dimensionName, String levelName, String columnName) {
		this.dimensionName = dimensionName;
		this.levelName = levelName;
		this.columnName = columnName;
	}

	public String getDimensionName() {
		return dimensionName;
	}

	public String getLevelName() {
		return levelName;
	}

	public String getColumnName() {
		return columnName;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result + ((dimensionName == null) ? 0 : dimensionName.hashCode());
		result = prime * result + ((levelName == null) ? 0 : levelName.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DimColumn other = (DimColumn) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (dimensionName == null) {
			if (other.dimensionName != null)
				return false;
		} else if (!dimensionName.equals(other.dimensionName))
			return false;
		if (levelName == null) {
			if (other.levelName != null)
				return false;
		} else if (!levelName.equals(other.levelName))
			return false;
		return true;
	}
}
