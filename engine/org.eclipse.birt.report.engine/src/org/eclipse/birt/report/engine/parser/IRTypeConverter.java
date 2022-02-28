/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.parser;

import java.util.HashMap;

import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.RGBColorValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * This class provides methods to convert Model types to CSSValue types.
 */
public class IRTypeConverter {
	private final static HashMap UnitMapping = new HashMap();

	static {
		UnitMapping.put(EngineIRConstants.UNITS_CM, Short.valueOf(CSSPrimitiveValue.CSS_CM));
		UnitMapping.put(EngineIRConstants.UNITS_CM, Short.valueOf(CSSPrimitiveValue.CSS_CM));
		UnitMapping.put(EngineIRConstants.UNITS_EM, Short.valueOf(CSSPrimitiveValue.CSS_EMS));
		UnitMapping.put(EngineIRConstants.UNITS_EX, Short.valueOf(CSSPrimitiveValue.CSS_EXS));
		UnitMapping.put(EngineIRConstants.UNITS_IN, Short.valueOf(CSSPrimitiveValue.CSS_IN));
		UnitMapping.put(EngineIRConstants.UNITS_MM, Short.valueOf(CSSPrimitiveValue.CSS_MM));
		UnitMapping.put(EngineIRConstants.UNITS_PC, Short.valueOf(CSSPrimitiveValue.CSS_PC));
		UnitMapping.put(EngineIRConstants.UNITS_PERCENTAGE, Short.valueOf(CSSPrimitiveValue.CSS_PERCENTAGE));
		UnitMapping.put(EngineIRConstants.UNITS_PT, Short.valueOf(CSSPrimitiveValue.CSS_PT));
		UnitMapping.put(EngineIRConstants.UNITS_PX, Short.valueOf(CSSPrimitiveValue.CSS_PX));
	}

	public static CSSValue toColor(Object value) {
		return toRGBColor(value);
	}

	public static CSSPrimitiveValue toCSSValue(short type, Object value) {
		CSSPrimitiveValue tempValue = null;
		switch (type) {
		case CSSPrimitiveValue.CSS_RGBCOLOR:
			tempValue = toRGBColor(value);
			break;

		case CSSPrimitiveValue.CSS_UNKNOWN:
			// check if it's a float value
			tempValue = toFloatValue(value);
			break;

		case CSSPrimitiveValue.CSS_STRING:
		case CSSPrimitiveValue.CSS_URI:
			return toStringValue(type, value);

		default:
			tempValue = toFloatValue(value);
		}

		if (tempValue != null) {
			return tempValue;
		}
		// convert to string value
		return toStringValue(value);
	}

	/**
	 * Converts to CSS RGBColor
	 */
	public static RGBColorValue toRGBColor(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Number) {
			return toRGBColor(((Number) value).intValue());
		} else {
			return toRGBColor(value.toString());
		}
	}

	public static RGBColorValue toRGBColor(String strValue) {
		return toRGBColor(ColorUtil.parseColor(strValue));
	}

	public static RGBColorValue toRGBColor(int intValue) {
		if (intValue > 0xFFFFFF || intValue < 0) {
			return null;
		}

		float r = intValue >> 16;
		float g = (intValue >> 8) & 0xFF;
		float b = intValue & 0xFF;

		return new RGBColorValue(toFloatValue(r), toFloatValue(g), toFloatValue(b));
	}

	/**
	 * Converts to CSS StringValue
	 */
	public static StringValue toStringValue(short type, String value) {
		return new StringValue(type, value);
	}

	public static StringValue toStringValue(short type, Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Boolean) {
			return toStringValue(type,
					((Boolean) value).booleanValue() ? BIRTConstants.BIRT_TRUE_VALUE : BIRTConstants.BIRT_FALSE_VALUE);
		}

		return toStringValue(type, value.toString());
	}

	public static StringValue toStringValue(Object value) {
		return toStringValue(CSSPrimitiveValue.CSS_STRING, value);
	}

	/**
	 * Converts various type of value to CSS FloatValue
	 */
	public static FloatValue toFloatValue(float value) {
		return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, value);
	}

	public static FloatValue toFloatValue(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Number) {
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, ((Number) value).floatValue());
		} else if (value instanceof DimensionType) {
			return toFloatValue((DimensionType) value);
		} else {
			return toFloatValue(value.toString());
		}
	}

	public static FloatValue toFloatValue(String value) {
		try {
			float fValue = Float.parseFloat(value);
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, fValue);
		} catch (NumberFormatException ex) {
		}
		return toFloatValue(DimensionType.parserUnit(value));
	}

	public static FloatValue toFloatValue(DimensionType value) {
		if (value.getValueType() == DimensionType.TYPE_DIMENSION) {
			Object obj = UnitMapping.get(value.getUnits());
			short unit;
			if (obj != null) {
				unit = ((Short) obj).shortValue();
			} else {
				unit = CSSPrimitiveValue.CSS_NUMBER;
			}

			return new FloatValue(unit, (float) value.getMeasure());
		}
		return null;
	}
}
