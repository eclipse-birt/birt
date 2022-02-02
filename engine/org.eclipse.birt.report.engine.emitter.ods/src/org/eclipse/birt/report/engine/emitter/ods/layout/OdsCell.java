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

import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public class OdsCell extends OdsContainer {
	private int rowSpan;
	private boolean covered;

	public OdsCell(StyleEntry style, ContainerSizeInfo sizeInfo, OdsContainer parent, boolean covered) {
		super(style, sizeInfo, parent);
		this.rowSpan = 0;
		this.covered = covered;
	}

	public OdsCell(StyleEntry style, ContainerSizeInfo sizeInfo, OdsContainer parent, int rowSpan) {
		super(style, sizeInfo, parent);
		this.rowSpan = rowSpan;
		this.covered = false;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public boolean isSpanCell() {
		return covered;
	}
}
