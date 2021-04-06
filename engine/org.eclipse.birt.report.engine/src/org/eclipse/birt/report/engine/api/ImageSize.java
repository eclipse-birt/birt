/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
