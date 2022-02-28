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

import org.eclipse.birt.report.engine.emitter.excel.layout.Page;

public class RowData {

	private SheetData[] rowdata;
	private double height;

	public RowData(Page page, SheetData[] rowdata, double height) {
		this.rowdata = rowdata;
		this.height = height;
	}

	public SheetData[] getRowdata() {
		return rowdata;
	}

	public void setRowdata(SheetData[] rowdata) {
		this.rowdata = rowdata;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
}
