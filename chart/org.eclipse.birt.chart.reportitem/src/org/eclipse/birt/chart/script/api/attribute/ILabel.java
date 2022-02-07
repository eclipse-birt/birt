/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.script.api.attribute;

/**
 * Represents Label object in a Chart in the scripting environment
 */

public interface ILabel {

	/**
	 * Checks if current label is visible
	 * 
	 * @return visible or not
	 */
	boolean isVisible();

	/**
	 * Sets if current label is visible
	 * 
	 * @param visible
	 */
	void setVisible(boolean visible);

	/**
	 * Gets the caption in the Label
	 * 
	 * @return the caption text
	 */
	IText getCaption();
}
