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

package org.eclipse.birt.report.engine.css.engine.value;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This class represents float values.
 *
 */
public class FloatValue extends Value implements CSSPrimitiveValue {

	private final static DecimalFormat FORMATTER = new DecimalFormat("#.###", new DecimalFormatSymbols(Locale.ENGLISH));

	/**
	 * Get the CSS text associated with the given type/value pair.
	 *
	 * @param unit
	 * @param value
	 * @return Returns the CSS text associated with the given type/value pair.
	 */
	public static String getCssText(short unit, float value) {
		if (unit < 0 || unit >= UNITS.length) {
			throw new DOMException(DOMException.SYNTAX_ERR, "");
		}
		String s = FORMATTER.format(value);
		return s + UNITS[unit - CSSPrimitiveValue.CSS_NUMBER];
	}

	/**
	 * The unit types representations
	 */
	protected final static String[] UNITS = { "", "%", "em", "ex", "px", "cm", "mm", "in", "pt", "pc", "deg", "rad",
			"grad", "ms", "s", "Hz", "kHz", "" };

	/**
	 * The float value
	 */
	protected float floatValue;

	/**
	 * The unit type
	 */
	protected short unitType;

	/**
	 * Creates a new value.
	 *
	 * @param unitType   unit type
	 * @param floatValue float value
	 */
	public FloatValue(short unitType, float floatValue) {
		this.unitType = unitType;
		this.floatValue = floatValue;
	}

	/**
	 * The type of the value.
	 */
	@Override
	public short getPrimitiveType() {
		return unitType;
	}

	/**
	 * Returns the float value.
	 */
	@Override
	public float getFloatValue() {
		return floatValue;
	}

	@Override
	public float getFloatValue(short unitType) {
		return convertFloatValue(unitType, this);
	}

	/**
	 * A string representation of the current value.
	 */
	@Override
	public String getCssText() {
		return getCssText(unitType, floatValue);
	}

	/**
	 * Returns a printable representation of this value.
	 */
	@Override
	public String toString() {
		return getCssText();
	}

	/**
	 * Converts the actual float value to the given unit type.
	 *
	 * @param unitType unit type
	 * @param value    float value
	 * @return Return the float value
	 */
	public static float convertFloatValue(short unitType, FloatValue value) {
		switch (unitType) {
		case CSSPrimitiveValue.CSS_NUMBER:
		case CSSPrimitiveValue.CSS_PERCENTAGE:
		case CSSPrimitiveValue.CSS_EMS:
		case CSSPrimitiveValue.CSS_EXS:
		case CSSPrimitiveValue.CSS_DIMENSION:
		case CSSPrimitiveValue.CSS_PX:
			if (value.getPrimitiveType() == unitType) {
				return value.getFloatValue();
			}
			break;
		case CSSPrimitiveValue.CSS_CM:
			return toCentimeters(unitType, value.getFloatValue());
		case CSSPrimitiveValue.CSS_MM:
			return toMillimeters(unitType, value.getFloatValue());
		case CSSPrimitiveValue.CSS_IN:
			return toInches(unitType, value.getFloatValue());
		case CSSPrimitiveValue.CSS_PT:
			return toPoints(unitType, value.getFloatValue());
		case CSSPrimitiveValue.CSS_PC:
			return toPicas(unitType, value.getFloatValue());
		case CSSPrimitiveValue.CSS_DEG:
			return toDegrees(unitType, value.getFloatValue());
		case CSSPrimitiveValue.CSS_RAD:
			return toRadians(unitType, value.getFloatValue());
		case CSSPrimitiveValue.CSS_GRAD:
			return toGradians(unitType, value.getFloatValue());
		case CSSPrimitiveValue.CSS_MS:
			return toMilliseconds(unitType, value.getFloatValue());
		case CSSPrimitiveValue.CSS_S:
			return toSeconds(unitType, value.getFloatValue());
		case CSSPrimitiveValue.CSS_HZ:
			return toHertz(unitType, value.getFloatValue());
		case CSSPrimitiveValue.CSS_KHZ:
			return tokHertz(unitType, value.getFloatValue());
		}
		throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
	}

	/**
	 * Converts the current value into centimeters.
	 */
	protected static float toCentimeters(int type, float value) {
		switch (type) {
		case CSSPrimitiveValue.CSS_CM:
			return value;
		case CSSPrimitiveValue.CSS_MM:
			return (value / 10);
		case CSSPrimitiveValue.CSS_IN:
			return (value * 2.54f);
		case CSSPrimitiveValue.CSS_PT:
			return (value * 2.54f / 72);
		case CSSPrimitiveValue.CSS_PC:
			return (value * 2.54f / 6);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into inches.
	 */
	protected static float toInches(int type, float value) {
		switch (type) {
		case CSSPrimitiveValue.CSS_CM:
			return (value / 2.54f);
		case CSSPrimitiveValue.CSS_MM:
			return (value / 25.4f);
		case CSSPrimitiveValue.CSS_IN:
			return value;
		case CSSPrimitiveValue.CSS_PT:
			return (value / 72);
		case CSSPrimitiveValue.CSS_PC:
			return (value / 6);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into millimeters.
	 */
	protected static float toMillimeters(int unit, float value) {
		switch (unit) {
		case CSSPrimitiveValue.CSS_CM:
			return (value * 10);
		case CSSPrimitiveValue.CSS_MM:
			return value;
		case CSSPrimitiveValue.CSS_IN:
			return (value * 25.4f);
		case CSSPrimitiveValue.CSS_PT:
			return (value * 25.4f / 72);
		case CSSPrimitiveValue.CSS_PC:
			return (value * 25.4f / 6);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into points.
	 */
	protected static float toPoints(int unit, float value) {
		switch (unit) {
		case CSSPrimitiveValue.CSS_CM:
			return (value * 72 / 2.54f);
		case CSSPrimitiveValue.CSS_MM:
			return (value * 72 / 25.4f);
		case CSSPrimitiveValue.CSS_IN:
			return (value * 72);
		case CSSPrimitiveValue.CSS_PT:
			return value;
		case CSSPrimitiveValue.CSS_PC:
			return (value * 12);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into picas.
	 */
	protected static float toPicas(int unit, float value) {
		switch (unit) {
		case CSSPrimitiveValue.CSS_CM:
			return (value * 6 / 2.54f);
		case CSSPrimitiveValue.CSS_MM:
			return (value * 6 / 25.4f);
		case CSSPrimitiveValue.CSS_IN:
			return (value * 6);
		case CSSPrimitiveValue.CSS_PT:
			return (value / 12);
		case CSSPrimitiveValue.CSS_PC:
			return value;
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into degrees.
	 */
	protected static float toDegrees(int unit, float value) {
		switch (unit) {
		case CSSPrimitiveValue.CSS_DEG:
			return value;
		case CSSPrimitiveValue.CSS_RAD:
			return (float) (value * 180 / Math.PI);
		case CSSPrimitiveValue.CSS_GRAD:
			return (value * 9 / 5);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into radians.
	 */
	protected static float toRadians(int unit, float value) {
		switch (unit) {
		case CSSPrimitiveValue.CSS_DEG:
			return (value * 5 / 9);
		case CSSPrimitiveValue.CSS_RAD:
			return value;
		case CSSPrimitiveValue.CSS_GRAD:
			return (float) (value * 100 / Math.PI);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into gradians.
	 */
	protected static float toGradians(int unit, float value) {
		switch (unit) {
		case CSSPrimitiveValue.CSS_DEG:
			return (float) (value * Math.PI / 180);
		case CSSPrimitiveValue.CSS_RAD:
			return (float) (value * Math.PI / 100);
		case CSSPrimitiveValue.CSS_GRAD:
			return value;
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into milliseconds.
	 */
	protected static float toMilliseconds(int unit, float value) {
		switch (unit) {
		case CSSPrimitiveValue.CSS_MS:
			return value;
		case CSSPrimitiveValue.CSS_S:
			return (value * 1000);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into seconds.
	 */
	protected static float toSeconds(int unit, float value) {
		switch (unit) {
		case CSSPrimitiveValue.CSS_MS:
			return (value / 1000);
		case CSSPrimitiveValue.CSS_S:
			return value;
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into Hertz.
	 */
	protected static float toHertz(int unit, float value) {
		switch (unit) {
		case CSSPrimitiveValue.CSS_HZ:
			return value;
		case CSSPrimitiveValue.CSS_KHZ:
			return (value / 1000);
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	/**
	 * Converts the current value into kHertz.
	 */
	protected static float tokHertz(int unit, float value) {
		switch (unit) {
		case CSSPrimitiveValue.CSS_HZ:
			return (value * 1000);
		case CSSPrimitiveValue.CSS_KHZ:
			return value;
		default:
			throw new DOMException(DOMException.INVALID_ACCESS_ERR, "");
		}
	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof FloatValue) {
			FloatValue f = (FloatValue) value;
			if (floatValue == f.floatValue && unitType == f.unitType) {
				return true;
			}
		}
		return false;
	}

}
