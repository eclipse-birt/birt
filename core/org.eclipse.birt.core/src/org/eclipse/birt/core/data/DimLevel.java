/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.data;

/**
 * This class is an Implementation of IDimLevel.
 * 
 */
class DimLevel implements IDimLevel {

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

	private void setQualifiedName() {
		qualifiedName = getAttrReference(dimensionName, levelName, attrName == null ? levelName : attrName);
	}

	private String getAttrReference(String dimName, String levelName, String attrName) {
		return dimName + '/' + levelName + '/' + attrName;
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
