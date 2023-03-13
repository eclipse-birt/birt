/***********************************************************************
 * Copyright (c) 2005 IBM Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.svg;

/**
 * Defines a set of constants that are used by the SVG device renderer.
 */
public interface ISVGConstants {

	/**
	 * A property name that identifies a list of javascript references. The
	 * references will be inlined in the generated svg output The list should be a
	 * java.util.List that contains string urls.
	 */
	String JAVASCRIPT_URL_REF_LIST = "javascript.ref.list"; //$NON-NLS-1$

	/**
	 * A property name that identifies a list of javascript code. The code will be
	 * inlined in the generated svg output. The list should be a java.util.List that
	 * contains string representing the code that will be inlined in the svg output.
	 */
	String JAVASCRIPT_CODE_LIST = "javascript.code.list"; //$NON-NLS-1$

	/**
	 * A property name that determines if the generated SVG should change its
	 * dimension to the containing element's width and height upon loading the SVG.
	 */
	String RESIZE_SVG = "resize.svg"; //$NON-NLS-1$

	/**
	 * A property name that determines if the generated SVG should contain embedded
	 * javascript code.
	 */
	String ENABLE_SCRIPT = "enable.scriptable"; //$NON-NLS-1$
}
