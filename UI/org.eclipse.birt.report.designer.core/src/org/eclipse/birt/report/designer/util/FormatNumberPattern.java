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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ULocale;

/**
 * A pattern class serves for getting and setting pattern string for a number.
 */

public class FormatNumberPattern {

	public static final String TEXT_CURRENCY_SYMBOL_NONE = Messages.getString("FormatNumberPage.currency.symbol.none"); //$NON-NLS-1$
	public static final String SYMBOL_POSITION_AFTER = Messages.getString("FormatNumberPage.symblePos.after"); //$NON-NLS-1$
	public static final String SYMBOL_POSITION_BEFORE = Messages.getString("FormatNumberPage.symblePos.before"); //$NON-NLS-1$

	public static final String[] ROUNDING_MODES_NAMES = { Messages.getString("FormatNumberPage.roundingMode.halfUp"), //$NON-NLS-1$
			Messages.getString("FormatNumberPage.roundingMode.halfDown"), //$NON-NLS-1$
			Messages.getString("FormatNumberPage.roundingMode.halfEven"), //$NON-NLS-1$
			Messages.getString("FormatNumberPage.roundingMode.up"), //$NON-NLS-1$
			Messages.getString("FormatNumberPage.roundingMode.down"), //$NON-NLS-1$
			Messages.getString("FormatNumberPage.roundingMode.ceiling"), //$NON-NLS-1$
			Messages.getString("FormatNumberPage.roundingMode.floor"), //$NON-NLS-1$
			Messages.getString("FormatNumberPage.roundingMode.unnecessary") //$NON-NLS-1$
	};

	public static final String[] ROUNDING_MODES_VALUES = { "HALF_UP", //$NON-NLS-1$
			"HALF_DOWN", //$NON-NLS-1$
			"HALF_EVEN", //$NON-NLS-1$
			"UP", //$NON-NLS-1$
			"DOWN", //$NON-NLS-1$
			"CEILING", //$NON-NLS-1$
			"FLOOR", //$NON-NLS-1$
			"UNNECESSARY" //$NON-NLS-1$
	};

	// private HashMap categoryPatternMaps;

	/**
	 * Category of number format pattern
	 */
	private String category = ""; //$NON-NLS-1$

	/**
	 * type
	 */
	private char type;

	protected String rounding = null;

	protected String zeroIndicator = "\'0\'"; //$NON-NLS-1$
	protected String defaultDecs = "0000000000"; //$NON-NLS-1$

	/**
	 * For currency, there is no default pattern, because DTE not support "currency"
	 * as a predefined pattern string, however, giving a default currency pattern
	 * here to provide convinence for setting pattern. Do not save the category name
	 * for this according defalut pattern because there is actually no default
	 * pattern for category "currency". Expect DTE to support this default pattern
	 * in future.
	 */
	protected String DEFAULT_CURRENCY_PATTERN = "#,##0.00"; //$NON-NLS-1$

	/** default pattern for fiexed number */
	protected String DEFAULT_FIXED_PATTERN = "###0.00"; //$NON-NLS-1$

	/** default pattern for percent number */
	protected String DEFAULT_PERCENT_PATTERN = "###0.00%"; //$NON-NLS-1$

	/** default pattern for scientific number */
	protected String DEFAULT_SCIENTIFIC_PATTERN = "0.00E00"; //$NON-NLS-1$

	/**
	 * Returns the default pattern for given category
	 *
	 * @param category
	 * @return
	 */
	public static String getPatternForCategory(String category, ULocale locale) {
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		String pattern = null;
		if (DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY.equals(category)) {
			pattern = "\u00A4###,##0.00"; //$NON-NLS-1$
			Currency currency = Currency.getInstance(locale);
			if (currency != null) {
				String symbol = currency.getSymbol();
				NumberFormat formater = NumberFormat.getCurrencyInstance(locale);
				String result = formater.format(1);
				if (result.endsWith(symbol)) {
					pattern = "###,##0.00";//$NON-NLS-1$

					result = result.substring(0, result.indexOf(symbol));

					for (int i = result.length() - 1; i >= 0; i--) {
						if (result.charAt(i) == ' ') {
							pattern += " "; //$NON-NLS-1$
							continue;
						}

						break;
					}

					pattern += "\u00A4"; //$NON-NLS-1$
				} else {
					pattern = "\u00A4"; //$NON-NLS-1$

					result = result.substring(result.indexOf(symbol) + symbol.length());

					for (int i = 0; i < result.length(); i++) {
						if (result.charAt(i) == ' ') {
							pattern += " "; //$NON-NLS-1$
							continue;
						}

						break;
					}

					pattern += "###,##0.00"; //$NON-NLS-1$
				}
			}
		} else if (DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED.equals(category)) {
			pattern = "#0.00"; //$NON-NLS-1$
		} else if (DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT.equals(category)) {
			pattern = "0.00%"; //$NON-NLS-1$
			NumberFormat formater = NumberFormat.getPercentInstance(locale);
			String result = formater.format(1);
			if (result.endsWith("%")) //$NON-NLS-1$
			{
				pattern = "0.00"; //$NON-NLS-1$

				result = result.substring(0, result.indexOf('%'));

				for (int i = result.length() - 1; i >= 0; i--) {
					if (result.charAt(i) == ' ') {
						pattern += " "; //$NON-NLS-1$
						continue;
					}

					break;
				}

				pattern += "%"; //$NON-NLS-1$
			} else {
				pattern = "%"; //$NON-NLS-1$

				result = result.substring(result.indexOf('%') + 1);

				for (int i = 0; i < result.length(); i++) {
					if (result.charAt(i) == ' ') {
						pattern += " "; //$NON-NLS-1$
						continue;
					}

					break;
				}

				pattern += "0.00"; //$NON-NLS-1$
			}
		} else if (DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC.equals(category)) {
			pattern = "0.00E00"; //$NON-NLS-1$
		} else {
			pattern = ""; //$NON-NLS-1$
		}
		return pattern;
	}

	/**
	 * @return Returns the index for the matching rounding mode value, if not found,
	 *         returns the default index as 0.
	 */
	public static int getRoundingModeIndexByValue(String val) {
		int i = 0;
		for (String rm : ROUNDING_MODES_VALUES) {
			if (rm.equalsIgnoreCase(val)) {
				return i;
			}
			i++;
		}
		return 0;
	}

	public static String getRoundingModeByName(String name) {
		int i = 0;
		for (String rm : ROUNDING_MODES_NAMES) {
			if (rm.equals(name)) {
				return ROUNDING_MODES_VALUES[i];
			}
			i++;
		}
		return null;
	}

	/**
	 * Constructor.
	 *
	 * @param category Name for the pattern.
	 */
	public FormatNumberPattern(String category) {
		this.category = category;
		setType('G');
		// createCategoryPatterns( );
	}

	/**
	 * Constructor.
	 */
	public FormatNumberPattern() {
		this.category = DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER;
		setType('G');
		// createCategoryPatterns( );
	}

	/**
	 * Get category name
	 *
	 * @return Returns the name.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Get type
	 *
	 * @return Returns the type.
	 */
	protected char getType() {
		return type;
	}

	/**
	 * Set type
	 *
	 * @param t The type to set
	 */
	protected void setType(char t) {
		this.type = t;
	}

	/**
	 * Gets pattern string for the pattern class.
	 */
	public String getPattern() {
		return this.category;
	}

	/**
	 * Sets the pattern string.
	 *
	 * @param patternStr
	 */
	public void setPattern(String patternStr) {
		this.category = valPattern(patternStr);
	}

	/**
	 * Validates the pattern string for predefined format category excludeing
	 * "custom".
	 *
	 * @param patternStr The pattern string to be validated.
	 * @return The validated pattern.
	 */
	protected String valPattern(String patternStr) {
		String patt;
		if (patternStr == null || getCategory().equals(patternStr)) {
			/**
			 * when the input patternStr is null or equals the category, use its
			 * corresponding defalut pattern value.
			 */
			patt = getDefaultPatt();
		} else {
			patt = patternStr;
		}
		return patt;
	}

	/**
	 * Gets default pattern for predefined format category. Should be overrided in
	 * sub classes, including :{ FormatCurrencyNumPattern, FormatFixedNumPattern,
	 * FormatPercentNumPattern, FormatScientificNumPattern.}
	 */
	protected String getDefaultPatt() {
		return this.category;
	}

	protected String checkRoundingMode(String pattern) {
		// try detect rounding mode
		int ridx = pattern.indexOf("{RoundingMode="); //$NON-NLS-1$
		if (ridx != -1) {
			int rnidx = pattern.indexOf('}', ridx);

			int offset = "{RoundingMode=".length(); //$NON-NLS-1$

			if (rnidx != -1 && rnidx > ridx + offset) {
				rounding = pattern.substring(ridx + offset, rnidx).trim();

				if (rnidx < pattern.length() - 1) {
					pattern = pattern.substring(0, ridx) + pattern.substring(rnidx + 1);
				} else {
					pattern = pattern.substring(0, ridx);
				}
			}
		}

		return pattern;
	}

	protected String applyRoundingMode(String pattern) {
		if (rounding != null) {
			pattern += "{RoundingMode=" + rounding + "}"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		return pattern;
	}
}
