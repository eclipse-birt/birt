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
package org.eclipse.birt.report.engine.emitter.ods;

import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public class Data extends SheetData {

	public Data() {
	}

	public Data(SheetData data) {
		this(data.getValue(), data.getStyleId(), data.getDataType());
		this.rowIndex = data.getRowIndex();
	}

	public Data(final Object value, final int datatype) {
		this(value, null, datatype);
	}

	public Data(final Object value, final StyleEntry styleId, final int datatype) {
		this(value, styleId, datatype, 0);
	}

	public Data(final Object value, final StyleEntry styleId, final int datatype, int rowSpanOfDesign) {
		this.value = value;
		this.styleId = styleId;
		this.dataType = datatype;
		this.rowSpanInDesign = rowSpanOfDesign;
	}

	public Object getValue() {
		return value;
	}
}