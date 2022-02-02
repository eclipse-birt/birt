/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.util;

import java.util.List;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.impl.MultipleFillImpl;
import org.eclipse.emf.common.util.EList;

/**
 * Utility class for Fill conversion.
 */

public class FillUtil {

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * Returns a darker color.
	 * 
	 * @param fill
	 * @return darker color
	 */
	public static ColorDefinition getDarkerColor(Fill fill) {
		if (fill instanceof ColorDefinition) {
			return goFactory.darker((ColorDefinition) fill);
		}
		if (fill instanceof Gradient) {
			ColorDefinition cdStart = ((Gradient) fill).getStartColor();
			ColorDefinition cdEnd = ((Gradient) fill).getEndColor();
			return goFactory.darker(getSortedColors(false, cdStart, cdEnd));
		}
		if (fill instanceof Image) {
			// Gray color
			return goFactory.createColorDefinition(128, 128, 128);
		}
		if (fill instanceof MultipleFill) {
			List<Fill> fills = ((MultipleFill) fill).getFills();
			ColorDefinition cd0 = getColor(fills.get(0));
			ColorDefinition cd1 = getColor(fills.get(1));
			return goFactory.darker(getSortedColors(false, cd0, cd1));
		}
		return null;
	}

	/**
	 * Returns a darker fill.
	 * 
	 * @param fill
	 * @return darker color or image
	 */
	public static Fill getDarkerFill(Fill fill) {
		if (fill instanceof ColorDefinition) {
			return goFactory.darker((ColorDefinition) fill);
		}
		if (fill instanceof Gradient) {
			ColorDefinition cdStart = ((Gradient) fill).getStartColor();
			ColorDefinition cdEnd = ((Gradient) fill).getEndColor();
			return goFactory.createGradient(goFactory.darker(cdStart), goFactory.darker(cdEnd));
		}
		if (fill instanceof MultipleFill) {
			List<Fill> fills = ((MultipleFill) fill).getFills();
			MultipleFill newFill = MultipleFillImpl.create();
			for (Fill fill_i : fills) {
				newFill.getFills().add(getDarkerFill(fill_i));
			}
			return newFill;
		}
		if (fill instanceof Image) {
			return fill.copyInstance();
		}
		return fill;
	}

	/**
	 * Returns a brighter color.
	 * 
	 * @param fill
	 * @return brighter color
	 */
	public static ColorDefinition getBrighterColor(Fill fill) {
		if (fill instanceof ColorDefinition) {
			return goFactory.brighter(((ColorDefinition) fill));
		}
		if (fill instanceof Gradient) {
			ColorDefinition cdStart = ((Gradient) fill).getStartColor();
			ColorDefinition cdEnd = ((Gradient) fill).getEndColor();
			return goFactory.brighter(getSortedColors(true, cdStart, cdEnd));
		}
		if (fill instanceof Image) {
			// Gray color
			return goFactory.createColorDefinition(192, 192, 192);
		}
		if (fill instanceof MultipleFill) {
			List<Fill> fills = ((MultipleFill) fill).getFills();
			ColorDefinition cd0 = getColor(fills.get(0));
			ColorDefinition cd1 = getColor(fills.get(1));
			return goFactory.brighter(getSortedColors(true, cd0, cd1));
		}
		return null;
	}

	public static Fill changeBrightness(Fill fill, double brightness) {
		if (fill instanceof ColorDefinition) {
			ColorDefinition new_fill = goFactory.copyOf((ColorDefinition) fill);
			applyBrightness(new_fill, brightness);
			return new_fill;
		}
		return fill;
	}

	/**
	 * Returns a brighter fill.
	 * 
	 * @param fill
	 * @return brighter color or image
	 */
	public static Fill getBrighterFill(Fill fill) {
		if (fill instanceof ColorDefinition) {
			return goFactory.brighter((ColorDefinition) fill);
		}
		if (fill instanceof Gradient) {
			ColorDefinition cdStart = ((Gradient) fill).getStartColor();
			ColorDefinition cdEnd = ((Gradient) fill).getEndColor();
			return goFactory.createGradient(goFactory.brighter(cdStart), goFactory.brighter(cdEnd));
		}
		if (fill instanceof MultipleFill) {
			List<Fill> fills = ((MultipleFill) fill).getFills();
			MultipleFill newFill = MultipleFillImpl.create();
			for (Fill fill_i : fills) {
				newFill.getFills().add(getBrighterFill(fill_i));
			}
			return newFill;
		}
		if (fill instanceof Image) {
			return fill.copyInstance();
		}
		return fill;
	}

	static ColorDefinition getSortedColors(boolean bBrighter, ColorDefinition cd1, ColorDefinition cd2) {
		int result = (cd1.getRed() + cd1.getGreen() + cd1.getBlue())
				- ((cd2.getRed() + cd2.getGreen() + cd2.getBlue()));
		if (bBrighter) {
			return result > 0 ? cd1 : cd2;
		}
		return result > 0 ? cd2 : cd1;
	}

	/**
	 * Converts Fill to Gradient if possible, and changes gradient angle according
	 * to chart direction. If Fill is Image type, just does nothing and returns.
	 * 
	 * @param fill
	 * @param bTransposed
	 * @return Gradient Fill after conversion or original Image Fill
	 */
	public static Fill convertFillToGradient(Fill fill, boolean bTransposed) {
		Gradient grad = null;
		if (fill instanceof ColorDefinition) {
			grad = createDefaultGradient((ColorDefinition) fill);
		} else if (fill instanceof MultipleFill) {
			List<Fill> fills = ((MultipleFill) fill).getFills();
			if (fills.get(0) instanceof ColorDefinition) {
				grad = createDefaultGradient((ColorDefinition) fills.get(0));
			}
		} else if (fill instanceof Gradient) {
			grad = (Gradient) fill;
		}

		if (grad != null) {
			// Change direction if it's transposed
			if (bTransposed && !grad.isSetDirection()) {
				grad.setDirection(90);
			}
			return grad;
		}
		// Do nothing for Image
		return fill;
	}

	private static void applyBrightness(ColorDefinition cdf, double brightness) {
		cdf.set((int) (cdf.getRed() * brightness), (int) (cdf.getGreen() * brightness),
				(int) (cdf.getBlue() * brightness), cdf.getTransparency());
	}

	public static Fill convertFillToGradient3D(Fill fill, boolean bTransposed) {
		if (fill instanceof ColorDefinition) {
			Gradient gradient = AttributeFactory.eINSTANCE.createGradient();
			ColorDefinition newStartColor = (ColorDefinition) changeBrightness(fill, 0.95);
			gradient.setStartColor(newStartColor);

			ColorDefinition newColor = (ColorDefinition) changeBrightness(fill, 0.65);
			gradient.setEndColor(newColor);

			return gradient;
		}
		return convertFillToGradient(fill, bTransposed);
	}

	/**
	 * Creates Gradient fill by default.
	 * 
	 * @param color color to create Gradient
	 * @return default gradient
	 */
	public static Gradient createDefaultGradient(ColorDefinition color) {
		if (color == null) {
			return null;
		}

		int currentLuminance = convertRGBToLuminance(color.getRed(), color.getGreen(), color.getBlue());

		ColorDefinition newStartColor = goFactory.copyOf(color);
		ColorDefinition newEndColor = goFactory.copyOf(color);

		if (currentLuminance < 200) {
			int lumDiff = 240 - currentLuminance;
			newEndColor.setRed(getNewColor(lumDiff, newEndColor.getRed(), 0.3));
			newEndColor.setGreen(getNewColor(lumDiff, newEndColor.getGreen(), 0.59));
			newEndColor.setBlue(getNewColor(lumDiff, newEndColor.getBlue(), 0.11));
		} else {
			int lumDiff = -100;
			newStartColor.setRed(getNewColor(lumDiff, newStartColor.getRed(), 0.3));
			newStartColor.setGreen(getNewColor(lumDiff, newStartColor.getGreen(), 0.59));
			newStartColor.setBlue(getNewColor(lumDiff, newStartColor.getBlue(), 0.11));
		}

		return goFactory.createGradient(newStartColor, newEndColor);
	}

	private static int convertRGBToLuminance(int red, int green, int blue) {
		return (int) (0.3 * red + 0.59 * green + 0.11 * blue);
	}

	private static int getNewColor(int lumDiff, int oldColor, double coefficient) {
		int newColor = (int) (lumDiff * coefficient) + oldColor;
		if (newColor < 0) {
			return 0;
		}
		return newColor < 255 ? newColor : 255;
	}

	/**
	 * The purpose of the Method is to make faster copy of Fill for rendering.
	 * 
	 * @param src
	 * @return fill copy
	 */
	public static Fill copyOf(Fill src) {
		return src.copyInstance();
	}

	/**
	 * Provides a distinct color as far as possible.
	 * 
	 * @param paletteColor
	 * @param paletteSize
	 * @param index
	 * @return
	 */
	private static ColorDefinition tunePaletteColor(ColorDefinition paletteColor, int paletteSize, int index) {
		ColorDefinition color = goFactory.copyOf(paletteColor);

		int cycle = index / paletteSize;
		int offset = (cycle / 3 + 1) * 71;
		int phrase = cycle % 3;

		switch (phrase) {
		case 0:
			color.setRed((color.getRed() + offset) % 256);
			color.setGreen((color.getGreen() + offset) % 256);
			break;
		case 1:
			color.setRed((color.getRed() + offset) % 256);
			color.setBlue((color.getBlue() + offset) % 256);
			break;
		case 2:
			color.setGreen((color.getGreen() + offset) % 256);
			color.setBlue((color.getBlue() + offset) % 256);
			break;
		}

		return color;
	}

	/**
	 * Returns the fill from palette. If the index is less than the palette colors
	 * size, simply return the fill. If else, first return brighter fill, then
	 * darker fill. The color fetching logic is like this: In the first round, use
	 * the color from palette directly. In the second round, use the brighter color
	 * of respective one in the first round. In the third round, use the darker
	 * color of respective one in the first round. In the forth round, use the
	 * brighter color of respective one in the second round. In the fifth round, use
	 * the darker color of respective one in the third round. ...
	 * 
	 * @param elPalette
	 * @param index
	 * @since 2.5
	 * @return fill from palette
	 */
	public static Fill getPaletteFill(EList<Fill> elPalette, int index) {
		final int iPaletteSize = elPalette.size();
		if (iPaletteSize == 0) {
			return null;
		}

		Fill fill = elPalette.get(index % iPaletteSize);
		if (index < iPaletteSize) {
			return goFactory.copyOf(fill);
		}

		if (fill instanceof ColorDefinition) {
			return tunePaletteColor((ColorDefinition) fill, iPaletteSize, index);
		}

		int d = index / iPaletteSize;
		if (d % 2 != 0) {
			Fill brighterFill = getBrighterFill(fill);
			while (d / 2 > 0) {
				d -= 2;
				brighterFill = getBrighterFill(brighterFill);
			}
			return brighterFill;
		}
		Fill darkerFill = getDarkerFill(fill);
		while ((d - 1) / 2 > 0) {
			d -= 2;
			darkerFill = getDarkerFill(darkerFill);
		}
		return darkerFill;
	}

	/**
	 * Converts Fill if possible. If Fill is MultipleFill type, convert to
	 * positive/negative Color according to the value. If not MultipleFill type,
	 * return original fill for positive value, or negative fill for negative value.
	 * 
	 * @param fill      Fill to convert
	 * @param dValue    numeric value
	 * @param fNegative Fill for negative value. Useless for positive value or
	 *                  MultipleFill
	 */
	public static Fill convertFill(Fill fill, double dValue, Fill fNegative) {
		if (dValue >= 0) {
			if (fill instanceof MultipleFill) {
				fill = goFactory.copyOf(((MultipleFill) fill).getFills().get(0));
			}
		} else {
			if (fill instanceof MultipleFill) {
				fill = goFactory.copyOf(((MultipleFill) fill).getFills().get(1));
			} else if (fNegative != null) {
				fill = fNegative;
			}
		}
		return fill;
	}

	/**
	 * Returns a color from various Fill.
	 * 
	 * @param fill
	 * @return color
	 * @since 2.5.1
	 */
	public static ColorDefinition getColor(Fill fill) {
		return getColor(fill, true);
	}

	/**
	 * Returns a color from various Fill.
	 * 
	 * @param fill
	 * @param bSelBrighter , true if brighter color will be selected.
	 * @return color
	 * @since 2.5.1
	 */
	public static ColorDefinition getColor(Fill fill, boolean bSelBrighter) {
		if (fill instanceof ColorDefinition) {
			return goFactory.copyOf(((ColorDefinition) fill));
		}
		if (fill instanceof Gradient) {
			ColorDefinition cdStart = ((Gradient) fill).getStartColor();
			ColorDefinition cdEnd = ((Gradient) fill).getEndColor();
			return goFactory.copyOf(getSortedColors(bSelBrighter, cdStart, cdEnd));
		}
		if (fill instanceof Image) {
			// Gray color
			return goFactory.createColorDefinition(192, 192, 192);
		}
		if (fill instanceof MultipleFill) {
			List<Fill> fills = ((MultipleFill) fill).getFills();
			ColorDefinition cdStart = getColor(fills.get(0));
			ColorDefinition cdEnd = getColor(fills.get(fills.size() - 1));
			return goFactory.copyOf(getSortedColors(bSelBrighter, cdStart, cdEnd));
		}
		return null;
	}

}
