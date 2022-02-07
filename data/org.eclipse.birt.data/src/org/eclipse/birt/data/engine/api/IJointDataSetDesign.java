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

package org.eclipse.birt.data.engine.api;

import java.util.List;

/**
 * This interface describes the static design of a Joint Data Set.
 */
public interface IJointDataSetDesign extends IBaseDataSetDesign {

	/**
	 * The integer value stands for an inner join operator.
	 */
	public static int INNER_JOIN = 0;

	/**
	 * The integer value stands for a left outer join operator.
	 */
	public static int LEFT_OUTER_JOIN = 1;

	/**
	 * The integer value stands for a right outer join operator.
	 */
	public static int RIGHT_OUTER_JOIN = 2;

	/**
	 * The integer value stands for a full outer join operator.
	 */
	public static int FULL_OUTER_JOIN = 3;

	/**
	 * This method returns the name of data set which servers as left operand of a
	 * joint.
	 * 
	 * @return name of data set that servers as first oprand
	 */
	public String getLeftDataSetDesignName();

	/**
	 * This method returns the name of data set which servers as right operand of a
	 * joint.
	 * 
	 * @return name of data set that servers as second oprand
	 */
	public String getRightDataSetDesignName();

	/**
	 * This method returns the name of data set which servers as left operand of a
	 * joint.
	 * 
	 * @return name of data set that servers as first oprand
	 */
	public String getLeftDataSetDesignQulifiedName();

	/**
	 * This method returns the name of data set which servers as right operand of a
	 * joint.
	 * 
	 * @return name of data set that servers as second oprand
	 */
	public String getRightDataSetDesignQulifiedName();

	/**
	 * This method returns the Joint Type.
	 * 
	 * @return the integer stands for a joint type.
	 */
	public int getJoinType();

	/**
	 * This method returns the Joint conditions. Only rows which can make these
	 * IJointConditionalExpression instance evaluate to true will be jointed.
	 * 
	 * @return the list contains joint conditions. All the elements in the list is
	 *         IJointConditionalExpression.
	 */
	public List getJoinConditions();

}
