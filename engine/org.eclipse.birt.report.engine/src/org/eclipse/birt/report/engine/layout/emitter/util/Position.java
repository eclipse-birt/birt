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

package org.eclipse.birt.report.engine.layout.emitter.util;

public class Position implements Comparable<Position> {
	float x, y;

	public Position(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof Position)) {
			return false;
		}
		Position dest = (Position) object;
		return x == dest.x && y == dest.y;
	}

	@Override
	public int hashCode() {
		int code = 13;
		code += x;
		code *= 31;
		code += y;
		return code;
	}

	@Override
	public String toString() {
		return "( " + x + ", " + y + " )";
	}

	@Override
	public int compareTo(Position other) {
		if (other == null) {
			return 0;
		}
		int deltaX = getCompareResult(x - other.x);
		if (deltaX != 0) {
			return deltaX;
		}
		return getCompareResult(y - other.y);
	}

	private int getCompareResult(float delta) {
		if (delta > 0) {
			return 1;
		}
		if (delta < 0) {
			return -1;
		}
		return 0;
	}

}
