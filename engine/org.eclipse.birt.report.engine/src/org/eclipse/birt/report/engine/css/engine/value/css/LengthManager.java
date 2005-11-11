package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.value.AbstractLengthManager;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class LengthManager extends AbstractLengthManager {

	String propertyName;
	boolean inherit;
	Value defaultValue;
	
	public LengthManager(String propertyName, boolean inherit, Value defaultValue)
	{
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

}
