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
package org.eclipse.birt.report.engine.emitter.ods;

public class BlankData extends Data {

	public enum Type {
		VERTICAL, HORIZONTAL, NONE
	}

	private SheetData data;

	private Type type;

	public BlankData(SheetData data) {
		super(data);
		this.data = data;
	}

	public BlankData() {
	}

	@Override
	public boolean isBlank() {
		return true;
	}

	public SheetData getData() {
		return data;
	}

	@Override
	public int getRowSpan() {
		if (data != null) {
			return data.getRowSpan();
		}
		return 0;
	}

	@Override
	public void setRowSpan(int rowSpan) {
		if (data != null) {
			data.setRowSpan(rowSpan);
		}
	}

	@Override
	public int getRowSpanInDesign() {
		if (data != null) {
			return data.getRowSpanInDesign();
		}
		return 0;
	}

	@Override
	public void decreasRowSpanInDesign() {
		if (data != null) {
			data.decreasRowSpanInDesign();
		}
	}

	@Override
	public float getHeight() {
		if (data != null) {
			return data.getHeight();
		}
		return super.getHeight();
	}

	@Override
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
