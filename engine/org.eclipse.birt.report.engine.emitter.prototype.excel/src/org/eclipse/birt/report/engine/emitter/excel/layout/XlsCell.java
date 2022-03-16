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

import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;

public class XlsCell extends XlsContainer {
	private int rowSpan;

	public XlsCell(StyleEntry style, ContainerSizeInfo sizeInfo, XlsContainer parent, int rowSpan) {
		super(style, sizeInfo, parent);
		this.rowSpan = rowSpan;
	}

	public int getRowSpan() {
		return rowSpan;
	}
}
