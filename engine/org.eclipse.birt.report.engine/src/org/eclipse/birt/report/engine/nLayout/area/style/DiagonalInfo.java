/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.nLayout.area.style;

import java.awt.Color;

public class DiagonalInfo extends AreaConstants {
	protected Color diagonalColor;
	protected Color antidiagonalColor;
	protected int antidiagonalNumber;
	protected int diagonalNumber;
	protected int antidiagonalStyle;
	protected int diagonalStyle;
	protected int diagonalWidth;
	protected int antidiagonalWidth;

	public DiagonalInfo(int diagonalNumber, int diagonalStyle, int diagonalWidth, int antidiagonalNumber,
			int antidiagonalStyle, int antidiagonalWidth, Color diagonalColor, Color antidiagonalColor) {
		this.diagonalNumber = diagonalNumber;
		this.diagonalStyle = diagonalStyle;
		this.diagonalWidth = diagonalWidth;
		this.antidiagonalNumber = antidiagonalNumber;
		this.antidiagonalStyle = antidiagonalStyle;
		this.antidiagonalWidth = antidiagonalWidth;
		this.diagonalColor = diagonalColor;
		this.antidiagonalColor = antidiagonalColor;
	}

	public DiagonalInfo() {
	}

	public DiagonalInfo(DiagonalInfo diagonalInfo) {
		this.diagonalNumber = diagonalInfo.diagonalNumber;
		this.diagonalStyle = diagonalInfo.diagonalStyle;
		this.diagonalWidth = diagonalInfo.diagonalWidth;
		this.antidiagonalNumber = diagonalInfo.antidiagonalNumber;
		this.antidiagonalStyle = diagonalInfo.antidiagonalStyle;
		this.antidiagonalWidth = diagonalInfo.antidiagonalWidth;
	}

	public void setDiagonal(int diagonalNumber, String diagonalStyle, int diagonalWidth, Color color) {
		this.diagonalNumber = diagonalNumber;
		this.diagonalStyle = stringStyleMap.get(diagonalStyle);
		this.diagonalWidth = diagonalWidth;
		this.diagonalColor = color;
	}

	public void setAntiDiagonal(int antidiagonalNumber, String antidiagonalStyle, int antidiagonalWidth, Color color) {
		this.antidiagonalNumber = antidiagonalNumber;
		this.antidiagonalStyle = stringStyleMap.get(antidiagonalStyle);
		this.antidiagonalWidth = antidiagonalWidth;
		this.antidiagonalColor = color;
	}

	public Color getDiagonalColor() {
		return diagonalColor;
	}

	public Color getAntidiagonalColor() {
		return this.antidiagonalColor;
	}

	public int getAntidiagonalNumber() {
		return antidiagonalNumber;
	}

	public int getDiagonalNumber() {
		return diagonalNumber;
	}

	public int getAntidiagonalStyle() {
		return antidiagonalStyle;
	}

	public int getDiagonalStyle() {
		return diagonalStyle;
	}

	public int getDiagonalWidth() {
		return diagonalWidth;
	}

	public int getAntidiagonalWidth() {
		return antidiagonalWidth;
	}

}
