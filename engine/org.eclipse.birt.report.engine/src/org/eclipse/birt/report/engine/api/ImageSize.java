/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

public class ImageSize {

	protected String unit;
	protected float width;
	protected float height;

	public ImageSize(String u, float w, float h) {
		unit = u;
		width = w;
		height = h;
	}

	public String getUnit() {
		return unit;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}
