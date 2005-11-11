package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.value.AbstractLengthManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class BorderWidthManager extends AbstractLengthManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_THIN_VALUE, CSSValueConstants.THIN_VALUE);
		values.put(CSSConstants.CSS_MEDIUM_VALUE, CSSValueConstants.MEDIUM_VALUE);
		values.put(CSSConstants.CSS_THICK_VALUE,
				CSSValueConstants.THICK_VALUE);
	}

	String propertyName;
	public BorderWidthManager(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public boolean isInheritedProperty() {
		return false;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.NUMBER_0;
	}
	public Value createValue(LexicalUnit lu, CSSEngine engine)
			throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case LexicalUnit.SAC_IDENT:
			String s = lu.getStringValue().toLowerCase().intern();
			Object v = values.get(s);
			if (v == null) {
				throw createInvalidIdentifierDOMException(lu.getStringValue());
			}
			return (Value) v;
		}
		return super.createValue(lu, engine);
	}
}
