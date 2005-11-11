package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class PageBreakBeforeAfterManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_AUTO_VALUE,
				CSSValueConstants.AUTO_VALUE);
		values.put(CSSConstants.CSS_ALWAYS_VALUE,
				CSSValueConstants.ALWAYS_VALUE);
		values.put(CSSConstants.CSS_AVOID_VALUE,
				CSSValueConstants.AVOID_VALUE);
		values.put(CSSConstants.CSS_LEFT_VALUE,
				CSSValueConstants.LEFT_VALUE);
		values.put(CSSConstants.CSS_RIGHT_VALUE,
				CSSValueConstants.RIGHT_VALUE);
	}

	public StringMap getIdentifiers() {
		return values;
	}
	
	String propertyName;

	public PageBreakBeforeAfterManager( String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public boolean isInheritedProperty() {
		return false;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.AUTO_VALUE;
	}
}

