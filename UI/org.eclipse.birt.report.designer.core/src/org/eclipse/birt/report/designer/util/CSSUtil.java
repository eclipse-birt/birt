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
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * CSS utilities.
 */

public class CSSUtil {

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

	private static final double POINTS_PER_PICA = 12;

	/**
	 *  
	 */
	public static final org.eclipse.swt.graphics.Point dpi = Display.getDefault().getDPI();

	/**
	 * Gets font size of given object.
	 * 
	 * @param object
	 * @return
	 */
	public static int getFontSize(Object object) {
		if (object instanceof DesignElementHandle) {
			String font = getFontSize((DesignElementHandle) object);
			return getFontSizeIntValue(font);
		} else if (object instanceof String) {
			return getFontSizeIntValue((String) object);
		}
		return getFontSizeIntValue(DesignChoiceConstants.FONT_SIZE_MEDIUM);
	}

	/**
	 * Gets font size of given design element handle.
	 * 
	 * @param handle DesignElementHandle
	 * @return
	 */
	public static String getFontSize(DesignElementHandle handle) {
		if (!(handle instanceof ReportItemHandle)) {
			if (handle instanceof ModuleHandle) {
				return DesignChoiceConstants.FONT_SIZE_MEDIUM;
			}
			if (handle instanceof GroupHandle) {
				handle = handle.getContainer();
			}
		}

		StyleHandle styleHandle = handle.getPrivateStyle();
		assert styleHandle != null;
		String fontSize = (String) (styleHandle.getFontSize().getValue());

		if (fontSize.equals(DesignChoiceConstants.FONT_SIZE_LARGER)) {
			String parentFontSize = getFontSize(handle.getContainer());
			return getLargerFontSize(parentFontSize);
		} else if (fontSize.equals(DesignChoiceConstants.FONT_SIZE_SMALLER)) {
			String parentFontSize = getFontSize(handle.getContainer());
			return getSmallerFontSize(parentFontSize);
		}
		return fontSize;
	}

	/**
	 * Gets the larger size of a given font size.
	 * 
	 * @param curSize The given font size.
	 */
	public static String getLargerFontSize(String curSize) {
		assert (curSize != null);
		for (int i = 0; i < DesignerConstants.fontSizes.length - 1; i++) {
			if (curSize.equals(DesignerConstants.fontSizes[i][0])) {
				return DesignerConstants.fontSizes[i + 1][0];
			}
		}
		return DesignerConstants.fontSizes[DesignerConstants.fontSizes.length - 1][0];
	}

	/**
	 * Gets the smaller size of a given font size.
	 * 
	 * @param curSize The given font size.
	 * 
	 */
	public static String getSmallerFontSize(String curSize) {
		assert (curSize != null);
		for (int i = DesignerConstants.fontSizes.length - 1; i > 0; i--) {
			if (curSize.equals(DesignerConstants.fontSizes[i][0])) {
				return DesignerConstants.fontSizes[i - 1][0];
			}
		}
		return DesignerConstants.fontSizes[0][0];
	}

	/**
	 * Gets the int value of a String described font size.
	 * 
	 * @param font The String described font size.
	 */
	public static int getFontSizeIntValue(String font) {
		assert (font != null);
		String size = DesignChoiceConstants.FONT_SIZE_MEDIUM;
		for (int i = 0; i < DesignerConstants.fontSizes.length; i++) {
			if (font.equals(DesignerConstants.fontSizes[i][0])) {
				size = DesignerConstants.fontSizes[i][1];
				break;
			}
		}
		return Integer.parseInt(size);
	}

	/**
	 * Gets the int value of a String described font weight.
	 * 
	 * @param fontWeight The String deccribed font weight.s
	 */
	public static int getFontWeight(String fontWeight) {
		int weight = 400;
		if (fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_100)) {
			weight = 100;
		} else if (fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_200)) {
			weight = 200;
		} else if (fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_300)) {
			weight = 300;
		} else if (fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_400)) {
			weight = 400;
		} else if (fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_500)) {
			weight = 500;
		} else if (fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_600)) {
			weight = 600;
		} else if (fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_700)) {
			weight = 700;
		} else if (fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_800)) {
			weight = 800;
		} else if (fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_900)) {
			weight = 900;
		} else if (fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_NORMAL)) {
			weight = 400;
		} else if (fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_BOLD)) {
			weight = 700;
		}
		return weight;
	}

	/**
	 * Gets the font weight value of a design element handle.
	 * 
	 * @param handle The given design element handle.
	 * @return The int value of the font weight
	 */
	public static int getFontWeight(DesignElementHandle handle) {
		int fontWeight = 400;
		String font = null;
		if (!(handle instanceof ReportItemHandle)) {
			if (handle instanceof ModuleHandle) {
				font = DesignChoiceConstants.FONT_WEIGHT_NORMAL;
				return getFontWeight(font);
			}
			if (handle instanceof GroupHandle) {
				handle = handle.getContainer();
			}
		}

		StyleHandle styleHandle = handle.getPrivateStyle();
		assert styleHandle != null;
		font = styleHandle.getFontWeight();

		if (font.equals(DesignChoiceConstants.FONT_WEIGHT_LIGHTER)) {
			int parentFontWeight = getFontWeight(handle.getContainer());
			return getLighterFontWeight(parentFontWeight);
		} else if (font.equals(DesignChoiceConstants.FONT_WEIGHT_BOLDER)) {
			String parentFontSize = getFontSize(handle.getContainer());
			return getBolderFontWeight(parentFontSize);
		}

		fontWeight = getFontWeight(font);
		return fontWeight;
	}

	/**
	 * Gets the bolder font weight of given font sweight.
	 * 
	 * @param curWeight The given font weight.
	 * @return The bolder font weight value
	 */
	public static int getBolderFontWeight(int curWeight) {
		int weight = curWeight + 100;
		if (weight > 900) {
			weight = 900;
		}
		return weight;
	}

	/**
	 * Gets the bolder font weight of a given String described font sweight.
	 * 
	 * @param curWeight The given String described font weight.
	 * @return The bolder font weight value
	 */
	public static int getBolderFontWeight(String curWeight) {
		int weight = getFontWeight(curWeight) + 100;
		if (weight > 900) {
			weight = 900;
		}
		return weight;
	}

	/**
	 * Gets the lighter font weight of a given String described font sweight.
	 * 
	 * @param curWeight The given String described font weight.
	 * @return The lighter font weight value
	 */
	public static int getLighterFontWeight(String curWeight) {
		int weight = getFontWeight(curWeight) - 100;
		if (weight < 100) {
			weight = 100;
		}
		return weight;
	}

	/**
	 * Gets the lighter font weight of a given font sweight.
	 * 
	 * @param curWeight The given font weight.
	 * @return The lighter font weight value
	 */
	public static int getLighterFontWeight(int curWeight) {
		int weight = curWeight - 100;
		if (weight < 100) {
			weight = 100;
		}
		return weight;
	}

	/**
	 * Converts object 's units to inch
	 * 
	 * @param object
	 * @return The inch value.
	 */
	public static double convertToInch(Object model) {
		return convertToInch(model, 0);
	}

	/**
	 * Converts object 's units to inch, with baseSize to compute the relative unit.
	 * 
	 * @param object   The origine object, may be DimensionValue or DimensionHandle.
	 * @param baseSize The given baseSize used to compute relative unit.
	 * @return The inch value.
	 */
	public static double convertToInch(Object object, int baseSize) {
		double inchValue = 0;
		double measure = 0;
		String units = ""; //$NON-NLS-1$

		if (object instanceof DimensionValue) {
			DimensionValue dimension = (DimensionValue) object;
			measure = dimension.getMeasure();
			units = dimension.getUnits();
		} else if (object instanceof DimensionHandle) {
			DimensionHandle dimension = (DimensionHandle) object;
			measure = dimension.getMeasure();
			units = dimension.getUnits();
		} else {
			// assert false;
		}

		if ("".equalsIgnoreCase(units))//$NON-NLS-1$
		{
			units = SessionHandleAdapter.getInstance().creatReportDesign().getDefaultUnits();
		}
		if (DesignChoiceConstants.UNITS_IN.equals(units)) {
			return measure;
		}

		// sets default baseSize to JFace Resources 's default font data 's
		// height.
		if (baseSize == 0) {
			Font defaultFont = JFaceResources.getDefaultFont();
			FontData[] fontData = defaultFont.getFontData();
			baseSize = fontData[0].getHeight();
		}

		// converts relative units to inch.
		if (DesignChoiceConstants.UNITS_EM.equals(units)) {
			inchValue = DimensionUtil
					.convertTo(measure * baseSize, DesignChoiceConstants.UNITS_PT, DesignChoiceConstants.UNITS_IN)
					.getMeasure();
		} else if (DesignChoiceConstants.UNITS_EX.equals(units)) {
			inchValue = DimensionUtil
					.convertTo(measure * baseSize / 3, DesignChoiceConstants.UNITS_PT, DesignChoiceConstants.UNITS_IN)
					.getMeasure();
		} else if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(units)) {
			inchValue = DimensionUtil
					.convertTo(measure * baseSize / 100, DesignChoiceConstants.UNITS_PT, DesignChoiceConstants.UNITS_IN)
					.getMeasure();
		} else if (DesignChoiceConstants.UNITS_PX.equals(units)) {
			inchValue = pixelToInch(measure);
		} else { // converts absolute units to inch.
			inchValue = DimensionUtil.convertTo(measure, units, DesignChoiceConstants.UNITS_IN).getMeasure();
		}
		return inchValue;
	}

	/**
	 * Converts object 's units to pixel, with baseSize to compute the relative
	 * unit.
	 * 
	 * @param object
	 * @param baseSize The given baseSize used to compute relative unit.
	 * @return The pixel value.
	 */
	public static double convertToPixel(Object object, int baseSize) {
		return inchToPixel(convertToInch(object, baseSize));
	}

	/**
	 * Converts object 's units to pixel.
	 * 
	 * @param object
	 * @return The pixel value.
	 */
	public static double convertToPixel(Object object) {
		return inchToPixel(convertToInch(object));
	}

	/**
	 * Converts object 's units to point, with baseSize to compute the relative
	 * unit.
	 * 
	 * @param object
	 * @param baseSize The given baseSize used to compute relative unit.
	 * @return The point value.
	 */
	public static double convertToPoint(Object object, int baseSize) {
		return inchToPoint(convertToInch(object, baseSize));
	}

	/**
	 * Converts object 's units to pixel.
	 * 
	 * @param object
	 * @return The pixel value.
	 */
	public static double convertToPoint(Object object) {
		return inchToPoint(convertToInch(object));
	}

	/**
	 * Converts inch to point.
	 */
	public static double inchToPoint(double inch) {
		double pt = inch * 72;
		return pt;
	}

	/**
	 * Converts inch to pixel.
	 */
	public static double inchToPixel(double x) {
		return x * dpi.x;

	}

	/**
	 * Converts two inch value to pixels using a Point object to contain them.
	 * 
	 * @param x a inch value.
	 * @param y another inch value.
	 * @return a Point object contains the two pixel values
	 */
	public static Point inchToPixel(double x, double y) {
		int xpixel = (int) (inchToPixel(x));
		int ypixel = (int) (inchToPixel(y));
		return new Point(xpixel, ypixel);
	}

	/**
	 * Converts pixel to inch.
	 * 
	 */
	public static double pixelToInch(double x) {
		return x / dpi.x;
	}

	/**
	 * Converts pixel to point.
	 */
	public static double pixelToPoint(double p) {
		return inchToPoint(pixelToInch(p));
	}

	/**
	 * Converts point to inch.
	 */
	public static double pointToInch(double point) {
		double inch = point / 72;
		return inch;
	}

	/**
	 * Converts point to pixel.
	 */
	public static double pointToPixel(double p) {
		return inchToPixel(pointToInch(p));
	}

	/**
	 * Converts a absolute unit to another absolute unit.
	 * 
	 * @param measure     The measure of the unit.
	 * @param fromUnit    The from unit, must be absolute unit.
	 * @param targetUnits The target unit, must be absolute unit.
	 * @return The target unit
	 */
	public static DimensionValue convertTo(double measure, String fromUnit, String targetUnit) {
		assert (isAbsoluteUnits(fromUnit));

		if (targetUnit.equalsIgnoreCase(fromUnit))
			return new DimensionValue(measure, fromUnit);

		double targetMeasure = 0.0;

		if (DesignChoiceConstants.UNITS_IN.equalsIgnoreCase(targetUnit)) {
			if (DesignChoiceConstants.UNITS_CM.equalsIgnoreCase(fromUnit))
				targetMeasure = measure / CM_PER_INCH;
			else if (DesignChoiceConstants.UNITS_MM.equalsIgnoreCase(fromUnit))
				targetMeasure = measure / CM_PER_INCH / 10;
			else if (DesignChoiceConstants.UNITS_PT.equalsIgnoreCase(fromUnit))
				targetMeasure = measure / POINTS_PER_INCH;
			else if (DesignChoiceConstants.UNITS_PC.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * POINTS_PER_PICA / POINTS_PER_INCH;
			else
				assert false;
		} else if (DesignChoiceConstants.UNITS_CM.equalsIgnoreCase(targetUnit)) {
			if (DesignChoiceConstants.UNITS_IN.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * CM_PER_INCH;
			else if (DesignChoiceConstants.UNITS_MM.equalsIgnoreCase(fromUnit))
				targetMeasure = measure / 10;
			else if (DesignChoiceConstants.UNITS_PT.equalsIgnoreCase(fromUnit))
				targetMeasure = measure / POINTS_PER_CM;
			else if (DesignChoiceConstants.UNITS_PC.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * POINTS_PER_PICA / POINTS_PER_CM;
			else
				assert false;
		} else if (DesignChoiceConstants.UNITS_MM.equalsIgnoreCase(targetUnit)) {
			if (DesignChoiceConstants.UNITS_IN.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * CM_PER_INCH * 10;
			else if (DesignChoiceConstants.UNITS_CM.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * 10;
			else if (DesignChoiceConstants.UNITS_PT.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * 10 / POINTS_PER_CM;
			else if (DesignChoiceConstants.UNITS_PC.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * POINTS_PER_PICA * 10 / POINTS_PER_CM;
			else
				assert false;
		} else if (DesignChoiceConstants.UNITS_PT.equalsIgnoreCase(targetUnit)) {
			if (DesignChoiceConstants.UNITS_IN.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * POINTS_PER_INCH;
			else if (DesignChoiceConstants.UNITS_CM.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * POINTS_PER_CM;
			else if (DesignChoiceConstants.UNITS_MM.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * POINTS_PER_CM / 10;
			else if (DesignChoiceConstants.UNITS_PC.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * POINTS_PER_PICA;
			else
				assert false;
		} else if (DesignChoiceConstants.UNITS_PC.equalsIgnoreCase(targetUnit)) {
			if (DesignChoiceConstants.UNITS_IN.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * POINTS_PER_INCH / POINTS_PER_PICA;
			else if (DesignChoiceConstants.UNITS_CM.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * POINTS_PER_CM / POINTS_PER_PICA;
			else if (DesignChoiceConstants.UNITS_MM.equalsIgnoreCase(fromUnit))
				targetMeasure = measure * POINTS_PER_CM / 10 / POINTS_PER_PICA;
			else if (DesignChoiceConstants.UNITS_PT.equalsIgnoreCase(fromUnit))
				targetMeasure = measure / POINTS_PER_PICA;
			else
				assert false;
		} else
			assert false;

		return new DimensionValue(targetMeasure, targetUnit);
	}

	/**
	 * Converts a DimensionValue object 's unit to another unit.
	 * 
	 * @param dim         The DimeasionValue object.
	 * @param defaultUnit The default unit for the dimension value if object has no
	 *                    unit.
	 * @param targetUnits The target unit, must be absolute unit.
	 * @return The target unit
	 */
	public static DimensionValue convertTo(DimensionValue dim, String defaultUnit, String targetUnits) {
		String fromUnit = dim.getUnits();
		if (DimensionValue.DEFAULT_UNIT.equalsIgnoreCase(fromUnit))
			fromUnit = defaultUnit;
		return convertTo(dim.getMeasure(), fromUnit, targetUnits);
	}

	/**
	 * Converts a DimensionValue object 's unit to another unit.
	 * 
	 * @param dimDesp     The DimeasionValue object 's String description.
	 * @param defaultUnit The default unit for the dimension value if object has no
	 *                    unit.
	 * @param targetUnits The target unit, must be absolute unit.
	 * @return The target unit
	 */
	public static DimensionValue convertTo(String dimDesp, String defaultUnit, String targetUnits)
			throws PropertyValueException {
		DimensionValue dim = DimensionValue.parse(dimDesp);
		return convertTo(dim, defaultUnit, targetUnits);
	}

	/**
	 * Returns if the unit is a absolute unit.
	 */
	public static boolean isAbsoluteUnits(String unit) {
		return DesignChoiceConstants.UNITS_IN.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_CM.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_MM.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_PT.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_PC.equalsIgnoreCase(unit);
	}

	/**
	 * Returns if the unit is a relative unit.
	 */
	public static boolean isRelativeUnits(String unit) {
		return DesignChoiceConstants.UNITS_EM.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_EX.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_PX.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_PERCENTAGE.equalsIgnoreCase(unit);
	}
}
