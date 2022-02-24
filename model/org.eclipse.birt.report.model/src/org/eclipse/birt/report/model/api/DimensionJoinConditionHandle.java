/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Represents a dimension join condition in the DimensionCondition. It defines
 * two keys for the cube and hierarchy join, one is from cube and another is
 * from hierarchy.
 */
public class DimensionJoinConditionHandle extends StructureHandle {

	/**
	 * Constructs a dimension join condition handle with the given
	 * <code>SimpleValueHandle</code> and the index of the dimension join condition
	 * in the dimension condition.
	 * 
	 * @param valueHandle handle to a list property or member
	 * @param index       index of the structure within the list
	 */

	public DimensionJoinConditionHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Gets the cube key in this dimension join condition.
	 * 
	 * @return the cube key in this dimension join condition
	 */
	public String getCubeKey() {
		return getStringProperty(DimensionJoinCondition.CUBE_KEY_MEMBER);
	}

	/**
	 * Sets the cube key in this dimension join condition.
	 * 
	 * @param cubeKey the cube key to set
	 */
	public void setCubeKey(String cubeKey) {
		setPropertySilently(DimensionJoinCondition.CUBE_KEY_MEMBER, cubeKey);
	}

	/**
	 * Gets the hierarchy key in this dimension join condition.
	 * 
	 * @return the hierarchy key in this dimension join condition
	 */
	public String getHierarchyKey() {
		return getStringProperty(DimensionJoinCondition.HIERARCHY_KEY_MEMBER);
	}

	/**
	 * Sets the hierarchy key in this dimension join condition.
	 * 
	 * @param hierarchyKey the hierarchy key to set
	 */
	public void setHierarchyKey(String hierarchyKey) {
		setPropertySilently(DimensionJoinCondition.HIERARCHY_KEY_MEMBER, hierarchyKey);
	}

	/**
	 * Gets the referred level element handle of this condition.
	 * 
	 * @return level element handle of this condition if found, otherwise null
	 */
	public LevelHandle getLevel() {
		ElementRefValue refValue = (ElementRefValue) ((Structure) getStructure()).getLocalProperty(getModule(),
				DimensionJoinCondition.LEVEL_MEMBER);
		if (refValue == null || !refValue.isResolved())
			return null;
		DesignElement element = refValue.getElement();
		return (LevelHandle) element.getHandle(element.getRoot());
	}

	/**
	 * Gets the referred level full name of this condition.
	 * 
	 * @return level full name of this condition if set, otherwise null
	 */
	public String getLevelName() {
		return getStringProperty(DimensionJoinCondition.LEVEL_MEMBER);
	}

	/**
	 * Sets the referred level by the name.
	 * 
	 * @param levelName the full name of the level element to set
	 * @throws SemanticException
	 */
	public void setLevel(String levelName) throws SemanticException {
		setProperty(DimensionJoinCondition.LEVEL_MEMBER, levelName);
	}

	/**
	 * Sets the referred level by the handle.
	 * 
	 * @param levelHandle the level handle to set
	 * @throws SemanticException
	 */
	public void setLevel(LevelHandle levelHandle) throws SemanticException {
		DesignElement element = levelHandle == null ? null : levelHandle.getElement();
		setProperty(DimensionJoinCondition.LEVEL_MEMBER, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof DimensionJoinConditionHandle))
			return false;

		DimensionJoinConditionHandle temp = (DimensionJoinConditionHandle) obj;

		return (temp.structContext.equals(this.structContext) && temp.elementHandle.equals(this.elementHandle));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */

	public int hashCode() {
		int hash = 1;

		hash = 7 * hash + this.structContext.getIndex(getModule());
		hash = 7 * hash + this.structContext.hashCode();
		hash = 7 * hash + this.elementHandle.hashCode();
		return hash;
	}
}
