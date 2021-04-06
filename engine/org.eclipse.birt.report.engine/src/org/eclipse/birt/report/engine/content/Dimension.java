/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.content;

public class Dimension {

	private int width = -1;

	private int height = -1;

	public Dimension() {

	}

	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setDimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public boolean isSet() {
		return (width != -1) && (height != -1);
	}

	public void unSet() {
		width = -1;
		height = -1;
	}

	public static Dimension scale(Dimension origin, double ratio) {
		return new Dimension((int) (origin.getWidth() * ratio), (int) (origin.getHeight() * ratio));
	}

	public double getRatio() {
		return ((double) width) / ((double) height);
	}
}
