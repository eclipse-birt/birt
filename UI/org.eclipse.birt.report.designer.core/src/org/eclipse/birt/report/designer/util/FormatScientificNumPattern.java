/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.util;

/**
 * A pattern class serves for getting and setting pattern string for a
 * scientific number.
 */
public class FormatScientificNumPattern extends FormatNumberPattern {

	private int decPlaces = 0;

	/**
	 * Constructor.
	 * 
	 * @param category
	 */
	public FormatScientificNumPattern(String category) {
		super(category);
		setType('S');
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.NumGeneralPattern
	 * #getPattern()
	 */
	public String getPattern() {
		String numStr = "0E00"; //$NON-NLS-1$
		String decStr = ""; //$NON-NLS-1$

		String pattern = numStr;

		decStr = DEUtil.getDecmalStr(decPlaces);

		if (decStr != "") //$NON-NLS-1$
		{
			pattern = "0." + decStr + "E00"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		pattern = applyRoundingMode(pattern);

		/**
		 * when the pattern equals the default value, just returns the category name as
		 * the pattern value. DTE recognize it.
		 */
		if (pattern.equals(DEFAULT_SCIENTIFIC_PATTERN)) {
			return getCategory();
		}
		return pattern;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.NumGeneralPattern
	 * #setPattern(java.lang.String)
	 */
	public void setPattern(String patternStr) {
		String patt = valPattern(patternStr);

		if (patt == null || getCategory().equalsIgnoreCase(patt)) {
			patt = DEFAULT_SCIENTIFIC_PATTERN;
		}

		patt = checkRoundingMode(patt);

		if (patt.indexOf(".") != -1) //$NON-NLS-1$
		{
			this.decPlaces = patt.lastIndexOf("E") - patt.indexOf(".") - 1; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.FormatNumberPattern
	 * #getDefaultPatt()
	 */
	protected String getDefaultPatt() {
		return DEFAULT_SCIENTIFIC_PATTERN;
	}

	/**
	 * Get DecPlaces
	 * 
	 * @return Returns the decPlaces.
	 */
	public int getDecPlaces() {
		return decPlaces;
	}

	/**
	 * Set DecPlaces
	 * 
	 * @param decPlaces The decPlaces to set.
	 */
	public void setDecPlaces(int decPlaces) {
		this.decPlaces = decPlaces;
	}

	public String getRoundingMode() {
		return rounding;
	}

	public void setRoundingMode(String mode) {
		this.rounding = mode;
	}

}