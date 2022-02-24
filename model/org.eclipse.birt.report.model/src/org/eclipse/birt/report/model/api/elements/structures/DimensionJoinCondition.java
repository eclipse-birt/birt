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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.core.PropertyStructure;

/**
 * Represents a dimension join condition in the DimensionCondition. It defines
 * two keys for the cube and hierarchy join, one is from cube and another is
 * from hierarchy.
 */
public class DimensionJoinCondition extends PropertyStructure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public final static String DIMENSION_JOIN_CONDITION_STRUCT = "DimensionJoinCondition"; //$NON-NLS-1$

	/**
	 * Property name of the cube key of this join.
	 */

	public final static String CUBE_KEY_MEMBER = "cubeKey"; //$NON-NLS-1$

	/**
	 * Property name of the hierarchy key of this join.
	 */
	public final static String HIERARCHY_KEY_MEMBER = "hierarchyKey"; //$NON-NLS-1$

	/**
	 * Name of the member that specifies the level name of this condition relates.
	 */
	public final static String LEVEL_MEMBER = "level"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */
	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new DimensionJoinConditionHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IStructure#getStructName()
	 */
	public String getStructName() {
		return DIMENSION_JOIN_CONDITION_STRUCT;
	}

	/**
	 * Sets the cube key.
	 * 
	 * @param cubeKey the key to set
	 */
	public void setCubeKey(String cubeKey) {
		setProperty(CUBE_KEY_MEMBER, cubeKey);
	}

	/**
	 * Gets the cube key of this join condition.
	 * 
	 * @return the cube key in this join condition
	 */
	public String getCubeKey() {
		return (String) getProperty(null, CUBE_KEY_MEMBER);
	}

	/**
	 * Sets the hierarchy key.
	 * 
	 * @param hierarchyKey the key to set
	 */
	public void setHierarchyKey(String hierarchyKey) {
		setProperty(HIERARCHY_KEY_MEMBER, hierarchyKey);
	}

	/**
	 * Gets the hierarchy key of this join condition.
	 * 
	 * @return the hierarchy key in this join condition
	 */
	public String getHierarchyKey() {
		return (String) getProperty(null, HIERARCHY_KEY_MEMBER);
	}
}
