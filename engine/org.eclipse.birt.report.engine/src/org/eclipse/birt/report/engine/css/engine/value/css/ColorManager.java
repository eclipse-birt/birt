package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.value.AbstractColorManager;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class ColorManager extends AbstractColorManager {

    /**
     * The default color value.
     */
    protected Value defaultValue;
    protected boolean inherit;
    protected String propertyName;

	public ColorManager(String propertyName, boolean inherit, Value defaultValue)
	{
		this.propertyName = propertyName;
		this.defaultValue = defaultValue;
		this.inherit = inherit;
	}

    
    /**
     * Implements {@link
     * org.apache.batik.css.engine.value.ValueManager#isInheritedProperty()}.
     */
    public boolean isInheritedProperty() {
    	return inherit;
    }

    /**
     * Implements {@link
     * org.apache.batik.css.engine.value.ValueManager#getPropertyName()}.
     */
    public String getPropertyName() {
	return propertyName;
    }
    
    /**
     * Implements {@link
     * org.apache.batik.css.engine.value.ValueManager#getDefaultValue()}.
     */
    public Value getDefaultValue() {
        return defaultValue;
    }
}
