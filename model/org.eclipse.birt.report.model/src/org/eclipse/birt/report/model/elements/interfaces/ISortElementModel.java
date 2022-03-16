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
 * Defines constants for SortElement.
 */
public interface ISortElementModel {

	/**
	 * Name of the "key" member. An expression that gives the sort key on which to
	 * sort.
	 */

	String KEY_PROP = "key"; //$NON-NLS-1$

	/**
	 * Name of the "direction" member.
	 */

	String DIRECTION_PROP = "direction"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the member value of this sort.
	 */

	String MEMBER_PROP = "member"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the strength of the sort collation.
	 */

	String STRENGTH_PROP = "strength"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the locale of the sort collation.
	 */

	String LOCALE_PROP = "locale"; //$NON-NLS-1$
}
