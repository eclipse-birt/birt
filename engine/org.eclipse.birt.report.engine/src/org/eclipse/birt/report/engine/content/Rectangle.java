/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.content;

public class Rectangle {

	Position point;
	Dimension dimension;

	public Rectangle() {
		point = new Position();
		dimension = new Dimension();
	}

	public Rectangle(Position p, Dimension d) {
		point = p;
		dimension = d;
	}

	public Rectangle(int x, int y, int width, int height) {
		point = new Position(x, y);
		dimension = new Dimension(width, height);
	}

	public void setLocation(int x, int y) {
		point.setLocation(x, y);
	}

	public void setDimension(int w, int h) {
		dimension.setDimension(w, h);
	}

	public boolean isSet() {
		return point.isSet() && dimension.isSet();
	}

	public Position getLocation() {
		return this.point;
	}

	public Dimension getDimension() {
		return this.dimension;
	}

	public int getX() {
		return point.getX();
	}

	public int getY() {
		return point.getY();
	}

	public int getWidth() {
		return dimension.getWidth();
	}

	public int getHeight() {
		return dimension.getHeight();
	}

}
