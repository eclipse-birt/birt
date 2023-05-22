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

/**
 * Representation of diagonal and antidiagonal information
 *
 * @since 3.3
 *
 */
public class DiagonalInfo extends AreaConstants {
	protected Color diagonalColor;
	protected Color antidiagonalColor;
	protected int antidiagonalNumber;
	protected int diagonalNumber;
	protected int antidiagonalStyle;
	protected int diagonalStyle;
	protected int diagonalWidth;
	protected int antidiagonalWidth;

	/**
	 * Constructor 01 - default constructor
	 */
	public DiagonalInfo() {
	}

	/**
	 * Constructro 02 - Constructor with detailed information properties
	 *
	 * @param diagonalNumber     count of the diagonal lines
	 * @param diagonalStyle      style of the diagonal line
	 * @param diagonalWidth      width of the diagonal line
	 * @param antidiagonalNumber count of the antidiagonal lines
	 * @param antidiagonalStyle  style of the antidiagonal line
	 * @param antidiagonalWidth  width of the antidiagonal line
	 * @param diagonalColor      color of the diagonal line
	 * @param antidiagonalColor  color of the antidiagonal line
	 */
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


	/**
	 * Constructor 03 - Constructor with diagonal info object
	 *
	 * @param diagonalInfo diagonal info object for initialization
	 */
	public DiagonalInfo(DiagonalInfo diagonalInfo) {
		this.diagonalNumber = diagonalInfo.diagonalNumber;
		this.diagonalStyle = diagonalInfo.diagonalStyle;
		this.diagonalWidth = diagonalInfo.diagonalWidth;
		this.antidiagonalNumber = diagonalInfo.antidiagonalNumber;
		this.antidiagonalStyle = diagonalInfo.antidiagonalStyle;
		this.antidiagonalWidth = diagonalInfo.antidiagonalWidth;
	}

	/**
	 * Set the diagonal line information
	 *
	 * @param diagonalNumber count of the diagonal lines
	 * @param diagonalStyle  style of the diagonal line
	 * @param diagonalWidth  width of the diagonal line
	 * @param diagonalColor  color of the diagonal line
	 */
	public void setDiagonal(int diagonalNumber, String diagonalStyle, int diagonalWidth, Color diagonalColor) {
		this.diagonalNumber = diagonalNumber;
		this.diagonalStyle = stringStyleMap.get(diagonalStyle);
		this.diagonalWidth = diagonalWidth;
		this.diagonalColor = diagonalColor;
	}

	/**
	 * Set the diagonal line information
	 *
	 * @param antidiagonalNumber count of the antidiagonal lines
	 * @param antidiagonalStyle  style of the antidiagonal line
	 * @param antidiagonalWidth  width of the antidiagonal line
	 * @param antidiagonalColor  color of the antidiagonal line
	 */
	public void setAntiDiagonal(int antidiagonalNumber, String antidiagonalStyle, int antidiagonalWidth,
			Color antidiagonalColor) {
		this.antidiagonalNumber = antidiagonalNumber;
		this.antidiagonalStyle = stringStyleMap.get(antidiagonalStyle);
		this.antidiagonalWidth = antidiagonalWidth;
		this.antidiagonalColor = antidiagonalColor;
	}

	/**
	 * Get the color of the diagonal line
	 *
	 * @return Return the color of the diagonal line
	 */
	public Color getDiagonalColor() {
		return diagonalColor;
	}

	/**
	 * Get the color of the antidiagonal line
	 *
	 * @return Return the color of the antidiagonal line
	 */
	public Color getAntidiagonalColor() {
		return this.antidiagonalColor;
	}

	/**
	 * Get the count of the diagonal line
	 *
	 * @return Return the count of the diagonal line
	 */
	public int getDiagonalNumber() {
		return diagonalNumber;
	}

	/**
	 * Get the count of the antidiagonal line
	 *
	 * @return Return the count of the antidiagonal line
	 */
	public int getAntidiagonalNumber() {
		return antidiagonalNumber;
	}

	/**
	 * Get the style of the diagonal line
	 *
	 * @return Return the style of the diagonal line
	 */
	public int getDiagonalStyle() {
		return diagonalStyle;
	}

	/**
	 * Get the style of the antidiagonal line
	 *
	 * @return Return the style of the antidiagonal line
	 */
	public int getAntidiagonalStyle() {
		return antidiagonalStyle;
	}

	/**
	 * Get the width of the diagonal line
	 *
	 * @return Return the width of the diagonal line
	 */
	public int getDiagonalWidth() {
		return diagonalWidth;
	}

	/**
	 * Get the width of the antidiagonal line
	 *
	 * @return Return the width of the antidiagonal line
	 */
	public int getAntidiagonalWidth() {
		return antidiagonalWidth;
	}
}
