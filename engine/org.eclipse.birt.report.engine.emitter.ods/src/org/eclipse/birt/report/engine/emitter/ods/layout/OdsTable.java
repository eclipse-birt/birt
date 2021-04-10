/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.ods.layout;

import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public class OdsTable extends OdsContainer {
	private int[] columnWidths;

	public OdsTable(StyleEntry entry, ContainerSizeInfo sizeInfo, OdsContainer parent) {
		super(entry, sizeInfo, parent);
	}

	public OdsTable(ColumnsInfo table, StyleEntry entry, ContainerSizeInfo sizeInfo, OdsContainer parent) {
		this(entry, sizeInfo, parent);
		if (table != null) {
			columnWidths = table.getColumns();
		}
	}

	public OdsTable(ColumnsInfo table, OdsContainer container) {
		this(table, container.getStyle(), container.getSizeInfo(), container);
	}

	public ContainerSizeInfo getColumnSizeInfo(int column, int span) {
		int startCoordinate = getSizeInfo().getStartCoordinate();

		for (int i = 0; i < column; i++) {
			startCoordinate += columnWidths[i];
		}

		int endCoordinate = 0;

		for (int i = column; i < column + span; i++) {
			endCoordinate += columnWidths[i];
		}

		return new ContainerSizeInfo(startCoordinate, endCoordinate);
	}
}
