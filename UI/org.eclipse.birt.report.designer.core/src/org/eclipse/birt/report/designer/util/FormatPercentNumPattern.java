/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.util;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 * A pattern class serves for getting and setting pattern string for a percent
 * number.
 */

public class FormatPercentNumPattern extends FormatNumberPattern {

	private int decPlaces = 0;
	private boolean useSep = false;
	// private boolean useZero = false;
	private boolean useBracket = false;
	private String symPos = ""; //$NON-NLS-1$

	/**
	 * Returns the default percent symbol position for given locale
	 * 
	 * @param locale
	 * @return
	 */
	public static String getDefaultSymbolPosition(ULocale locale) {
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		NumberFormat formater = NumberFormat.getPercentInstance(locale);
		String result = formater.format(1);
		if (result.endsWith("%")) //$NON-NLS-1$
		{
			return FormatNumberPattern.SYMBOL_POSITION_AFTER;
		} else {
			return FormatNumberPattern.SYMBOL_POSITION_BEFORE;
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param category
	 */
	public FormatPercentNumPattern(String category) {
		super(category);
		setType('P');
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
		if (symPos.equalsIgnoreCase(FormatNumberPattern.SYMBOL_POSITION_BEFORE)) {
			positivePatt = "%" + positivePatt; //$NON-NLS-1$
			if (negativePatt != null) {
				negativePatt = "%" + negativePatt;//$NON-NLS-1$
			}
		} else if (symPos.equalsIgnoreCase(FormatNumberPattern.SYMBOL_POSITION_AFTER)) {
			positivePatt = positivePatt + "%"; //$NON-NLS-1$
			if (negativePatt != null) {
				negativePatt = negativePatt + "%";//$NON-NLS-1$
			}
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
		if (pattern.equals(DEFAULT_PERCENT_PATTERN)) {
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
		if (patt.indexOf("%") != -1) //$NON-NLS-1$
		{
			if (patt.indexOf("%") == 0) //$NON-NLS-1$
			{
				this.symPos = FormatNumberPattern.SYMBOL_POSITION_BEFORE;
			} else {
				this.symPos = FormatNumberPattern.SYMBOL_POSITION_AFTER;
			}
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
		return DEFAULT_PERCENT_PATTERN;
	}

	/**
	 * ��Get decPlaces
	 * 
	 * @return Returns the decPlaces.
	 */
	public int getDecPlaces() {
		return decPlaces;
	}

	/**
	 * Set decPlaces
	 * 
	 * @param decPlaces The decPlaces to set.
	 */
	public void setDecPlaces(int decPlaces) {
		this.decPlaces = decPlaces;
	}

	/**
	 * Returns useSep.
	 */
	public boolean getUseSep() {
		return this.useSep;
	}

	/**
	 * Set UseSep
	 * 
	 * @param useSep The useSep to set.
	 */
	public void setUseSep(boolean useSep) {
		this.useSep = useSep;
	}

	/**
	 * get UseBracket Returns useBracket.
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

	/**
	 * @return Returns the symPos.
	 */
	public String getSymPos() {
		return symPos;
	}

	/**
	 * Set SymPos
	 * 
	 * @param symPos The symPos to set.
	 */
	public void setSymPos(String symPos) {
		this.symPos = symPos;
	}

	public String getRoundingMode() {
		return rounding;
	}

	public void setRoundingMode(String mode) {
		this.rounding = mode;
	}

}
