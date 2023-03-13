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

package org.eclipse.birt.report.model.metadata;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Color property type. Color properties are stored either as an integer or a
 * string. If the user enters a color name, then we store the name. An RGB value
 * is stored as an integer. The various <code>getMumble</code> methods perform
 * the task of translating from color names to RGB values when needed.
 * <p>
 * Integer color values can be converted into other formats using a format
 * preference. Method {@link ColorUtil#format(int, int) }is used to do the
 * conversion.
 */

public class ColorPropertyType extends PropertyType {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(ColorPropertyType.class.getName());

	/**
	 * ChoiceSet name for color set.
	 */

	public static final String COLORS_CHOICE_SET = "colors"; //$NON-NLS-1$

	// colors

	/**
	 * The color black. sRGB value "#000000".
	 */

	public static final String BLACK = "black"; //$NON-NLS-1$

	/**
	 * The color white. sRGB value ""#FFFFFF".
	 */

	public static final String WHITE = "white"; //$NON-NLS-1$

	/**
	 * The color red. sRGB value "#FF0000".
	 */

	public static final String RED = "red"; //$NON-NLS-1$

	/**
	 * The color blue. sRGB value "#0000FF".
	 */

	public static final String BLUE = "blue"; //$NON-NLS-1$

	/**
	 * The color blue. sRGB value "#FFA500".
	 */

	public static final String ORANGE = "orange"; //$NON-NLS-1$

	/**
	 * The color silver. sRGB value "#C0C0C0".
	 */

	public static final String SILVER = "silver"; //$NON-NLS-1$

	/**
	 * The color gray. sRGB value "#808080".
	 */

	public static final String GRAY = "gray"; //$NON-NLS-1$

	/**
	 * The color maroon. sRGB value "#800000".
	 */

	public static final String MAROON = "maroon"; //$NON-NLS-1$

	/**
	 * The color purple. sRGB value "#800080".
	 */

	public static final String PURPLE = "purple"; //$NON-NLS-1$

	/**
	 * The color fuchsia. sRGB value "#FF00FF".
	 */

	public static final String FUCHSIA = "fuchsia"; //$NON-NLS-1$

	/**
	 * The color green. sRGB value "#008000".
	 */

	public static final String GREEN = "green"; //$NON-NLS-1$

	/**
	 * The color lime. sRGB value "#00FF00".
	 */

	public static final String LIME = "lime"; //$NON-NLS-1$

	/**
	 * The color olive. sRGB value "#808000".
	 */

	public static final String OLIVE = "olive"; //$NON-NLS-1$

	/**
	 * The color yellow. sRGB value "#FFFF00".
	 */

	public static final String YELLOW = "yellow"; //$NON-NLS-1$

	/**
	 * The color navy. sRGB value "#000080".
	 */

	public static final String NAVY = "navy"; //$NON-NLS-1$

	/**
	 * The color teal. sRGB value "#008080".
	 */

	public static final String TEAL = "teal"; //$NON-NLS-1$

	/**
	 * The color aqua. sRGB value "#00FFFF".
	 */

	public static final String AQUA = "aqua"; //$NON-NLS-1$

	/**
	 * Color choice set.
	 */

	private ChoiceSet colorChoices = null;

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.color"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public ColorPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/**
	 * Get the predefined color choice set.
	 *
	 * @return the predefined color choice set.
	 */

	@Override
	public IChoiceSet getChoices() {
		if (colorChoices == null) {
			colorChoices = (ChoiceSet) MetaDataDictionary.getInstance().getChoiceSet(COLORS_CHOICE_SET);
			assert colorChoices != null;
		}

		return colorChoices;
	}

	/**
	 * Validate a color property value. A color property can be:
	 * <ul>
	 * <li>Null or blank String meaning to clear the property value.</li>
	 * <li>A string with a value of the form 0xhhhhhh that follows the rules for a
	 * Java hexadecimal constant.</li>
	 * <li>A string with a value #hhhhhh that follows the rules of HTML color
	 * codes.</li>
	 * <li>A string with a value #hhh that follows the rules of HTML RGB color
	 * model. The three-digit RGB notation (#rgb) is converted into six-digit form
	 * (#rrggbb) by replicating digits, not by adding zeros.</li>
	 * <li>One of the HTML color names</li>
	 * <li>One of the localized color names.</li>
	 * <li>Css relative value RGB(100.0%, 50.0%, 50.0%)</li>
	 * <li>Css absolute value RGB(255, 127, 127 )</li>
	 * <li>A custom color name defined in the design.</li>
	 * <li>An integer with a valid RGB color value.</li>
	 * </ul>
	 * <p>
	 * Colors recognized as names should include those supported by <a
	 * href="http://msdn.microsoft.com/library/default.asp?url=/workshop/author/dhtml/reference/colors/colors.asp"
	 * Internet Explorer </a> and
	 * <a href="http://www.w3.org/TR/html401/types.html#h-6.5">HTML </a>.
	 *
	 * @return an Integer if the value represents an RBG value, a String if the
	 *         value represents a color name defined in rom.def or in the design, or
	 *         null if the value is null
	 */

	@Override
	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			return validateInputString(module, element, defn, (String) value);
		}
		if (value instanceof Integer) {
			int intValue = ((Integer) value).intValue();

			if (intValue >= 0 && intValue <= 0xFFFFFF) {
				return value;
			}
		}
		logger.log(Level.SEVERE, "Invalid color value " + value); //$NON-NLS-1$

		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, COLOR_TYPE);
	}

	/**
	 * Validate a xml color property value. The possible valid values are the same
	 * to {@link #validateValue(Module, PropertyDefn, Object)}except that the input
	 * <code>value</code> is a String, and it can not be a localized color name.
	 *
	 * @see #validateValue(Module, PropertyDefn, Object)
	 */

	@Override
	public Object validateXml(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		assert value == null || value instanceof String;
		String tmpValue = (String) value;

		tmpValue = StringUtil.trimString(tmpValue);
		if (tmpValue == null) {
			return null;
		}

		Object validValue = validateColor(module, tmpValue);
		if (validValue != null) {
			return validValue;
		}

		// String does not make sense.

		logger.log(Level.SEVERE, "Invalid color property value " + tmpValue); //$NON-NLS-1$

		throw new PropertyValueException(tmpValue, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, COLOR_TYPE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	@Override
	public int getTypeCode() {
		return COLOR_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	@Override
	public String getName() {
		return COLOR_TYPE_NAME;
	}

	/**
	 * Returns the color as a string. Returns the color as its internal string name
	 * if the color is identified by name. Returns the color as an RGB value if
	 * identified by RGB value.
	 *
	 * @return the internal name of the color if it is identified by name, return
	 *         the color as an RGB value(#FF00FF) if it is identified by RGB integer
	 *         value.
	 */

	@Override
	public String toString(Module module, PropertyDefn defn, Object value) {
		if (value == null) {
			return null;
		}

		// A color name.

		if (value instanceof String) {
			return (String) value;
		}

		// Integer color value
		if (value instanceof Integer) {
			int color = ((Integer) value).intValue();
			return StringUtil.toRgbText(color).toUpperCase();
		}

		return null;
	}

	/**
	 * Return the integer value of the color. The color can be identified by a color
	 * value, a pre-defined color name or a custom color.
	 *
	 * @return the integer value of the color. Return <code>-1</code> if
	 *         <code>value</code> is null or the <code>value</code> is not a valid
	 *         internal value for a color.
	 *
	 *
	 */

	@Override
	public int toInteger(Module module, Object value) {
		if (value == null) {
			return -1;
		}

		// Color value.

		if (value instanceof Integer) {
			return ((Integer) value).intValue();
		}

		// A color name.

		IChoiceSet choices = getChoices();
		assert choices != null;

		if (choices.contains((String) value)) {
			return ColorUtil.parsePredefinedColor((String) value);
		}

		// Check for a custom color.

		if (module != null) {
			CustomColor customColor = module.findColor((String) value);
			if (customColor != null) {
				return customColor.getRGB();
			}
		}

		// Choice not found.

		return -1;

	}

	/**
	 * Return the Cascading Style Sheet (CSS) value for this color. It is either an
	 * RGB value in the form #RRGGBB, or a CSS-defined color name.
	 *
	 * @param module the module, provide informations about custom colors.
	 * @param value  the property value
	 * @return a String with either a CSS defined color name or an RGB color value
	 *         in CSS format. Returns <code>null</code> if the value is
	 *         <code>null</code> or if a system color cannot be found.
	 */

	public String toCssColor(Module module, Object value) {
		if (value == null) {
			return null;
		}

		// If the value is an integer, return it in CSS color format.

		if (value instanceof Integer) {
			return StringUtil.toRgbText(((Integer) value).intValue());
		}

		// The standard color names match the CSS colors.

		String colorName = (String) value;

		IChoice choice = getChoices().findChoice(colorName);
		if (choice != null) {
			return choice.getName();
		}

		// The name must be a custom color name. Return its RGB value.

		if (module != null) {
			CustomColor customColor = module.findColor(colorName);

			// This color must be undefined: a semantic error that should have
			// been previously caught.

			// Return the RGB value of the system color.

			if (customColor != null) {
				return StringUtil.toRgbText(customColor.getRGB());
			}
		}

		return null;
	}

	/**
	 * Returns a localized display string for the color. If the <code>value</code>
	 * is one of the pre-defined colors, display name of the color choice will be
	 * returned; If the <code>value</code> is from customer color pallete, then
	 * display name of the <code>CustomColor</code> will be returned; If the
	 * <code>value</code> is an integer, then the RGB value in default format set on
	 * the design session will be returned.
	 *
	 * @return the display string for the color.
	 */

	@Override
	public String toDisplayString(Module module, PropertyDefn defn, Object value) {
		if (value == null) {
			return null;
		}

		IChoiceSet choices = getChoices();

		if (value instanceof String) {
			if (choices != null) {
				IChoice color;

				// A color name.

				color = choices.findChoice((String) value);
				if (color != null) {
					// Return localized color name.

					return color.getDisplayName();
				}
			}

			assert module != null;

			// Find from customer color pallete.

			CustomColor customColor = module.findColor((String) value);
			if (customColor != null) {
				return customColor.getDisplayName(module);
			}
		} else if (value instanceof Integer) {
			// Return the default format set on the design session for RGB text.

			return ColorUtil.format(((Integer) value).intValue(), module.getSession().getColorFormat());
		}

		// assert false for other type.

		assert false;
		return null;
	}

	/**
	 * Validate a color value in locale-dependent way. The possible color valid
	 * values are the same to
	 * {@link #validateValue(Module, PropertyDefn, Object)}except that the input
	 * <code>value</code> is a String.
	 *
	 * @see #validateValue(Module, PropertyDefn, Object)
	 */

	@Override
	public Object validateInputString(Module module, DesignElement element, PropertyDefn defn, String value)
			throws PropertyValueException {
		value = StringUtil.trimString(value);
		if (value == null) {
			return null;
		}

		Object validValue = validateColor(module, value);
		if (validValue != null) {
			return validValue;
		}

		// Assume that user input a localized color name.
		// Convert the localized color name into internal name.

		IChoiceSet choices = getChoices();
		if (choices != null) {
			IChoice color = choices.findChoiceByDisplayName(value);
			if (color != null) {
				return color.getName();
			}
		}

		logger.log(Level.SEVERE, "Invalid color value " + value); //$NON-NLS-1$

		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, COLOR_TYPE);
	}

	/**
	 * A color is a either a keyword or a numerical RGB specification. Use this
	 * method to validate a color value and convert it into a internal
	 * representation.
	 * <p>
	 * A valid value can be as followings:
	 * <ul>
	 * <li>A hexadecimal number in Java or HTML format: "#rrggbb", "#rgb" or
	 * "0xRRGGBB"</li>
	 * <li>A decimal number: "16711680"</li>
	 * <li>A CSS color name: "Red", "Green".</li>
	 * <li>CSS absolute or relative format: {rgb(r,g,b)} or {rgb(r%,g%,b%)}</li>
	 * <li>A custom defined color.</li>
	 * </ul>
	 *
	 * @param module the report design, provide informations about custom colors.
	 * @param value  color value.
	 * @return the validated value in internal representations. Return
	 *         <code>null</code> if the color value doesn't match any case.
	 *
	 * @throws PropertyValueException if color value is invalid.
	 *
	 */

	private Object validateColor(Module module, String value) throws PropertyValueException {
		if (StringUtil.isBlank(value)) {
			return null;
		}

		// 1. If the value matches a predefined choice, return its name.
		// This check should be put in the first, because ColorUtil.parseColor()
		// will also do predefined color check but return an integer value.

		IChoiceSet choices = getChoices();
		if (choices != null) {
			IChoice choice = choices.findChoice(value);
			if (choice != null) {
				return choice.getName();
			}
		}

		// 2.check the followings:
		// 1) decimal number
		// 2) "#FFFF00"
		// 3) "#FF0"
		// 4) "0xFF00FF"
		// 5) "rgb(r,g,b)" or "rgb(r%,g%,b%)"

		try {
			int retValue = ColorUtil.parseColor(value);
			if (retValue != -1) {
				return Integer.valueOf(retValue);
			}
		} catch (NumberFormatException e) {
			logger.log(Level.SEVERE, "Invalid color value " + value); //$NON-NLS-1$

			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, COLOR_TYPE);
		}

		// 3. Checks in the customer color pallete.

		if (module != null) {
			CustomColor customColor = module.findColor(value);
			if (customColor != null) {
				return value;
			}
		}

		return null;
	}

	/**
	 * Return a CSS-compatible color value. It is a CSS-defined color name, or a CSS
	 * absolute RGB value( RGB(r,g,b) ).
	 *
	 * @param module the module
	 * @param value  the property value
	 * @return a CSS-compatible color value, return <code>null</code> if
	 *         <code>value</code> is null
	 *
	 */

	public String toCSSCompatibleColor(Module module, Object value) {
		if (value == null) {
			return null;
		}

		int rgbValue = -1;

		if (value instanceof String) {
			// The standard CSS colors.

			IChoice choice = getChoices().findChoice((String) value);
			if (choice != null) {
				return choice.getName();
			}

			// The name may be a custom color name, get its RGB value.

			if (module != null) {
				CustomColor customColor = module.findColor((String) value);

				if (customColor != null) {
					rgbValue = customColor.getRGB();
				}
			}

		} else if (value instanceof Integer) {
			// If the value is an integer, return it in CSS color format.

			rgbValue = ((Integer) value).intValue();
		}

		if (rgbValue != -1) {
			// return the color in css absolute format.

			return ColorUtil.format(rgbValue, ColorUtil.CSS_ABSOLUTE_FORMAT);
		}

		return null;
	}

}
