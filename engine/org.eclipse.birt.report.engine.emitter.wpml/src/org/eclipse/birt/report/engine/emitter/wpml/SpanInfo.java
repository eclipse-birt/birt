/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

import org.eclipse.birt.report.engine.content.IStyle;

public class SpanInfo {

	private int columnId = 0;

	private int columnSpan = 0;

	private int cellWidth = 0;

	private boolean start = false;

	private IStyle style = null;

	public SpanInfo(int columnId, int columnSpan, int cellWidth, boolean start, IStyle style) {
		this.columnId = columnId;
		this.columnSpan = columnSpan;
		this.cellWidth = cellWidth;
		this.start = start;
		this.style = style;
	}

	public int getColumnId() {
		return this.columnId;
	}

	public int getColumnSpan() {
		return this.columnSpan;
	}

	public int getCellWidth() {
		return this.cellWidth;
	}

	public boolean isStart() {
		return this.start;
	}

	public IStyle getStyle() {
		return this.style;
	}
}
