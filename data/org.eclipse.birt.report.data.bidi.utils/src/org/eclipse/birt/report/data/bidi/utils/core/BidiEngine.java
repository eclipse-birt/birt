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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;

/**
 * Bidi engine that performs various types of Bidi reordering and shaping.
 *
 * @author Lina Kemmel (bidi_hcg)
 */
public class BidiEngine extends Bidi {

	private static final int CORE_LTR = 0;
	private static final int CORE_RTL = 1;
	private static final int REVERSE = 2;
	private static final int SHAPE = 3;

	public static BidiEngine INSTANCE = new BidiEngine();

	private List<BidiScheme> reorderingSchemes, shapingSchemes;

	/**
	 * BidiEngine default constructor
	 */
	private BidiEngine() {
		super();
		initReorderingSchemes();
		initShapingSchemes();
	}

	/**
	 * Reorders the given string according to the input and output Bidi format.
	 *
	 * @param txt       Input String to reorder
	 * @param inFormat  String representing the input format
	 * @param outFormat String representing the output format
	 * @return Resultant reordered string
	 */
	public String process(String txt, String inFormat, String outFormat) {
		if (!isBidi(txt, inFormat, outFormat) || !BidiFormat.isValidBidiFormat(inFormat)
				|| !BidiFormat.isValidBidiFormat(outFormat) || inFormat.equals(outFormat)) {
			return txt;
		}
		normalizeFormat(inFormat, txt);
		normalizeFormat(outFormat, txt);

		ListIterator<BidiScheme> it = reorderingSchemes.listIterator();

		while (it.hasNext()) {
			BidiScheme scheme = it.next();

			if (inFormat.matches(scheme.inFormat) && outFormat.matches(scheme.outFormat)) {
				String outStr = txt;

				boolean doMirroring = inFormat.charAt(BidiFormat.SYM_SWAP_INDX) != outFormat
						.charAt(BidiFormat.SYM_SWAP_INDX);
				int shapeOptions = getShapingOptions(inFormat, outFormat);

				int[] procedures = (int[]) scheme.data;
				for (int i = 0, n = procedures.length; i < n; i++) {
					if (shapeOptions != 0 && SHAPE == procedures[i]) {
						outStr = shape(outStr, shapeOptions);
					} else if (CORE_LTR == procedures[i]) {
						outStr = coreLTR(outStr, doMirroring);
						doMirroring = false;
					} else if (CORE_RTL == procedures[i]) {
						outStr = coreRTL(outStr, doMirroring);
						doMirroring = false;
					} else if (REVERSE == procedures[i]) {
						outStr = reverse(outStr, doMirroring);
					}
				}
				return outStr;
			}
		}
		return txt;
	}

	/**
	 * Populates Bidi reordering schemes list
	 */
	private void initReorderingSchemes() {
		reorderingSchemes = new ArrayList<>();

		/* 0: Visual RTL => Visual LTR */
		reorderingSchemes.add(new BidiScheme("VL.{3}", "VR.{3}", //$NON-NLS-1$
				new int[] { SHAPE, REVERSE }));
		/* 1: Visual LTR => Visual RTL */
		reorderingSchemes.add(new BidiScheme("VR.{3}", "VL.{3}", //$NON-NLS-1$ //$NON-NLS-2$
				new int[] { REVERSE, SHAPE }));
		/* 2: Logical LTR => Visual LTR */
		reorderingSchemes.add(new BidiScheme("IL.{3}", "VL.{3}", //$NON-NLS-1$ //$NON-NLS-2$
				new int[] { CORE_LTR, SHAPE }));
		/* 3: Logical RTL => Visual LTR */
		reorderingSchemes.add(new BidiScheme("IR.{3}", "VL.{3}", //$NON-NLS-1$ //$NON-NLS-2$
				new int[] { CORE_RTL, SHAPE }));
		/* 4: Logical LTR => Visual RTL */
		reorderingSchemes.add(new BidiScheme("IL.{3}", "VR.{3}", //$NON-NLS-1$ //$NON-NLS-2$
				new int[] { CORE_LTR, SHAPE, REVERSE }));
		/* 5: Logical RTL => Visual RTL */
		reorderingSchemes.add(new BidiScheme("IR.{3}", "VR.{3}", //$NON-NLS-1$ //$NON-NLS-2$
				new int[] { CORE_RTL, SHAPE, REVERSE }));
		/* 6: Logical LTR => Logical RTL */
		reorderingSchemes.add(new BidiScheme("IL.{3}", "IR.{3}", //$NON-NLS-1$ //$NON-NLS-2$
				new int[] { CORE_LTR, SHAPE, REVERSE, CORE_RTL, REVERSE }));
		/* 7: Logical RTL => Logical LTR */
		reorderingSchemes.add(new BidiScheme("IR.{3}", "IL.{3}", //$NON-NLS-1$ //$NON-NLS-2$
				new int[] { CORE_RTL, SHAPE, CORE_LTR }));
		/* 8: Visual LTR => Logical RTL */
		reorderingSchemes.add(new BidiScheme("VL.{3}", "IR.{3}", //$NON-NLS-1$ //$NON-NLS-2$
				new int[] { SHAPE, REVERSE, CORE_RTL, REVERSE }));
		/* 9: Visual RTL => Logical RTL */
		reorderingSchemes.add(new BidiScheme("VR.{3}", "IR.{3}", //$NON-NLS-1$ //$NON-NLS-2$
				new int[] { REVERSE, SHAPE, REVERSE, CORE_RTL, REVERSE }));
		/* 10: Visual LTR => Logical LTR */
		reorderingSchemes.add(new BidiScheme("VL.{3}", "IL.{3}", //$NON-NLS-1$ //$NON-NLS-2$
				new int[] { SHAPE, CORE_LTR }));
		/* 11: Visual RTL => Logical LTR */
		reorderingSchemes.add(new BidiScheme("VR.{3}", "IL.{3}", //$NON-NLS-1$ //$NON-NLS-2$
				new int[] { REVERSE, SHAPE, CORE_LTR }));
	}

	/**
	 * Populates Bidi shaping schemes list
	 */
	private void initShapingSchemes() {
		shapingSchemes = new ArrayList<>();

		shapingSchemes.add(new BidiScheme(".{3}S.", ".{3}NN", //$NON-NLS-1$ //$NON-NLS-2$
				ArabicShaping.LETTERS_UNSHAPE | ArabicShaping.DIGITS_AN2EN));

		shapingSchemes.add(new BidiScheme(".{3}S.", ".{3}NH", //$NON-NLS-1$ //$NON-NLS-2$
				ArabicShaping.LETTERS_UNSHAPE | ArabicShaping.DIGITS_EN2AN));

		shapingSchemes.add(new BidiScheme(".{3}S.", ".L.NC", //$NON-NLS-1$ //$NON-NLS-2$
				ArabicShaping.LETTERS_UNSHAPE | ArabicShaping.DIGITS_EN2AN_INIT_LR));

		shapingSchemes.add(new BidiScheme(".{3}S.", ".R.NC", //$NON-NLS-1$ //$NON-NLS-2$
				ArabicShaping.LETTERS_UNSHAPE | ArabicShaping.DIGITS_EN2AN_INIT_AL));

		shapingSchemes.add(new BidiScheme(".{3}N.", ".{3}SN", //$NON-NLS-1$ //$NON-NLS-2$
				ArabicShaping.LETTERS_SHAPE | ArabicShaping.DIGITS_AN2EN));

		shapingSchemes.add(new BidiScheme(".{3}N.", ".{3}SH", //$NON-NLS-1$ //$NON-NLS-2$
				ArabicShaping.LETTERS_SHAPE | ArabicShaping.DIGITS_EN2AN));

		shapingSchemes.add(new BidiScheme(".{3}N.", ".L.SC", //$NON-NLS-1$ //$NON-NLS-2$
				ArabicShaping.LETTERS_SHAPE | ArabicShaping.DIGITS_EN2AN_INIT_LR));

		shapingSchemes.add(new BidiScheme(".{3}N.", ".R.SC", //$NON-NLS-1$ //$NON-NLS-2$
				ArabicShaping.LETTERS_SHAPE | ArabicShaping.DIGITS_EN2AN_INIT_AL));
	}

	/**
	 * Performs basic Bidi reordering of a text paragraph
	 *
	 * @param src         Input String
	 * @param level       Paragraph embedding level
	 * @param doMirroring Whether to perform character mirroring or not
	 * @return Reordered String
	 */
	private String reorder(String src, byte level, boolean doMirroring) {
		setPara(src, level, null);

		if (doMirroring) {
			return writeReordered(DO_MIRRORING);
		}

		return writeReordered(REORDER_DEFAULT);
	}

	/**
	 * Performs basic Bidi reordering of a LTR paragraph (Logical LTR -> Visual LTR)
	 *
	 * @param src         Input String
	 * @param doMirroring Whether to perform character mirroring or not
	 * @return Reordered String
	 */
	private String coreLTR(String src, boolean doMirroring) {
		return reorder(src, (byte) DIRECTION_LEFT_TO_RIGHT, doMirroring);
	}

	/**
	 * Performs basic Bidi reordering of a RTL paragraph (Logical RTL -> Visual LTR)
	 *
	 * @param src         Input String
	 * @param doMirroring Whether to perform character mirroring or not
	 * @return Reordered String
	 */
	private String coreRTL(String src, boolean doMirroring) {
		return reorder(src, (byte) DIRECTION_RIGHT_TO_LEFT, doMirroring);
	}

	/**
	 * Performs string reverse
	 *
	 * @param str         String to reverse
	 * @param doMirroring Whether perform character mirroring or not
	 * @return Reversed string
	 */
	private String reverse(String str, boolean doMirroring) {
		if (doMirroring) {
			return writeReverse(str, OUTPUT_REVERSE | DO_MIRRORING);
		} else {
			return writeReverse(str, OUTPUT_REVERSE);
		}
	}

	/**
	 * @param format
	 * @return
	 */
	private int getShapingOptions(String inFormat, String outFormat) {
		ListIterator<BidiScheme> it = shapingSchemes.listIterator();

		while (it.hasNext()) {
			BidiScheme scheme = it.next();

			if (inFormat.matches(scheme.inFormat) && outFormat.matches(scheme.outFormat)) {
				return ((Integer) scheme.data).intValue();
			}
		}
		return 0;
	}

	/**
	 * Performs Arabic numeric and literal shaping
	 *
	 * @param str     source String to shape
	 * @param options Shape options
	 * @return shaped String
	 */
	private String shape(String str, int options) {
		ArabicShaping shaper = new ArabicShaping(options | ArabicShaping.TEXT_DIRECTION_VISUAL_LTR);
		try {
			return shaper.shape(str);
		} catch (ArabicShapingException e) {
			e.printStackTrace();
			return str;
		}
	}

	/**
	 * Figures out if the given text requires Bidi processing
	 *
	 * @param txt       String to query Bidi processing
	 * @param inFormat  String representing input Bidi format
	 * @param outFormat String representing output Bidi format
	 * @return Boolean indicating whether the string needs Bidi processing
	 */
	private boolean isBidi(String txt, String inFormat, String outFormat) {
		if (txt == null || txt.length() < 1) {
			return false;
		}

		if (!(new Bidi(txt, DIRECTION_RIGHT_TO_LEFT)).isLeftToRight()) {
			return true;
		}

		boolean inIsRTL = BidiConstants.TEXT_DIRECTION_RTL.equals(BidiFormat.getTextDirectionFromStr(inFormat));

		boolean outIsRTL = BidiConstants.TEXT_DIRECTION_RTL.equals(BidiFormat.getTextDirectionFromStr(outFormat));

		return inIsRTL != outIsRTL;
	}

	/**
	 * @param ch
	 * @return
	 */
	private boolean isLtr(char ch) {
		return UCharacter.DIRECTIONALITY_LEFT_TO_RIGHT == UCharacter.getDirectionality(ch);
	}

	/**
	 * @param ch
	 * @return
	 */
	private boolean isRtl(char ch) {
		byte direction = UCharacter.getDirectionality(ch);
		return UCharacter.DIRECTIONALITY_RIGHT_TO_LEFT == direction
				|| UCharacter.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC == direction;
	}

	/**
	 * @param formatStr
	 * @param txt
	 * @return
	 */
	private String normalizeFormat(String formatStr, String txt) {
		String direction = BidiFormat.getTextDirectionFromStr(formatStr);
		boolean isLtr = BidiConstants.TEXT_DIRECTION_CONTEXTLTR.equals(direction);
		boolean isRtl = !isLtr && BidiConstants.TEXT_DIRECTION_CONTEXTRTL.equals(direction);

		if (!(isLtr || isRtl)) {
			return formatStr;
		}

		for (int i = 0, n = txt.length(); i < n; i++) {
			if (isLtr(txt.charAt(i))) {
				isRtl = false;
				break;
			}
			if (isRtl(txt.charAt(i))) {
				isRtl = true;
				break;
			}
		}
		char[] newFormat = formatStr.toCharArray();
		newFormat[BidiFormat.TEXT_DIRECTION_INDX] = isRtl ? BidiFormat.TEXT_DIRECTION_RTL_CODE
				: BidiFormat.TEXT_DIRECTION_LTR_CODE;
		return new String(newFormat);
	}

	static class BidiScheme {

		String inFormat = ""; //$NON-NLS-1$
		String outFormat = ""; //$NON-NLS-1$
		Object data = null;

		BidiScheme(String inFormat, String outFormat, Object data) {
			this.inFormat = inFormat;
			this.outFormat = outFormat;
			this.data = data;
		}
	}

}
