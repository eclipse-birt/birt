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
 * Defines constants for MemberValue.
 */
public interface IMemberValueModel {

	/**
	 * Name of the property that gives the detail value of this member value.
	 */
	String VALUE_PROP = "value"; //$NON-NLS-1$

	/**
	 * Name of the property that gives name of the referred cube level element.
	 */
	String LEVEL_PROP = "level"; //$NON-NLS-1$

	/**
	 * Name of the property that defines a list CrosstabMemberValue.
	 */
	String MEMBER_VALUES_PROP = "memberValues"; //$NON-NLS-1$

	/**
	 * Name of the property that defines a list of FilterCondition.
	 */
	String FILTER_PROP = "filter"; //$NON-NLS-1$
}
