package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class TextAlignManager extends IdentifierManager {

    /**
     * The identifier values.
     */
    protected final static StringMap values = new StringMap();
    static {
	values.put(CSSConstants.CSS_LEFT_VALUE,
                   CSSValueConstants.LEFT_VALUE);
	values.put(CSSConstants.CSS_RIGHT_VALUE,
                   CSSValueConstants.RIGHT_VALUE);
	values.put(CSSConstants.CSS_CENTER_VALUE,
            CSSValueConstants.CENTER_VALUE);
	values.put(CSSConstants.CSS_JUSTIFY_VALUE,
            CSSValueConstants.JUSTIFY_VALUE);
    }

	public StringMap getIdentifiers() {
		return values;
	}

	public TextAlignManager(String propertyName)
	{
		this.propertyName = propertyName;
	}
	
	String propertyName;
	
	public String getPropertyName() {
		return propertyName;
	}

	public boolean isInheritedProperty() {
		return true;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.LEFT_VALUE;
	}
}
