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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for Level element to store the constants on it.
 */

public interface ILevelModel {

	/**
	 * Name of the property that list of attributes defined for this level element.
	 * Each attribute inside is a string which refer to a dataset column name.
	 */

	String ATTRIBUTES_PROP = "attributes"; //$NON-NLS-1$

	/**
	 * Name of the property that is used to create static group based on the rules
	 * define in this property.
	 */
	String STATIC_VALUES_PROP = "staticValues"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the interval base of search.
	 */
	String INTERVAL_BASE_PROP = "intervalBase"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the interval range of search.
	 */
	String INTERVAL_RANGE_PROP = "intervalRange"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the interval of search.
	 */
	String INTERVAL_PROP = "interval"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates whether this level is static, dynamic or
	 * mirrored.
	 */
	String LEVEL_TYPE_PROP = "levelType"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the data type of this level.
	 */
	String DATA_TYPE_PROP = "dataType"; //$NON-NLS-1$

	/**
	 * Name of the property that specifies a list of privilige of users.
	 */

	String VALUE_ACCESS_CONTROLS_PROP = "valueAccessControls"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the type of a date-time level, such as
	 * year, quater, month, week, day.
	 */
	String DATE_TIME_LEVEL_TYPE = "dateTimeLevelType"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the format for the date-time level.
	 */
	String DATE_TIME_FORMAT_PROP = "dateTimeFormat"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the default value.
	 */
	String DEFAULT_VALUE_PROP = "defaultValue"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the expression to calculate ACL for the
	 * level. This expression is evaluated once during the generation of the cube.
	 */
	String ACL_EXPRESSION_PROP = "ACLExpression"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the expression to calculate ACL for each
	 * member of the level. This expression is evaluated once for each level member
	 * during the generation of the cube, and has access to the full content of the
	 * level member (e.g., its value, attributes, etc.)
	 */
	String MEMBER_ACL_EXPRESSION_PROP = "memberACLExpression"; //$NON-NLS-1$

	/**
	 * Name of the action property, which defines what action can be performed when
	 * clicking the level.
	 */

	String ACTION_PROP = "action"; //$NON-NLS-1$

	/**
	 * Name of the format property, which saves the format of the level.
	 */
	String FORMAT_PROP = "format"; //$NON-NLS-1$

	/**
	 * Name of the alignment property.
	 */
	String ALIGNMENT_PROP = "alignment"; //$NON-NLS-1$
}
