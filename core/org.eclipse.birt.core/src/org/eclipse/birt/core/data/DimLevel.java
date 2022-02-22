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

package org.eclipse.birt.core.data;

import java.util.Objects;

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
	@Override
	public String getDimensionName() {
		return dimensionName;
	}

	/**
	 * @return the levelName
	 */
	@Override
	public String getLevelName() {
		return levelName;
	}

	/**
	 * Return the attribute name.
	 *
	 * @return
	 */
	@Override
	public String getAttrName() {
		return this.attrName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(dimensionName, levelName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || !(obj instanceof DimLevel)) {
			return false;
		}
		final DimLevel other = (DimLevel) obj;
		if (!Objects.equals(dimensionName, other.dimensionName)) {
			return false;
		}
		if (!Objects.equals(levelName, other.levelName)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
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
