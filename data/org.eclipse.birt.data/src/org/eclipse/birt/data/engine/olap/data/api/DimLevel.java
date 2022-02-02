/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.data.api;

import org.eclipse.birt.core.data.IDimLevel;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;

/**
 * This class is responsible for encapsulating level naming information in the
 * cube. All levels' names are not guarantee to be unique since Birt2.2RC0,
 * which means two levels in different dimensions maybe share the same level
 * name. Using DimLevel object will avoid these kind of conflict, since it use
 * the qualified name to identify a level. The qualified name of a level
 * consists of the dimension name and level name and separated with splash.
 * 
 * 
 */
public class DimLevel implements Comparable, IDimLevel {

	private String dimensionName;
	private String levelName;
	private String attrName;

	private String qualifiedName;

	/**
	 * @param dimensionName
	 * @param levelName
	 */
	public DimLevel(String dimensionName, String levelName) {
		this(dimensionName, levelName, null);
	}

	public DimLevel(String dimensionName, String levelName, String attrName) {
		this.dimensionName = dimensionName;
		this.levelName = levelName;
		this.attrName = attrName;
		setQualifiedName();
	}

	/**
	 * @param levelDefn
	 */
	public DimLevel(ILevelDefinition levelDefn) {
		this.levelName = levelDefn.getName();
		this.dimensionName = levelDefn.getHierarchy().getDimension().getName();
		setQualifiedName();
	}

	private void setQualifiedName() {
		qualifiedName = OlapExpressionUtil.getAttrReference(dimensionName, levelName,
				attrName == null ? levelName : attrName);
	}

	/**
	 * @return the dimensionName
	 */
	public String getDimensionName() {
		return dimensionName;
	}

	/**
	 * @return the levelName
	 */
	public String getLevelName() {
		return levelName;
	}

	/**
	 * Return the attribute name.
	 * 
	 * @return
	 */
	public String getAttrName() {
		return this.attrName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dimensionName == null) ? 0 : dimensionName.hashCode());
		result = prime * result + ((levelName == null) ? 0 : levelName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DimLevel))
			return false;
		final DimLevel other = (DimLevel) obj;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return qualifiedName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(T)
	 */
	public int compareTo(Object obj) {
		if (obj == null || !(obj instanceof DimLevel)) {
			return -1;
		}
		DimLevel dimLevel = (DimLevel) obj;
		return this.toString().compareTo(dimLevel.toString());
	}
}
