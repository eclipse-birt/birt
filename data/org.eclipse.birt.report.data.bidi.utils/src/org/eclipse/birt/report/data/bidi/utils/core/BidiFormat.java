/***********************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.data.bidi.utils.core;

/**
 * @author fira@il.ibm.com (bidi_hcg)
 * 
 */
public class BidiFormat {
	private final static char ORDERING_SCHEME_LOGICAL_CODE = 'I';
	private final static char ORDERING_SCHEME_VISUAL_CODE = 'V';
	protected final static char TEXT_DIRECTION_LTR_CODE = 'L';
	protected final static char TEXT_DIRECTION_RTL_CODE = 'R';
	private final static char TEXT_DIRECTION_CONTEXTUALLTR_CODE = 'C';
	private final static char TEXT_DIRECTION_CONTEXTUALRTL_CODE = 'D';
	private final static char SYMSWAPP_YES = 'Y';
	private final static char SYMSWAPP_NO = 'N';
	private final static char SHAPING_NOMINAL_CODE = 'N';
	private final static char SHAPING_SHAPED_CODE = 'S';
	private final static char NUMSHAPING_NOMINAL_CODE = 'N';
	private final static char NUMSHAPING_NATIONAL_CODE = 'H';
	private final static char NUMSHAPING_CONTEXTUAL_CODE = 'C';
	private final static int ORDERING_SCHEME_INDX = 0;
	protected final static int TEXT_DIRECTION_INDX = 1;
	protected final static int SYM_SWAP_INDX = 2;
	private final static int TEXT_SHAPING_INDX = 3;
	private final static int NUMERAL_SHAPING_INDX = 4;
	private final static int MAX_INDX = 5;

	private final static String VALID_FORMAT = "[IViv][LRCDlrcd][YNyn][NSns][NHCnhcp]"; //$NON-NLS-1$

	private String orderingScheme;
	private String textDirection;
	private String numeralShaping;
	private String textShaping;
	private boolean symSwap;

	public BidiFormat(String orderingScheme, String textDirection, boolean symSwap, String textShaping,
			String numeralShaping) {
		this.orderingScheme = orderingScheme;
		this.textDirection = textDirection;
		this.symSwap = symSwap;
		this.textShaping = textShaping;
		this.numeralShaping = numeralShaping;
	}

	public BidiFormat(String bidiFormatString) {
		if (!isValidBidiFormat(bidiFormatString))
			bidiFormatString = BidiConstants.DEFAULT_BIDI_FORMAT_STR;
		bidiFormatString = bidiFormatString.toUpperCase();
		orderingScheme = getOrderingSchemeFromStr(bidiFormatString);
		textDirection = getTextDirectionFromStr(bidiFormatString);
		symSwap = getSymSwapFromStr(bidiFormatString);
		textShaping = getTextShapingFromStr(bidiFormatString);
		numeralShaping = getNumeralShapingFromStr(bidiFormatString);
	}

	public String getOrderingScheme() {
		return orderingScheme;
	}

	public String getTextDirection() {
		return textDirection;
	}

	public boolean getSymSwap() {
		return symSwap;
	}

	public String getTextShaping() {
		return textShaping;
	}

	public String getNumeralShaping() {
		return numeralShaping;
	}

	public BidiFormat getBiDiFormat() {
		return this;
	}

	public String getBiDiFormatString() {
		StringBuffer buf = new StringBuffer(MAX_INDX + 1);

		if (orderingScheme.equals(BidiConstants.ORDERING_SCHEME_LOGICAL))
			buf.append(ORDERING_SCHEME_LOGICAL_CODE);
		else if (orderingScheme.equals(BidiConstants.ORDERING_SCHEME_VISUAL))
			buf.append(ORDERING_SCHEME_VISUAL_CODE);

		if (textDirection.equals(BidiConstants.TEXT_DIRECTION_LTR))
			buf.append(TEXT_DIRECTION_LTR_CODE);
		else if (textDirection.equals(BidiConstants.TEXT_DIRECTION_RTL))
			buf.append(TEXT_DIRECTION_RTL_CODE);
		else if (textDirection.equals(BidiConstants.TEXT_DIRECTION_CONTEXTLTR))
			buf.append(TEXT_DIRECTION_CONTEXTUALLTR_CODE);
		else if (textDirection.equals(BidiConstants.TEXT_DIRECTION_CONTEXTRTL))
			buf.append(TEXT_DIRECTION_CONTEXTUALRTL_CODE);

		if (symSwap)
			buf.append(SYMSWAPP_YES);
		else
			buf.append(SYMSWAPP_NO);

		if (textShaping.equals(BidiConstants.SHAPING_NOMINAL))
			buf.append(SHAPING_NOMINAL_CODE);
		else if (textShaping.equals(BidiConstants.SHAPING_SHAPED))
			buf.append(SHAPING_SHAPED_CODE);
		if (numeralShaping.equals(BidiConstants.NUMSHAPING_NOMINAL))
			buf.append(NUMSHAPING_NOMINAL_CODE);
		else if (numeralShaping.equals(BidiConstants.NUMSHAPING_NATIONAL))
			buf.append(NUMSHAPING_NATIONAL_CODE);
		else if (numeralShaping.equals(BidiConstants.NUMSHAPING_CONTEXT))
			buf.append(NUMSHAPING_CONTEXTUAL_CODE);

		if (buf.length() < MAX_INDX)
			return BidiConstants.DEFAULT_BIDI_FORMAT_STR;

		return String.valueOf(buf);

	}

	private static String getOrderingSchemeFromStr(String bidiFormatStr) {
		char code = bidiFormatStr.charAt(ORDERING_SCHEME_INDX);
		if (code == ORDERING_SCHEME_LOGICAL_CODE)
			return BidiConstants.ORDERING_SCHEME_LOGICAL;
		return BidiConstants.ORDERING_SCHEME_VISUAL;
	}

	public static String getTextDirectionFromStr(String bidiFormatStr) {
		char code = bidiFormatStr.charAt(TEXT_DIRECTION_INDX);
		if (code == TEXT_DIRECTION_RTL_CODE)
			return BidiConstants.TEXT_DIRECTION_RTL;
		if (code == TEXT_DIRECTION_CONTEXTUALLTR_CODE)
			return BidiConstants.TEXT_DIRECTION_CONTEXTLTR;
		if (code == TEXT_DIRECTION_CONTEXTUALRTL_CODE)
			return BidiConstants.TEXT_DIRECTION_CONTEXTRTL;
		return BidiConstants.TEXT_DIRECTION_LTR;
	}

	private static boolean getSymSwapFromStr(String bidiFormatStr) {
		char code = bidiFormatStr.charAt(SYM_SWAP_INDX);
		return code == SYMSWAPP_YES;
	}

	protected static String getTextShapingFromStr(String bidiFormatStr) {
		char code = bidiFormatStr.charAt(TEXT_SHAPING_INDX);
		if (code == SHAPING_NOMINAL_CODE)
			return BidiConstants.SHAPING_NOMINAL;
		return BidiConstants.SHAPING_SHAPED;
	}

	protected static String getNumeralShapingFromStr(String bidiFormatStr) {
		char code = bidiFormatStr.charAt(NUMERAL_SHAPING_INDX);
		if (code == NUMSHAPING_CONTEXTUAL_CODE)
			return BidiConstants.NUMSHAPING_CONTEXT;
		if (code == NUMSHAPING_NATIONAL_CODE)
			return BidiConstants.NUMSHAPING_NATIONAL;
		return BidiConstants.NUMSHAPING_NOMINAL;
	}

	public void update(String fieldName, Object value) {
		if (BidiConstants.BIDI_FORMAT_ORIENTATION.equals(fieldName)
				|| BidiConstants.BIDI_FORMAT_CONTENT_ORIENTATION.equals(fieldName)
				|| BidiConstants.BIDI_FORMAT_METADATA_ORIENTATION.equals(fieldName))
			updateTextDirection(value.toString());
		else if (BidiConstants.BIDI_FORMAT_ORDERINGSCHEME.equals(fieldName)
				|| BidiConstants.BIDI_FORMAT_CONTENT_ORDERINGSCHEME.equals(fieldName)
				|| BidiConstants.BIDI_FORMAT_METADATA_ORDERINGSCHEME.equals(fieldName))
			updateOrderingScheme(value.toString());
		else if (BidiConstants.BIDI_FORMAT_SYMSWAP.equals(fieldName)
				|| BidiConstants.BIDI_FORMAT_CONTENT_SYMSWAP.equals(fieldName)
				|| BidiConstants.BIDI_FORMAT_METADATA_SYMSWAP.equals(fieldName))
			updateSymSwap(value);
		else if (BidiConstants.BIDI_FORMAT_TEXTSHAPING.equals(fieldName)
				|| BidiConstants.BIDI_FORMAT_CONTENT_TEXTSHAPING.equals(fieldName)
				|| BidiConstants.BIDI_FORMAT_METADATA_TEXTSHAPING.equals(fieldName))
			updateTextShaping(value.toString());
		else
			// BiDiConstants.BIDI_FOMAT_NUMSHAPING.equals(fieldName)
			updateNumericShaping(value.toString());
	}

	private void updateTextDirection(String value) {
		if ((BidiConstants.TEXT_DIRECTION_LTR.equals(value)) || (BidiConstants.TEXT_DIRECTION_RTL.equals(value))
				|| (BidiConstants.TEXT_DIRECTION_CONTEXTLTR.equals(value))
				|| (BidiConstants.TEXT_DIRECTION_CONTEXTRTL.equals(value)))
			textDirection = value;
	}

	private void updateOrderingScheme(String value) {
		if ((BidiConstants.ORDERING_SCHEME_LOGICAL.equals(value))
				|| (BidiConstants.ORDERING_SCHEME_VISUAL.equals(value)))
			orderingScheme = value;
	}

	private void updateSymSwap(Object value) {
		symSwap = (Boolean.valueOf(value.toString())).booleanValue();
	}

	private void updateTextShaping(String value) {
		if ((BidiConstants.SHAPING_NOMINAL.equals(value)) || (BidiConstants.SHAPING_SHAPED.equals(value)))
			textShaping = value;
	}

	private void updateNumericShaping(String value) {
		if ((BidiConstants.NUMSHAPING_CONTEXT.equals(value)) || (BidiConstants.NUMSHAPING_NATIONAL.equals(value))
				|| (BidiConstants.NUMSHAPING_NOMINAL.equals(value)))
			numeralShaping = value;
	}

	public static boolean isValidBidiFormat(String str) {
		return str != null && str.matches(VALID_FORMAT);
	}

	public boolean equals(BidiFormat biDiFormat) {
		if (this.numeralShaping.equals(biDiFormat.numeralShaping)
				&& this.orderingScheme.equals(biDiFormat.orderingScheme)
				&& this.textDirection.equals(biDiFormat.textDirection)
				&& this.textShaping.equals(biDiFormat.textShaping) && this.symSwap == biDiFormat.symSwap)
			return true;
		return false;
	}

	public String toString() {
		return this.getBiDiFormatString();
	}

}
