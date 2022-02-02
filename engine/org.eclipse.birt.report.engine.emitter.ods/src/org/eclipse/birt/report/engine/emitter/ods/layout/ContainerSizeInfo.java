/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.emitter.ods.layout;

public class ContainerSizeInfo {
	// start point and the width in points
	private int start, width;

	public ContainerSizeInfo(int start, int width) {
		this.start = start;
		this.width = width;
	}

	public int getStartCoordinate() {
		return start;
	}

	public int getWidth() {
		return width;
	}

	public int getEndCoordinate() {
		return start + width;
	}
}
