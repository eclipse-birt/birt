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

public class BlankData extends Data {

	public static enum Type {
		VERTICAL, HORIZONTAL, NONE
	};

	private SheetData data;

	private Type type;

	public BlankData(SheetData data) {
		super(data);
		this.data = data;
	}

	public BlankData() {
	}

	public boolean isBlank() {
		return true;
	}

	public SheetData getData() {
		return data;
	}

	public int getRowSpan() {
		if (data != null) {
			return data.getRowSpan();
		}
		return 0;
	}

	public void setRowSpan(int rowSpan) {
		if (data != null) {
			data.setRowSpan(rowSpan);
		}
	}

	public int getRowSpanInDesign() {
		if (data != null) {
			return data.getRowSpanInDesign();
		}
		return 0;
	}

	public void decreasRowSpanInDesign() {
		if (data != null) {
			data.decreasRowSpanInDesign();
		}
	}

	public float getHeight() {
		if (data != null) {
			return data.getHeight();
		}
		return super.getHeight();
	}

	public void setHeight(float height) {
		if (data != null) {
			data.setHeight(height);
		}
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
