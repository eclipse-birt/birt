/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.odf;

import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public class SpanInfo {

	private int columnId = 0;

	private int columnSpan = 1;

	private int rowSpan = 1;

	private boolean start = false;

	private StyleEntry style = null;

	public SpanInfo(int columnId, int columnSpan, int rowSpan, boolean start, StyleEntry style) {
		this.columnId = columnId;
		this.columnSpan = columnSpan;
		this.rowSpan = rowSpan;
		this.start = start;
		this.style = style;
	}

	public int getColumnId() {
		return this.columnId;
	}

	public int getColumnSpan() {
		return this.columnSpan;
	}

	public int getRowSpan() {
		return this.rowSpan;
	}

	public boolean isStart() {
		return this.start;
	}

	public StyleEntry getStyle() {
		return this.style;
	}
}