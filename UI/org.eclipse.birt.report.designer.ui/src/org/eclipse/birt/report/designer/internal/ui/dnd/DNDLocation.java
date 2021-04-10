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

package org.eclipse.birt.report.designer.internal.ui.dnd;

import org.eclipse.draw2d.geometry.Point;

/**
 * A class wrapper for different loction type.
 */

public class DNDLocation {
	private Point point;
	private int location;

	public DNDLocation(Point point) {
		this.point = point;
	}

	public DNDLocation(int location) {
		this.location = location;
	}

	public Point getPoint() {
		return point;
	}

	public int getLocation() {
		return location;
	}
}
