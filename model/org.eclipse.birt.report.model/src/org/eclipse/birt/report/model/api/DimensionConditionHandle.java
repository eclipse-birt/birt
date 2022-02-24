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
import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Represents the handle of the cube-dimension/hierarchy join condition.
 * 
 * <p>
 * <dl>
 * <dt><strong>Primary Keys </strong></dt>
 * <dd>Primary keys define a list of primary key to do the join actions between
 * cube and hierarchy in dimension. Each one in the list must be one of the data
 * set column in data set defined in cube.</dd>
 * <dt><strong>Hierarchy</strong></dt>
 * <dd>Hierarchy refers a hierarchy element in one of the dimension in the cube.
 * </dd>
 * </dl>
 * 
 */

public class DimensionConditionHandle extends StructureHandle {

	/**
	 * Constructs the handle of the cube join condition.
	 * 
	 * @param valueHandle the value handle for the cube join condition list of one
	 *                    property
	 * @param index       the position of this join condition in the list
	 */

	public DimensionConditionHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Gets the member handle to deal with all the
	 * <code>DimensionJoinCondition</code>.
	 * 
	 * @return the member handle for all the DimensionJoinCondition
	 */
	public MemberHandle getJoinConditions() {
		return getMember(DimensionCondition.JOIN_CONDITIONS_MEMBER);
	}

	/**
	 * Gets the dimension join condition handle.
	 * 
	 * @param joinCondition the join condition.
	 * @return the dimension join condition handle.
	 * @throws SemanticException
	 */
	public DimensionJoinConditionHandle addJoinCondition(DimensionJoinCondition joinCondition)
			throws SemanticException {
		return (DimensionJoinConditionHandle) getJoinConditions().addItem(joinCondition);
	}

	/**
	 * Removes join condition from this dimension condition.
	 * 
	 * @param joinCondition the join condition to remove
	 * @throws SemanticException
	 */
	public void removeJoinCondition(DimensionJoinCondition joinCondition) throws SemanticException {
		getJoinConditions().removeItem(joinCondition);
	}

	/**
	 * Remove the join condition in the specified position.
	 * 
	 * @param index the position where the join condition resides
	 * @throws SemanticException
	 */
	public void removeJoinCondition(int index) throws SemanticException {
		getJoinConditions().removeItem(index);
	}

	/**
	 * Gets the referred hierarchy handle of this condition.
	 * 
	 * @return hierarchy handle of this condition if found, otherwise null
	 */
	public HierarchyHandle getHierarchy() {
		ElementRefValue refValue = (ElementRefValue) ((Structure) getStructure()).getLocalProperty(getModule(),
				DimensionCondition.HIERARCHY_MEMBER);
		if (refValue == null || !refValue.isResolved())
			return null;
		DesignElement element = refValue.getElement();
		return (HierarchyHandle) element.getHandle(element.getRoot());
	}

	/**
	 * Gets the referred hierarchy name of this condition.
	 * 
	 * @return hierarchy name of this condition if set, otherwise null
	 */
	public String getHierarchyName() {
		return getStringProperty(DimensionCondition.HIERARCHY_MEMBER);
	}

	/**
	 * Sets the referred hierarchy by the name.
	 * 
	 * @param hierarchyName the hierarchy name to set
	 * @throws SemanticException
	 */
	public void setHierarchy(String hierarchyName) throws SemanticException {
		setProperty(DimensionCondition.HIERARCHY_MEMBER, hierarchyName);
	}

	/**
	 * Sets the referred hierarchy by the handle.
	 * 
	 * @param hierarchyHandle the hierarchy handle to set
	 * @throws SemanticException
	 */
	public void setHierarchy(HierarchyHandle hierarchyHandle) throws SemanticException {
		DesignElement element = hierarchyHandle == null ? null : hierarchyHandle.getElement();
		setProperty(DimensionCondition.HIERARCHY_MEMBER, element);
	}
}
