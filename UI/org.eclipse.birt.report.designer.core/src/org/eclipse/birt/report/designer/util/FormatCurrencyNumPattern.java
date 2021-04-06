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

import java.util.ArrayList;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.util.StringUtil;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ULocale;

/**
 * A pattern class serves for getting and setting pattern string for a currency.
 */
public class FormatCurrencyNumPattern extends FormatNumberPattern {

	private int decPlaces = 0;
	private boolean useSep = false;
	private boolean useSpace = false;
	private boolean useBracket = false;
	private String symbol = ""; //$NON-NLS-1$
	private String symPos = ""; //$NON-NLS-1$

	public static String[] BUILT_IN_SYMBOLS;

	static {
		java.util.List<String> list = new ArrayList<String>();
		list.add(Messages.getString("FormatNumberPage.currency.symbol.none")); //$NON-NLS-1$
		list.add("\u00A5"); //$NON-NLS-1$
		list.add("$"); //$NON-NLS-1$
		list.add("\u20ac"); //$NON-NLS-1$
		list.add("\u00A3"); //$NON-NLS-1$
		// list.add( "\u20A9" ); //$NON-NLS-1$
		list.add("DKK"); //$NON-NLS-1$

		String defaultSymbol = null;
		Currency defaultCurrency = Currency.getInstance(ULocale.getDefault());
		if (defaultCurrency != null) {
			// the default currency could be null if country is not set properly
			// in locale settings.
			defaultSymbol = defaultCurrency.getSymbol();
		}

		/*
		 * bug 233779, the localSymbol doesn't allow to be null in IBM JDK List but in
		 * Sun JDK, it's option and ArrayList allows to be null.
		 */
		if (defaultSymbol != null && !list.contains(defaultSymbol)) {
			list.add(1, defaultSymbol);
		}
		BUILT_IN_SYMBOLS = list.toArray(new String[list.size()]);
	}

	/**
	 * Returns the default currency symbol position for given locale. Returns
	 * <code>null</code> if no symbol needed by default.
	 * 
	 * @param locale
	 * @return
	 */
	public static String getDefaultSymbolPosition(ULocale locale) {
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		Currency currency = Currency.getInstance(locale);
		if (currency != null) {
			String symbol = currency.getSymbol();
			if (symbol == null) {
				return null;
			}
			NumberFormat formater = NumberFormat.getCurrencyInstance(locale);
			String result = formater.format(1);
			if (result.endsWith(symbol)) {
				return FormatNumberPattern.SYMBOL_POSITION_AFTER;
			} else {
				return FormatNumberPattern.SYMBOL_POSITION_BEFORE;
			}
		}
		return null;
	}

	/**
	 * Returns the default number of fraction digits that should be displayed for
	 * the default currency for given locale.
	 * 
	 * @param locale
	 * @return
	 */
	public static int getDefaultFractionDigits(ULocale locale) {
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		Currency currency = Currency.getInstance(locale);
		if (currency != null) {
			return currency.getDefaultFractionDigits();
		}

		return 2;
	}

	/**
	 * Returns the if symbol space is used by default for given locale.
	 * 
	 * @param locale
	 * @return
	 */
	public static boolean getDefaultUsingSymbolSpace(ULocale locale) {
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		Currency currency = Currency.getInstance(locale);
		if (currency != null) {
			String symbol = currency.getSymbol();
			if (symbol == null) {
				return false;
			}
			NumberFormat formater = NumberFormat.getCurrencyInstance(locale);
			String result = formater.format(1);
			if (result.endsWith(symbol)) {
				result = result.substring(0, result.indexOf(symbol));

				for (int i = result.length() - 1; i >= 0; i--) {
					if (UCharacter.isSpaceChar(result.codePointAt(i))) {
						return true;
					}
				}
			} else {
				result = result.substring(result.indexOf(symbol) + symbol.length());

				for (int i = 0; i < result.length(); i++) {
					if (UCharacter.isSpaceChar(result.codePointAt(i))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Constructor.
	 * 
	 * @param category Category name for currency number format pattern.
	 */
	public FormatCurrencyNumPattern(String category) {
		super(category);
		setType('C');
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

		if (!StringUtil.isEmpty(decStr)) {
			positivePatt = positivePatt + "." + decStr; //$NON-NLS-1$
		}
		if (useBracket) {
			negativePatt = "(" + positivePatt + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (symbol.length() != 0 && !symbol.equalsIgnoreCase(FormatNumberPattern.TEXT_CURRENCY_SYMBOL_NONE)) {
			if (symPos.equalsIgnoreCase(FormatNumberPattern.SYMBOL_POSITION_BEFORE)) {
				positivePatt = symbol + (getUseSpace() ? " " : "") //$NON-NLS-1$ //$NON-NLS-2$
						+ positivePatt;
				if (negativePatt != null) {
					negativePatt = symbol + (getUseSpace() ? " " : "") //$NON-NLS-1$ //$NON-NLS-2$
							+ negativePatt;
				}
			} else if (symPos.equalsIgnoreCase(FormatNumberPattern.SYMBOL_POSITION_AFTER)) {
				positivePatt = positivePatt + (getUseSpace() ? " " : "") //$NON-NLS-1$ //$NON-NLS-2$
						+ symbol;
				if (negativePatt != null) {
					negativePatt = negativePatt + (getUseSpace() ? " " : "") //$NON-NLS-1$ //$NON-NLS-2$
							+ symbol;
				}
			}
		}
		if (negativePatt != null) {
			pattern = positivePatt + ";" + negativePatt;//$NON-NLS-1$
		} else {
			pattern = positivePatt;
		}

		pattern = applyRoundingMode(pattern);

		/**
		 * For currency, there is no default pattern, because DTE not support "currency"
		 * as a predefined pattern string.
		 */
		// if ( pattern.equals( DEFAULT_CURRENCY_PATTERN ) )
		// {
		// return getCategory( );
		// }
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
		this.useSpace = patt.indexOf(" ") != -1; //$NON-NLS-1$
		this.useBracket = patt.indexOf("(") != -1 //$NON-NLS-1$
				&& patt.indexOf(")") != -1; //$NON-NLS-1$
		this.decPlaces = 0;
		if (patt.indexOf(".") != -1) //$NON-NLS-1$
		{
			this.decPlaces = patt.lastIndexOf("0") - patt.lastIndexOf("."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		this.symbol = ""; //$NON-NLS-1$
		this.symPos = ""; //$NON-NLS-1$
		for (int i = 0; i < BUILT_IN_SYMBOLS.length; i++) {
			String sTemp = BUILT_IN_SYMBOLS[i];
			int sPos = patt.indexOf(sTemp);
			if (sPos != -1) {
				this.symbol = sTemp;
				if (sPos == 0) {
					this.symPos = FormatNumberPattern.SYMBOL_POSITION_BEFORE;
				} else {
					this.symPos = FormatNumberPattern.SYMBOL_POSITION_AFTER;
				}
				break;
			}
		}

		if (this.symbol.length() == 0) {
			// TODO guess the symbol
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
		return DEFAULT_CURRENCY_PATTERN;
	}

	/**
	 * Get decPlaces.
	 * 
	 * @return Returns the decPlaces.
	 */
	public int getDecPlaces() {
		return decPlaces;
	}

	/**
	 * Set decPaces
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
	 * @param useBracket The useBracket to set.
	 */
	public void setUseBracket(boolean useBracket) {
		this.useBracket = useBracket;
	}

	/**
	 * Get symbeol
	 * 
	 * @return Returns the symbol.
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Set symbol
	 * 
	 * @param symbol The symbol to set.
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * get SysmPos
	 * 
	 * @return Returns the symPos.
	 */
	public String getSymPos() {
		return symPos;
	}

	/**
	 * Set symPos
	 * 
	 * @param symPos The symPos to set.
	 */
	public void setSymPos(String symPos) {
		this.symPos = symPos;
	}

	public boolean getUseSpace() {
		return useSpace;
	}

	public void setUseSpace(boolean useSpace) {
		this.useSpace = useSpace;
	}

	public String getRoundingMode() {
		return rounding;
	}

	public void setRoundingMode(String mode) {
		this.rounding = mode;
	}
}