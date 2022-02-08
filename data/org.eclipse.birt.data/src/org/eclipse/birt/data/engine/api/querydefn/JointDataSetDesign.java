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

package org.eclipse.birt.data.engine.api.querydefn;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * This is an implementation of IJointDataSetDesign
 */
public class JointDataSetDesign extends BaseDataSetDesign implements IJointDataSetDesign {

	//
	private String left;
	private String right;

	/**
	 * The join type of this JointDataSet. May one of inner join, left outer join
	 * and right outer join.
	 */
	private int joinType = IJointDataSetDesign.INNER_JOIN;

	/**
	 * A list of join conditions.
	 */
	private List joinConditions = new ArrayList();
	private String rightDataSetQualifiedName;
	private String leftDataSetQualifiedName;

	/**
	 * Constructor
	 */
	public JointDataSetDesign(String dataSetName) {
		super(dataSetName);
	}

	/**
	 * Constructor.
	 * 
	 * @throws DataException
	 */
	public JointDataSetDesign(String name, String left, String right, int joinType, List joinConditions)
			throws DataException {
		super(name);
		validateJoinType(joinType);

		this.right = right;
		this.left = left;
		this.joinType = joinType;

		this.joinConditions.addAll(joinConditions);

	}

	/**
	 * @param joinType
	 * @throws DataException
	 */
	private void validateJoinType(int joinType) throws DataException {
		if (!((joinType == IJointDataSetDesign.INNER_JOIN) || (joinType == IJointDataSetDesign.LEFT_OUTER_JOIN)
				|| (joinType == IJointDataSetDesign.RIGHT_OUTER_JOIN)
				|| (joinType == IJointDataSetDesign.FULL_OUTER_JOIN)))
			throw new DataException(ResourceConstants.INVALID_JOIN_TYPE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.IJointDataSetDesign#getLeftDataSetDesign()
	 */
	public String getLeftDataSetDesignName() {
		return left;
	}

	public void setLeftDataSetDesignName(String dataSetName) {
		left = dataSetName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.IJointDataSetDesign#getRightDataSetDesign()
	 */
	public String getRightDataSetDesignName() {
		return right;
	}

	public void setRightDataSetDesignName(String dataSetName) {
		right = dataSetName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IJointDataSetDesign#getJoinType()
	 */
	public int getJoinType() {
		return joinType;
	}

	/**
	 * 
	 * @param joinType
	 */
	public void setJoinType(int joinType) {
		this.joinType = joinType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IJointDataSetDesign#getJoinConditions()
	 */
	public List getJoinConditions() {
		return joinConditions;
	}

	/**
	 * add Join Condition
	 * 
	 * @param jc
	 */
	public void addJoinCondition(JoinCondition jc) {
		this.joinConditions.add(jc);
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IJointDataSetDesign#
	 * getLeftDataSetDesignQulifiedName()
	 */
	public String getLeftDataSetDesignQulifiedName() {
		if (this.leftDataSetQualifiedName == null)
			return this.left;
		return this.leftDataSetQualifiedName;
	}

	public void setLeftDataSetDesignQulifiedName(String leftDataSetQualifiedName) {
		this.leftDataSetQualifiedName = leftDataSetQualifiedName;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IJointDataSetDesign#
	 * getRightDataSetDesignQulifiedName()
	 */
	public String getRightDataSetDesignQulifiedName() {
		if (this.rightDataSetQualifiedName == null)
			return this.right;
		return this.rightDataSetQualifiedName;
	}

	public void setRightDataSetDesignQulifiedName(String rightDataSetQualifiedName) {
		this.rightDataSetQualifiedName = rightDataSetQualifiedName;
	}

}
