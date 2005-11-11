package org.eclipse.birt.report.engine.css.engine.value;

import org.w3c.dom.css.CSSValue;

/**
 * This singleton class represents the 'inherit' value.
 * 
 * @version $Id: InheritValue.java,v 1.2 2005/10/13 10:00:00 wyan Exp $
 */
public class InheritValue extends Value {
	/**
	 * The only instance of this class.
	 */
	public final static InheritValue INSTANCE = new InheritValue();

	/**
	 * Creates a new InheritValue object.
	 */
	protected InheritValue() {
	}

	/**
	 * A string representation of the current value.
	 */
	public String getCssText() {
		return "inherit";
	}

	/**
	 * A code defining the type of the value.
	 */
	public short getCssValueType() {
		return CSSValue.CSS_INHERIT;
	}

	/**
	 * Returns a printable representation of this object.
	 */
	public String toString() {
		return getCssText();
	}
}
