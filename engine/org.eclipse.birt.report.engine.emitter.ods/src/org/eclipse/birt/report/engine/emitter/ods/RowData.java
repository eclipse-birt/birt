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

public class RowData {

	private SheetData[] rowdata;
	private double height;

	public RowData(SheetData[] rowdata, double height) {
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
