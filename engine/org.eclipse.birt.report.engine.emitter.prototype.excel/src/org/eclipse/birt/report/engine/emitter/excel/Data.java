/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.excel;

public class Data extends SheetData {

	public Data() {
	}

	public Data(SheetData data) {
		this(data.getValue(), data.getStyleId(), data.getDataType());
		this.rowIndex = data.getRowIndex();
	}

	public Data(final Object value, final int datatype) {
		this(value, 0, datatype);
	}

	public Data(final Object value, final int styleId, final int datatype) {
		this(value, styleId, datatype, 0);
	}

	public Data(final Object value, final int styleId, final int datatype, int rowSpanOfDesign) {
		this.value = value;
		this.styleId = styleId;
		this.dataType = datatype;
		this.rowSpanInDesign = rowSpanOfDesign;
	}

	@Override
	public Object getValue() {
		return value;
	}
}
