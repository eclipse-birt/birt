package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.value.AbstractValueManager;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

public class StringManager extends AbstractValueManager {

	String propertyName;

	boolean inherit;

	Value defaultValue;

	public StringManager(String propertyName, boolean inherit,
			Value defaultValue) {
		this.propertyName = propertyName;
		this.inherit = inherit;
		this.defaultValue = defaultValue;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public boolean isInheritedProperty() {
		return inherit;
	}

	public Value getDefaultValue() {
		return defaultValue;
	}

	public Value createValue(LexicalUnit lu, CSSEngine engine)
			throws DOMException {

		switch (lu.getLexicalUnitType()) {
		case LexicalUnit.SAC_INHERIT:
			return CSSValueConstants.INHERIT_VALUE;
		default:
			return new StringValue(CSSPrimitiveValue.CSS_STRING, lu
					.getStringValue());
		}
	}

	public Value createStringValue(short type, String value,
			CSSEngine engine) throws DOMException {
		return new StringValue(CSSPrimitiveValue.CSS_STRING, value);
	}

}
