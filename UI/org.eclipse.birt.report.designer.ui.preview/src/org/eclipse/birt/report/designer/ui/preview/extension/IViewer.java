/*************************************************************************************
 * Copyright (c) 2006 Actuate Corporation and others.
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

package org.eclipse.birt.report.designer.ui.preview.extension;

/**
 * The interface for all report viewers.
 */
public interface IViewer {

	/**
	 * Set target report design file.
	 * 
	 * @param reportDesignFile
	 */
	public void setInput(Object input);

	/**
	 * Start render report.
	 */
	public void render();

	/**
	 * Disposes of this viewer.
	 */
	public void close();

}
