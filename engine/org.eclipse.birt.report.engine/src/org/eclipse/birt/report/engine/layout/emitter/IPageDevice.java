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
