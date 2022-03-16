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
