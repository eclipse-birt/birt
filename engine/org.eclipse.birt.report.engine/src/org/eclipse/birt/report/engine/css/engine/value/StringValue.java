
package org.eclipse.birt.report.engine.css.engine.value;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This class represents string values.
 * 
 * @version $Id: StringValue.java,v 1.1 2005/11/11 06:26:44 wyan Exp $
 */
public class StringValue extends Value {

	/**
	 * Returns the CSS text associated with the given type/value pair.
	 */
	public static String getCssText(short type, String value) {
		if( value == null )
		{
			return null;
		}
		
		switch (type) {
		case CSSPrimitiveValue.CSS_URI:
			return "url(" + value + ")";

		case CSSPrimitiveValue.CSS_STRING:
			char q = (value.indexOf('"') != -1) ? '\'' : '"';
			return q + value + q;
		}
		return value;
	}

	/**
	 * The value of the string
	 */
	protected String value;

	/**
	 * The unit type
	 */
	protected short unitType;

	/**
	 * Creates a new StringValue.
	 */
	public StringValue(short type, String s) {
		unitType = type;
		value = s;
	}

	/**
	 * The type of the value.
	 */
	public short getPrimitiveType() {
		return unitType;
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param obj
	 *            the reference object with which to compare.
	 */
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof StringValue)) {
			return false;
		}
		StringValue v = (StringValue) obj;
		if (unitType != v.unitType) {
			return false;
		}
		if(value!=null)
		{
			return value.equals(v.value);
		}
		else
		{
			if(v.value==null)
			{
				return true;
			}
		}
		return false;
		
	}

	/**
	 * A string representation of the current value.
	 */
	public String getCssText() {
		return getCssText(unitType, value);
	}

	/**
	 * This method is used to get the string value.
	 * 
	 * @exception DOMException
	 *                INVALID_ACCESS_ERR: Raised if the value doesn't contain a
	 *                string value.
	 */
	public String getStringValue() throws DOMException {
		return value;
	}

	/**
	 * Returns a printable representation of this value.
	 */
	public String toString() {
		return getCssText();
	}
}
