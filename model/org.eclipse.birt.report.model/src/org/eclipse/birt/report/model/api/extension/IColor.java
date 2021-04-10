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

package org.eclipse.birt.report.model.api.extension;

public interface IColor {

	int getRed();

	int getGreen();

	int getBlue();

	/**
	 * Returns the transpareny depth.
	 * 
	 * @return the integer between 0 and 255.
	 */

	int getTransparency();

	void setRed(int red);

	void setGreen(int green);

	void setBlue(int blud);

	/**
	 * Sets the transpareny depth.
	 * 
	 * @param value the integer between 0 and 255.
	 */

	void setTransparency(int value);
}
