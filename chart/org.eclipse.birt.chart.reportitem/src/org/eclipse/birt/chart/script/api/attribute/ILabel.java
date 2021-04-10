/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
