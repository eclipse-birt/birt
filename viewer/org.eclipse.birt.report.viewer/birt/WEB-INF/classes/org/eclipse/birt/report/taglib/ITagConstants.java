/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.taglib;

/**
 * Defined constants used in BIRT tags
 *
 */
public interface ITagConstants {
	/**
	 * Blank String Var
	 */
	String BLANK_STRING = ""; //$NON-NLS-1$

	/**
	 * Scrolling style
	 */
	String SCROLLING_YES = "yes"; //$NON-NLS-1$
	String SCROLLING_AUTO = "auto"; //$NON-NLS-1$

	/**
	 * Attribute
	 */
	String ATTR_HOSTPAGE = "hasHostPage"; //$NON-NLS-1$

	/**
	 * Report Container
	 */
	String CONTAINER_IFRAME = "iframe"; //$NON-NLS-1$
	String CONTAINER_DIV = "div"; //$NON-NLS-1$
}
