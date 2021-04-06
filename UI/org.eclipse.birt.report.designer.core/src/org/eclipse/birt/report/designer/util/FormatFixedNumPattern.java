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
 * A pattern class serves for getting and setting pattern string for a fixed
 * number.
 */

public class FormatFixedNumPattern extends FormatNumberPattern {

	private int decPlaces = 0;
	private boolean useSep = false;
	// private boolean useZero = false;
	private boolean useBracket = false;

	/**
	 * Constructor.
	 * 
	 * @param category
	 */
	public FormatFixedNumPattern(String category) {
		super(category);
		setType('F');
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.NumGeneralPattern
	 * #getPattern()
	 */
	public String getPattern() {
		String numStr = "###0"; //$NON-NLS-1$
		String decStr = ""; //$NON-NLS-1$

		String positivePatt = numStr;
		String negativePatt = null;
		String pattern;

		if (useSep) {
			positivePatt = "#,##0"; //$NON-NLS-1$
		}
		decStr = DEUtil.getDecmalStr(decPlaces);

		if (decStr != "") //$NON-NLS-1$
		{
			positivePatt = positivePatt + "." + decStr; //$NON-NLS-1$
		}
		// if ( useZero )
		// {
		// positivePatt = zeroIndicator + positivePatt;
		// }
		if (useBracket) {
			negativePatt = "(" + positivePatt + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (negativePatt != null) {
			pattern = positivePatt + ";" + negativePatt;//$NON-NLS-1$
		} else {
			pattern = positivePatt;
		}

		pattern = applyRoundingMode(pattern);

		/**
		 * when the pattern equals the default value, just returns the category name as
		 * the pattern value. DTE recognize it.
		 */
		if (pattern.equals(DEFAULT_FIXED_PATTERN)) {
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

		patt = checkRoundingMode(patt);

		this.useSep = patt.indexOf(",") != -1; //$NON-NLS-1$
		// this.useZero = patt.indexOf( zeroIndicator ) != -1;
		this.useBracket = patt.indexOf("(") != -1 //$NON-NLS-1$
				&& patt.indexOf(")") != -1; //$NON-NLS-1$
		if (patt.indexOf(".") != -1) //$NON-NLS-1$
		{
			this.decPlaces = patt.lastIndexOf("0") - patt.lastIndexOf("."); //$NON-NLS-1$ //$NON-NLS-2$
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
		return DEFAULT_FIXED_PATTERN;
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

	/**
	 * get UseSep Returns useSep.
	 */
	public boolean getUseSep() {
		return this.useSep;
	}

	/**
	 * Set useSep
	 * 
	 * @param useSep The useSep to set.
	 */
	public void setUseSep(boolean useSep) {
		this.useSep = useSep;
	}

	/**
	 * Returns useBracket.
	 */
	public boolean getUseBracket() {
		return this.useBracket;
	}

	/**
	 * Set useBracket
	 * 
	 * @param useBracket The useBracket to set.
	 */
	public void setUseBracket(boolean useBracket) {
		this.useBracket = useBracket;
	}

	public String getRoundingMode() {
		return rounding;
	}

	public void setRoundingMode(String mode) {
		this.rounding = mode;
	}

}