/*************************************************************************************
 * Copyright (c) 2006 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
