
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

import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(columnName, dimensionName, levelName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		DimColumn other = (DimColumn) obj;
		if (!Objects.equals(columnName, other.columnName)) {
			return false;
		}
		if (!Objects.equals(dimensionName, other.dimensionName)) {
			return false;
		}
		if (!Objects.equals(levelName, other.levelName)) {
			return false;
		}
		return true;
	}
}
