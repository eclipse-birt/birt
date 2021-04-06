/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.emitter;

import java.awt.Color;

public interface IPageDevice {

	/**
	 * Creates a new page with specified width and height.
	 * 
	 * @param width           page width.
	 * @param height          page height.
	 * @param backgroundColor background color.
	 * @return the new page.
	 */
	IPage newPage(int width, int height, Color backgroundColor);

	/**
	 * Closes this page device.
	 * 
	 * @throws Exception
	 */
	void close() throws Exception;
}