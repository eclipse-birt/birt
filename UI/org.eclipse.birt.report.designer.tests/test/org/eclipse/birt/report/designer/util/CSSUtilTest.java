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

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;

/**
 *
 */

public class CSSUtilTest extends BaseTestCase {

	/**
	 * Conversion factor from inches to cm.
	 */

	private static final double CM_PER_INCH = 2.54;

	/**
	 * Conversion factor from inches to points.
	 */

	private static final double POINTS_PER_INCH = 72;

	/**
	 * Conversion factor from cm to points.
	 */

	private static final double POINTS_PER_CM = POINTS_PER_INCH / CM_PER_INCH;

	/**
	 * Conversion factor from picas to points.
	 */

	// private static final double POINTS_PER_PICA = 12;

	/**
	 * Constucts a test case instance.
	 */
	public CSSUtilTest(String name) {
		super(name);
	}

	/**
	 * Tests get font size.
	 *
	 * This test case is not designed to test the actual numbers (i.e. font sizes)
	 * Rather, this test case is designed to test the function that gets font sizes
	 * such as getFontSize, getSmallerFontSize and getLargerFontSize. getFontSize
	 * should return the size associated with the symbolic size name
	 * getSmallerFontSize should return the size smaller by one except where you
	 * reach bottom getLargerFontSize should return the size larger by one execpt
	 * where you reach ceiling
	 *
	 */
	public void testGetFontSize() {
		DesignElementHandle handle = getReportDesignHandle();
		int baseSize = Integer.parseInt(DesignerConstants.fontSizes[3][1]);

		final int FONT_SIZE_XX_SMALL = Integer.parseInt(DesignerConstants.fontSizes[0][1]);
		final int FONT_SIZE_X_SMALL = Integer.parseInt(DesignerConstants.fontSizes[1][1]);
		final int FONT_SIZE_SMALL = Integer.parseInt(DesignerConstants.fontSizes[2][1]);
		final int FONT_SIZE_MEDIUM = Integer.parseInt(DesignerConstants.fontSizes[3][1]);
		final int FONT_SIZE_LARGE = Integer.parseInt(DesignerConstants.fontSizes[4][1]);
		final int FONT_SIZE_X_LARGE = Integer.parseInt(DesignerConstants.fontSizes[5][1]);
		final int FONT_SIZE_XX_LARGE = Integer.parseInt(DesignerConstants.fontSizes[6][1]);

		String fontSize;
		int sizeValue;

		Object obj = null;
		sizeValue = CSSUtil.getFontSize(obj);
		assertEquals(baseSize, sizeValue);

		fontSize = CSSUtil.getFontSize(handle);
		assertEquals(baseSize, CSSUtil.getFontSizeIntValue(fontSize));

		fontSize = DesignChoiceConstants.FONT_SIZE_XX_LARGE;
		fontSize = CSSUtil.getLargerFontSize(fontSize);
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_XX_LARGE);

		fontSize = DesignChoiceConstants.FONT_SIZE_MEDIUM;
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_MEDIUM);

		fontSize = CSSUtil.getLargerFontSize(fontSize);
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_LARGE);
		assertEquals(fontSize, DesignChoiceConstants.FONT_SIZE_LARGE);

		fontSize = CSSUtil.getLargerFontSize(fontSize);
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_X_LARGE);
		assertEquals(fontSize, DesignChoiceConstants.FONT_SIZE_X_LARGE);

		fontSize = CSSUtil.getLargerFontSize(fontSize);
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_XX_LARGE);
		assertEquals(fontSize, DesignChoiceConstants.FONT_SIZE_XX_LARGE);

		fontSize = CSSUtil.getLargerFontSize(fontSize);
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_XX_LARGE);
		assertEquals(fontSize, DesignChoiceConstants.FONT_SIZE_XX_LARGE);

		fontSize = CSSUtil.getSmallerFontSize(fontSize);
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_X_LARGE);
		assertEquals(fontSize, DesignChoiceConstants.FONT_SIZE_X_LARGE);

		fontSize = CSSUtil.getSmallerFontSize(fontSize);
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_LARGE);
		assertEquals(fontSize, DesignChoiceConstants.FONT_SIZE_LARGE);

		fontSize = CSSUtil.getSmallerFontSize(fontSize);
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_MEDIUM);
		assertEquals(fontSize, DesignChoiceConstants.FONT_SIZE_MEDIUM);

		fontSize = CSSUtil.getSmallerFontSize(fontSize);
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_SMALL);
		assertEquals(fontSize, DesignChoiceConstants.FONT_SIZE_SMALL);

		fontSize = CSSUtil.getSmallerFontSize(fontSize);
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_X_SMALL);
		assertEquals(fontSize, DesignChoiceConstants.FONT_SIZE_X_SMALL);

		fontSize = CSSUtil.getSmallerFontSize(fontSize);
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_XX_SMALL);
		assertEquals(fontSize, DesignChoiceConstants.FONT_SIZE_XX_SMALL);

		fontSize = CSSUtil.getSmallerFontSize(fontSize);
		sizeValue = CSSUtil.getFontSizeIntValue(fontSize);
		assertEquals(sizeValue, FONT_SIZE_XX_SMALL);
		assertEquals(fontSize, DesignChoiceConstants.FONT_SIZE_XX_SMALL);

	}

	/**
	 *
	 */
	public void testConvertTos() {
		int baseSize = 10;
		double pxValue;
		double inchValue;
		double ptValue;
		double px, pt, in;

		Object obj = null;
		inchValue = CSSUtil.convertToInch(obj);
		assertEquals(0, inchValue, 0.01);

		// *** cm => inch, px, pt.
		DimensionValue dim;
		dim = new DimensionValue(36, "cm"); //$NON-NLS-1$

		inchValue = CSSUtil.convertToInch(dim);
		assertEquals(inchValue * CM_PER_INCH, dim.getMeasure(), 0.01);

		pxValue = CSSUtil.convertToPixel(dim);
		px = CSSUtil.inchToPixel(dim.getMeasure() / CM_PER_INCH);
		assertEquals(pxValue, px, 0.01);

		ptValue = CSSUtil.convertToPoint(dim);
		assertEquals(dim.getMeasure() * POINTS_PER_CM, ptValue, 0.01);

		// *** mm => inch, px, pt.
		dim = new DimensionValue(307, "mm"); //$NON-NLS-1$
		inchValue = CSSUtil.convertToInch(dim);
		assertEquals(inchValue * CM_PER_INCH * 10, dim.getMeasure(), 0.01);

		pxValue = CSSUtil.convertToPixel(dim);
		px = CSSUtil.inchToPixel(dim.getMeasure() / CM_PER_INCH / 10);
		assertEquals(pxValue, px, 0.01);

		ptValue = CSSUtil.convertToPoint(dim);
		assertEquals(dim.getMeasure() * POINTS_PER_CM / 10, ptValue, 0.01);

		// *** pt => inch, px
		dim = new DimensionValue(222, "pt"); //$NON-NLS-1$
		inchValue = CSSUtil.convertToInch(dim);
		in = CSSUtil.pointToInch(dim.getMeasure());
		assertEquals(in, inchValue, 0.01);

		pxValue = CSSUtil.convertToPixel(dim);
		px = CSSUtil.pointToPixel(dim.getMeasure());
		assertEquals(pxValue, px, 0.01);

		// --------------- below test convert relative units to absolute units.
		// *** em => inch, px, pt
		dim = new DimensionValue(302, "em"); //$NON-NLS-1$

		inchValue = CSSUtil.convertToInch(dim, baseSize);
		assertEquals(dim.getMeasure() * baseSize / POINTS_PER_INCH, inchValue, 0.01);

		ptValue = CSSUtil.convertToPoint(dim, baseSize);
		assertEquals(dim.getMeasure() * baseSize, ptValue, 0.01);

		pxValue = CSSUtil.convertToPixel(dim, baseSize);
		px = CSSUtil.inchToPixel(inchValue);
		assertEquals(px, pxValue, 0.01);

		// *** ex => inch, px, pt
		dim = new DimensionValue(301, "ex"); //$NON-NLS-1$

		inchValue = CSSUtil.convertToInch(dim, baseSize);
		assertEquals(dim.getMeasure() * baseSize / POINTS_PER_INCH / 3, inchValue, 0.01);

		ptValue = CSSUtil.convertToPoint(dim, baseSize);
		assertEquals(dim.getMeasure() * baseSize / 3, ptValue, 0.01);

		pxValue = CSSUtil.convertToPixel(dim, baseSize);
		px = CSSUtil.inchToPixel(inchValue);
		assertEquals(px, pxValue, 0.01);

		// *** % => inch, px, pt
		dim = new DimensionValue(303, "%"); //$NON-NLS-1$

		ptValue = CSSUtil.convertToPoint(dim, baseSize);
		assertEquals(dim.getMeasure() * baseSize / 100, ptValue, 0.01);

		inchValue = CSSUtil.convertToInch(dim, baseSize);
		assertEquals(dim.getMeasure() * baseSize / POINTS_PER_INCH / 100, inchValue, 0.01);

		pxValue = CSSUtil.convertToPixel(dim, baseSize);
		px = CSSUtil.inchToPixel(inchValue);
		assertEquals(px, pxValue, 0.01);

		// *** px => inch, pt
		dim = new DimensionValue(100, "px"); //$NON-NLS-1$

		inchValue = CSSUtil.convertToInch(dim, baseSize);
		in = CSSUtil.pixelToInch(dim.getMeasure());
		assertEquals(in, inchValue, 0.01);

		ptValue = CSSUtil.convertToPoint(dim, baseSize);
		pt = CSSUtil.pixelToPoint(dim.getMeasure());
		assertEquals(pt, ptValue, 0.01);
	}

	/**
	 * Tests units conversion.
	 *
	 */
	public void testUnitsConversion() {
		DimensionValue dim1 = new DimensionValue(100, "in"); //$NON-NLS-1$

		DimensionValue dim3 = new DimensionValue(7200, "pt"); //$NON-NLS-1$
		DimensionValue dim4 = new DimensionValue(254, "cm"); //$NON-NLS-1$
		DimensionValue dim5 = new DimensionValue(2540, "mm"); //$NON-NLS-1$
		DimensionValue dim6 = new DimensionValue(600, "pc"); //$NON-NLS-1$

		DimensionValue target = CSSUtil.convertTo(dim1.getMeasure(), dim1.getUnits(), dim3.getUnits());
		assertEquals(target, dim3);

		target = CSSUtil.convertTo(dim3.getMeasure(), dim3.getUnits(), dim4.getUnits());
		assertEquals(target.getMeasure(), dim4.getMeasure(), 0.01);
		assertTrue(target.equals(dim4));

		target = CSSUtil.convertTo(dim1.getMeasure(), dim1.getUnits(), dim5.getUnits());
		assertEquals(target, dim5);

		target = CSSUtil.convertTo(dim1.getMeasure(), dim1.getUnits(), dim6.getUnits());
		assertEquals(target, dim6);

		target = CSSUtil.convertTo(dim1, null, dim3.getUnits());
		assertEquals(target, dim3);

		target = CSSUtil.convertTo(dim1, null, dim4.getUnits());
		assertEquals(target, dim4);

		target = CSSUtil.convertTo(dim1, null, dim5.getUnits());
		assertEquals(target, dim5);

		target = CSSUtil.convertTo(dim1, null, dim4.getUnits());
		assertEquals(target, dim4);

		String dimDesp = dim1.toString();
		try {
			target = CSSUtil.convertTo(dimDesp, null, dim3.getUnits());
		} catch (PropertyValueException e) {
			e.printStackTrace();
		}
		assertEquals(target, dim3);

		// *** test isAbsoluteUnits && isRelativeUnits.
		String[] absoluteUnits = { "in", "cm", "mm", "pt", "pc" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		};
		String[] relativeUnits = { "em", "ex", "%", "px" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		};

		for (int i = 0; i < absoluteUnits.length; i++) {
			assertEquals(true, CSSUtil.isAbsoluteUnits(absoluteUnits[i]));
			assertEquals(false, CSSUtil.isRelativeUnits(absoluteUnits[i]));
		}

		for (int j = 0; j < relativeUnits.length; j++) {
			assertEquals(true, CSSUtil.isRelativeUnits(relativeUnits[j]));
			assertEquals(false, CSSUtil.isAbsoluteUnits(relativeUnits[j]));
		}
	}

	/**
	 * Tests get font weight.
	 */
	public void testFontWeight() {
		DesignElementHandle handle = getReportDesignHandle();

		int baseWeight = 400;

		int target = CSSUtil.getFontWeight(handle);
		assertEquals(baseWeight, target, 0.01);

		target = CSSUtil.getFontWeight(DesignChoiceConstants.FONT_WEIGHT_NORMAL);
		assertEquals(baseWeight, target, 0.01);

		target = CSSUtil.getBolderFontWeight(target);
		assertEquals(500, target, 0.01);

		target = CSSUtil.getBolderFontWeight(target);
		assertEquals(600, target);

		target = CSSUtil.getBolderFontWeight(target);
		assertEquals(700, target);

		target = CSSUtil.getBolderFontWeight(target);
		assertEquals(800, target);

		target = CSSUtil.getBolderFontWeight(target);
		assertEquals(900, target);

		target = CSSUtil.getBolderFontWeight(target);
		assertEquals(900, target);

		target = CSSUtil.getLighterFontWeight(target);
		assertEquals(800, target);

		target = CSSUtil.getLighterFontWeight(target);
		assertEquals(700, target);

		target = CSSUtil.getLighterFontWeight(target);
		assertEquals(600, target);

		target = CSSUtil.getLighterFontWeight(target);
		assertEquals(500, target);

		target = CSSUtil.getLighterFontWeight(target);
		assertEquals(400, target);

		target = CSSUtil.getLighterFontWeight(target);
		assertEquals(300, target);

		target = CSSUtil.getLighterFontWeight(target);
		assertEquals(200, target);

		target = CSSUtil.getLighterFontWeight(target);
		assertEquals(100, target);

		target = CSSUtil.getLighterFontWeight(target);
		assertEquals(100, target);

		target = CSSUtil.getFontWeight(DesignChoiceConstants.FONT_WEIGHT_900);
		assertEquals(900, target);

		target = CSSUtil.getLighterFontWeight(target);
		assertEquals(800, target);

		target = CSSUtil.getLighterFontWeight(target);
		assertEquals(700, target);

		target = CSSUtil.getBolderFontWeight(DesignChoiceConstants.FONT_WEIGHT_100);
		assertEquals(200, target);

		target = CSSUtil.getBolderFontWeight(DesignChoiceConstants.FONT_WEIGHT_200);
		assertEquals(300, target);

		target = CSSUtil.getBolderFontWeight(DesignChoiceConstants.FONT_WEIGHT_300);
		assertEquals(400, target);

		target = CSSUtil.getBolderFontWeight(DesignChoiceConstants.FONT_WEIGHT_400);
		assertEquals(500, target);

		target = CSSUtil.getBolderFontWeight(DesignChoiceConstants.FONT_WEIGHT_500);
		assertEquals(600, target);

		target = CSSUtil.getBolderFontWeight(DesignChoiceConstants.FONT_WEIGHT_600);
		assertEquals(700, target);

		target = CSSUtil.getBolderFontWeight(DesignChoiceConstants.FONT_WEIGHT_700);
		assertEquals(800, target);

		target = CSSUtil.getBolderFontWeight(DesignChoiceConstants.FONT_WEIGHT_800);
		assertEquals(900, target);

		target = CSSUtil.getBolderFontWeight(DesignChoiceConstants.FONT_WEIGHT_900);
		assertEquals(900, target);

		target = CSSUtil.getLighterFontWeight(DesignChoiceConstants.FONT_WEIGHT_900);
		assertEquals(800, target);

		target = CSSUtil.getLighterFontWeight(DesignChoiceConstants.FONT_WEIGHT_800);
		assertEquals(700, target);

		target = CSSUtil.getLighterFontWeight(DesignChoiceConstants.FONT_WEIGHT_700);
		assertEquals(600, target);

		target = CSSUtil.getLighterFontWeight(DesignChoiceConstants.FONT_WEIGHT_600);
		assertEquals(500, target);

		target = CSSUtil.getLighterFontWeight(DesignChoiceConstants.FONT_WEIGHT_500);
		assertEquals(400, target);

		target = CSSUtil.getLighterFontWeight(DesignChoiceConstants.FONT_WEIGHT_400);
		assertEquals(300, target);

		target = CSSUtil.getLighterFontWeight(DesignChoiceConstants.FONT_WEIGHT_300);
		assertEquals(200, target);

		target = CSSUtil.getLighterFontWeight(DesignChoiceConstants.FONT_WEIGHT_200);
		assertEquals(100, target);

		target = CSSUtil.getLighterFontWeight(DesignChoiceConstants.FONT_WEIGHT_100);
		assertEquals(100, target);
	}
}
