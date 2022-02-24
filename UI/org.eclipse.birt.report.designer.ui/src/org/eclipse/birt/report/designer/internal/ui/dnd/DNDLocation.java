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
