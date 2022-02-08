/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.excel.layout;

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
